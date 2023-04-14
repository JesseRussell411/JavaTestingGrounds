package cothread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A thread that acts like a coroutine, for the worst of both worlds.
 *
 * @param <I>
 * @param <O>
 */
public class CoThread<I, O> implements Function<I, CompletableFuture<O>>, Supplier<CompletableFuture<O>>, Consumer<I>, Runnable, AutoCloseable {
    /**
     * What to run the co thread with.
     */
    private final Executor executor;
    /**
     * What the co thread does.
     */
    private final Runnable threadBody;
    /**
     * The thread to run in. null if an executor was provided.
     */
    private final Thread thread;
    /**
     * The queue of jobs to run.
     */
    private final ConcurrentLinkedDeque<Job<I, O>> jobs = new ConcurrentLinkedDeque<>();
    /**
     * Whether the thread has been started.
     */
    private volatile boolean started = false;
    /**
     * Whether the co-thread is dead.
     */
    private volatile boolean done = false;

    /**
     * @return How many jobs are currently queued to run.
     */
    public int jobLoad() {
        return jobs.size();
    }

    @Override
    public void close() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    /**
     * @return The thread that is being used or null if an executor was provided.
     */
    public Thread thread() {
        return thread;
    }

    /**
     * @return Whether the body has finished running, whether by returning or throwing an exception.
     * <p>
     * Note that the thread or executor may still be running for a short time afterwards in order to complete
     * any remaining jobs or to attend to any other cleanup tasks.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Creates a CoThread in the given executor.
     * **Note** that if the executor executes in the same thread as the caller,
     * a deadlock will occur as the caller's thread is stuck waiting for itself
     * to add a new job.
     */
    public CoThread(Executor executor, BiFunction<Yield, I, O> body) {
        this.executor = executor;
        this.threadBody = createThreadBody(body);
        this.thread = null;
    }

    /**
     * Creates a CoThread in the given threadGroup with the given name.
     */
    public CoThread(ThreadGroup threadGroup, String name, BiFunction<Yield, I, O> body) {
        this.threadBody = createThreadBody(body);
        this.thread = new Thread(threadGroup, this.threadBody, name);
        this.executor = defaultExecutor();
    }

    /**
     * Creates a CoThread with the given name.
     */
    public CoThread(String name, BiFunction<Yield, I, O> body) {
        this.threadBody = createThreadBody(body);
        this.thread = new Thread(this.threadBody, name);
        this.executor = defaultExecutor();
    }

    /**
     * Creates a CoThread in the given threadGroup.
     */
    public CoThread(ThreadGroup threadGroup, BiFunction<Yield, I, O> body) {
        this.threadBody = createThreadBody(body);
        this.thread = new Thread(threadGroup, this.threadBody);
        this.executor = defaultExecutor();
    }

    /**
     * Creates a CoThread.
     */
    public CoThread(BiFunction<Yield, I, O> body) {
        this.threadBody = createThreadBody(body);
        this.thread = new Thread(this.threadBody);
        this.executor = defaultExecutor();
    }

    private Executor defaultExecutor() {
        return (Runnable runnable) -> {
            assert runnable == threadBody;
            this.thread.start();
        };
    }

    private Runnable createThreadBody(BiFunction<Yield, I, O> body) {
        return () -> {
            try {
                final Job<I, O> initialJob;
                synchronized (this) {
                    while (jobs.isEmpty()) this.wait();
                    initialJob = jobs.peek();
                }

                O output = null;
                Throwable exception = null;
                boolean threw = false;

                try {
                    // run body
                    output = body.apply(new Yield(), initialJob.input);
                } catch (Throwable e) {
                    threw = true;
                    exception = e;
                }


                final Job<I, O> finalJob;
                synchronized (this) {
                    while (jobs.isEmpty()) this.wait();
                    finalJob = jobs.pop();
                }

                // complete with final results
                if (threw) {
                    finalJob.output.completeExceptionally(exception);
                } else {
                    finalJob.output.complete(output);
                }

                done = true;

                // complete the remaining jobs
                completeAllJobsBecauseDone();
            } catch (InterruptedException e) {
                throw new CoThreadInterruptedException(e);
            } finally {
                // make sure done flag is raised
                done = true;
            }
        };
    }

