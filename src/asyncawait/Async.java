package asyncawait;

import exceptions.NotImplementedException;
import functionPlus.*;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.Stream;

public class Async {

    public class AsyncRunnable extends AsyncSupplier<Void> implements Runnable {
        public void run() {
            get();
        }
    }

    public class AsyncBigFunction<T extends Iterable<?>, R> {
        public CompletableFuture<R> apply(T arguments) {
            throw new NotImplementedException();
        }
    }

    public class AsyncSupplier<R> implements Supplier<CompletableFuture<R>> {
        public CompletableFuture<R> get() {
            throw new NotImplementedException();
        }
    }

    public class AsyncConsumer<T> extends AsyncFunction<T, Void> implements Consumer<T> {
        public void accept(T t) {
            apply(t);
        }
    }

    public class AsyncFunction<T, R> extends AsyncBigFunction<Iterable<Object>, R> implements Function<T, CompletableFuture<R>> {
        public CompletableFuture<R> apply(T t) {
            return apply(List.of(t));
        }
    }


    public class AsyncBiConsumer<A, B> extends AsyncBiFunction<A, B, Void> implements BiConsumer<A, B> {
        public void accept(A a, B b) {
            apply(a, b);
        }
    }

    public class AsyncBiFunction<A, B, R> implements BiFunction<A, B, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b) {
            throw new NotImplementedException();
        }
    }


    public class AsyncTriConsumer<A, B, C> extends AsyncTriFunction<A, B, C, Void> implements TriConsumer<A, B, C> {
        public void accept(A a, B b, C c) {
            apply(a, b, c);
        }
    }

    public class AsyncTriFunction<A, B, C, R> implements TriFunction<A, B, C, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b, C c) {
            throw new NotImplementedException();
        }
    }


    public class AsyncQuadConsumer<A, B, C, D> extends AsyncQuadFunction<A, B, C, D, Void> implements QuadConsumer<A, B, C, D> {
        public void accept(A a, B b, C c, D d) {
            apply(a, b, c, d);
        }
    }

    public class AsyncQuadFunction<A, B, C, D, R> implements QuadFunction<A, B, C, D, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b, C c, D d) {
            throw new NotImplementedException();
        }
    }


    public class AsyncPentaConsumer<A, B, C, D, E> extends AsyncPentaFunction<A, B, C, D, E, Void> implements PentaConsumer<A, B, C, D, E> {
        public void accept(A a, B b, C c, D d, E e) {
            apply(a, b, c, d, e);
        }
    }

    public class AsyncPentaFunction<A, B, C, D, E, R> implements PentaFunction<A, B, C, D, E, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b, C c, D d, E e) {
            throw new NotImplementedException();
        }
    }


    public class AsyncHexaConsumer<A, B, C, D, E, F> extends AsyncHexaFunction<A, B, C, D, E, F, Void> implements HexaConsumer<A, B, C, D, E, F> {
        public void accept(A a, B b, C c, D d, E e, F f) {
            apply(a, b, c, d, e, f);
        }
    }

    public class AsyncHexaFunction<A, B, C, D, E, F, R> implements HexaFunction<A, B, C, D, E, F, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b, C c, D d, E e, F f) {
            throw new NotImplementedException();
        }
    }


    public class AsyncHeptaConsumer<A, B, C, D, E, F, G> extends AsyncHeptaFunction<A, B, C, D, E, F, G, Void> implements HeptaConsumer<A, B, C, D, E, F, G> {
        public void accept(A a, B b, C c, D d, E e, F f, G g) {
            apply(a, b, c, d, e, f, g);
        }
    }

    public class AsyncHeptaFunction<A, B, C, D, E, F, G, R> implements HeptaFunction<A, B, C, D, E, F, G, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b, C c, D d, E e, F f, G g) {
            throw new NotImplementedException();
        }
    }

    public class AsyncOctoConsumer<A, B, C, D, E, F, G, H> extends AsyncOctoFunction<A, B, C, D, E, F, G, H, Void> implements OctoConsumer<A, B, C, D, E, F, G, H> {
        public void accept(A a, B b, C c, D d, E e, F f, G g, H h) {
            apply(a, b, c, d, e, f, g, h);
        }
    }

    public class AsyncOctoFunction<A, B, C, D, E, F, G, H, R> implements OctoFunction<A, B, C, D, E, F, G, H, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b, C c, D d, E e, F f, G g, H h) {
            throw new NotImplementedException();
        }
    }


    public class AsyncNonaConsumer<A, B, C, D, E, F, G, H, I> extends AsyncNonaFunction<A, B, C, D, E, F, G, H, I, Void> implements NonaConsumer<A, B, C, D, E, F, G, H, I> {
        public void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
            apply(a, b, c, d, e, f, g, h, i);
        }
    }

    public class AsyncNonaFunction<A, B, C, D, E, F, G, H, I, R> implements NonaFunction<A, B, C, D, E, F, G, H, I, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
            throw new NotImplementedException();
        }
    }


    public class AsyncDecaConsumer<A, B, C, D, E, F, G, H, I, J> extends AsyncDecaFunction<A, B, C, D, E, F, G, H, I, J, Void> implements DecaConsumer<A, B, C, D, E, F, G, H, I, J> {
        public void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j) {
            apply(a, b, c, d, e, f, g, h, i, j);
        }
    }

    public class AsyncDecaFunction<A, B, C, D, E, F, G, H, I, J, R> implements DecaFunction<A, B, C, D, E, F, G, H, I, J, CompletableFuture<R>> {
        public CompletableFuture<R> apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j) {
            throw new NotImplementedException();
        }
    }
}


