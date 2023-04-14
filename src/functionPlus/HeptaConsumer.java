package functionPlus;

@FunctionalInterface
public interface HeptaConsumer<A, B, C, D, E, F, G> {
    void accept(A a, B b, C c, D d, E e, F f, G g);

    default HeptaConsumer<A, B, C, D, E, F, G> andThen(HeptaConsumer<A, B, C, D, E, F, G> after) {
        return (a, b, c, d, e, f, g) -> {
            accept(a, b, c, d, e, f, g);
            after.accept(a, b, c, d, e, f, g);
        };
    }
}
