import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
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
public class CoThread<I, O> implements Function<I, CompletableFuture<O>>, Supplier<CompletableFuture<O>>, Consumer<I> {
    private final Object lock = new Object();
    private final Thread thread;

    private volatile boolean started = false;
    private volatile boolean done = false;


    private record Job<I, O>(I input, CompletableFuture<O> output) {
    }

    private final ConcurrentLinkedDeque<Job<I, O>> jobs = new ConcurrentLinkedDeque<>();


    public CoThread(BiFunction<Yield, I, O> body) {
        this.thread = new Thread(() -> {
            synchronized (lock) {
                if (jobs.isEmpty()) {
                    throw new IllegalStateException("no jobs");
                }

                final var firstJob = jobs.peek();

                // run body
                O output = null;
                Throwable exception = null;
                boolean threw = false;

                try {
                    output = body.apply(new Yield(), firstJob.input);
                } catch (Throwable e) {
                    threw = true;
                    exception = e;
                }

                done = true;

                if (jobs.isEmpty()) {
                    throw new IllegalStateException("no jobs");
                }

                final var finalJob = jobs.pop();

                // complete with final results
                if (threw) {
                    finalJob.output.completeExceptionally(exception);
                } else {
                    finalJob.output.complete(output);
                }

                while (!jobs.isEmpty()) {
                    // TODO maybe an exception would be better?
                    jobs.pop().output.complete(null);
                }
            }
        });
    }

    /**
     * Gives a job to the CoThread.
     * @param input The value to give to the CoThread.
     * @return A future that resolved to the result of the job (the next call to Yield::apply in the CoThread).
     */
    public CompletableFuture<O> apply(I input) {
        if (done) {
            // TODO maybe an exception would be better?
            return CompletableFuture.completedFuture(null);
        }

        synchronized (lock) {
            // check if done
            if (done) {
                // TODO maybe an exception would be better?
                return CompletableFuture.completedFuture(null);
            }

            // create new job and add it to the job queue
            final var output = new CompletableFuture<O>();

            jobs.add(new Job<>(input, output));

            // make sure the thread is started
            if (!started) {
                thread.start();
                started = true;
            }

            // notify the thread that there's a new job
            lock.notifyAll();

            // return the future that will resolve to the job's output
            return output;
        }
    }

    /**
     * Alias for CoThread::apply.
     */
    public CompletableFuture<O> get() {
        return this.apply(null);
    }

    /**
     * Alias for CoThread::apply.
     */
    public void accept(I input) {
        this.apply(input);
    }


    /**
     * Yielder to the calling thread.
     */
    public class Yield implements Function<O, I>, Supplier<I>, Consumer<O> {
        private Yield() {
        }

        /**
         * Yields to the calling thread and waits for the next call to CoThread::apply.
         *
         * @param output The value to give back to the calling thread.
         * @return The input provided by the next call to CoThread::apply.
         */
        public I apply(O output) {
            synchronized (lock) {
                try {
                    // wait for the previous job. this might me necessary in
                    // the case of an excepting being thrown by the completion
                    // function in the previous cycle's completable future.
                    while (jobs.isEmpty()) {
                        lock.wait();
                    }

                    // complete the previous job with the given input
                    final var previousJob = jobs.pop();
                    previousJob.output.complete(output);

                    // wait for the next job
                    while (jobs.isEmpty()) {
                        lock.wait();
                    }

                    final var job = jobs.peek();

                    // return the job's input
                    return job.input;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * Alias for Yield::apply.
         */
        public I get() {
            return this.apply(null);
        }

        /**
         * Alias for Yield::apply.
         *
         * @param output the input argument
         */
        public void accept(O output) {
            this.apply(output);
        }
    }
}