    private synchronized void startIfNotStarted() {
        if (!started) {
            executor.execute(threadBody);
            started = true;
        }
    }

    private CompletableFuture<O> completeJobBecauseDone(Job<I, O> job) {
        assert done;

        // TODO maybe an exception would be better?
        job.output.complete(null);
        return job.output;
    }

    private void completeAllJobsBecauseDone() {
        assert done;

        Job<I, O> job;
        while (true) {
            job = jobs.poll();
            if (job == null) break;
            completeJobBecauseDone(job);
        }
    }

    /**
     * Adds a new job to the queue.
     *
     * @param input The value to give to the cothread.CoThread.
     * @return A future that resolved to the result of the job (the next call to Yield::apply in the cothread.CoThread).
     */
    public CompletableFuture<O> apply(I input) {
        // create new job
        final var job = new Job<I, O>(input);

        // check if done
        if (done) return completeJobBecauseDone(job);

        // add new job to queue
        jobs.add(job);

        synchronized (this) {
            // make sure the thread is running
            startIfNotStarted();

            // notify thread of new job
            this.notifyAll();
        }

        // return job's future output
        return job.output;
    }

    /**
     * Alias for apply(nulL).
     */
    public CompletableFuture<O> get() {
        return this.apply(null);
    }

    /**
     * Alias for apply.
     */
    public void accept(I input) {
        this.apply(input);
    }

    /**
     * alias for apply(null)
     */
    public void run() {
        this.apply(null);
    }


    /**
     * Yielder to the calling thread.
     */
    public class Yield implements Function<O, I>, Supplier<I>, Consumer<O>, Runnable {
        private Yield() {
        }

        /**
         * Yields to the calling thread and waits for the next call to cothread.CoThread::apply.
         *
         * @param output The value to give back to the calling thread.
         * @return The input provided by the next call to cothread.CoThread::apply.
         */
        public I apply(O output) throws CoThreadInterruptedException {

            try {
                final Job<I, O> previousJob;
                synchronized (this) {
                    // wait for the previous job
                    while (jobs.isEmpty()) this.wait();
                    previousJob = jobs.pop();
                }
                // complete the previous job with the given input
                previousJob.output.complete(output);
                synchronized (this) {
                    // wait for the next job
                    while (jobs.isEmpty()) this.wait();
                    final var job = jobs.peek();
                    // return the job's input
                    return job.input;
                }
            } catch (InterruptedException e) {
                throw new CoThreadInterruptedException(e);
            }
        }

        /**
         * Alias for apply(null).
         */
        public I get() throws CoThreadInterruptedException {
            return this.apply(null);
        }

        /**
         * Alias for apply.
         */
        public void accept(O output) throws CoThreadInterruptedException {
            this.apply(output);
        }

        /**
         * Alias for apply(null).
         */
        public void run() throws CoThreadInterruptedException {
            this.apply(null);
        }
    }

    public static class CoThreadInterruptedException extends RuntimeException {
        private CoThreadInterruptedException(InterruptedException cause) {
            super(cause);
        }
    }

    /**
     * A job to be run by the body.
     */
    private static class Job<I, O> {
        /**
         * The input value to return from Yield::apply or to be given to
         * the body function's second argument on its first call.
         */
        public final I input;
        /**
         * The future that is to be completed with the value supplied to the next call
         * to Yield::apply or the value returned from the body or to be completed
         * exceptionally with the exception thrown from the body.
         */
        public final CompletableFuture<O> output;

        public Job(I input) {
            this.input = input;
            this.output = new CompletableFuture<>();
        }
    }
}
