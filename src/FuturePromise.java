import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.function.Function;

public class FuturePromise<T> {
    private final CompletableFuture<T> completableFuture;

    public FuturePromise(CompletableFuture<T> completableFuture) {
        this.completableFuture = completableFuture;
    }

    public FuturePromise(CompletionStage<T> completionStage) {
        this.completableFuture = new CompletableFuture<>();
        completionStage.thenAccept(this.completableFuture::complete);
        completionStage.exceptionally(e -> {
            this.completableFuture.completeExceptionally(e);
            return null;
        });
    }

    public <R> FuturePromise<R> thenApply(Function<? super T, ? extends R> action) {
        final var next = this.completableFuture.thenApply(action);
        return (FuturePromise<R>) new FuturePromise<>(next);
    }
}
