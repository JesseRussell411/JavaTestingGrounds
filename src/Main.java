import arrays.ArrayIterator;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class Main {
    public static String iterToString(Iterator<?> iter, String delim) {
        final var bob = new StringBuilder();

        if (!iter.hasNext()) return bob.toString();
        bob.append(iter.next());

        while (iter.hasNext()) {
            final var item = iter.next();
            bob.append(delim);
            bob.append(item);
        }

        return bob.toString();
    }

    public static String iterToString(Iterator<?> iter) {
        return iterToString(iter, ", ");
    }

    public static void printi(Iterable<?> iterable) {
        System.out.println(iterToString(iterable.iterator()));
    }

    public static void printi(Object[] array) {
        System.out.println(iterToString(new ArrayIterator<>(array)));
    }

    public static void printi(Stream<?> stream) {
        System.out.println(iterToString(stream.iterator()));
    }


    public static void main(String[] args) {
        var ttl = new TwoTailList<Integer>(0);

        for (int i = 0; i < 10; i++) {
            ttl.add(i);
        }

        printi(ttl);

        ttl.remove(ttl.indexOf(4));
        printi(ttl);
        ttl.remove(ttl.indexOf(3));
        printi(ttl);
        ttl.remove(ttl.indexOf(8));
        printi(ttl);
        printi(ttl);
        ttl.remove(ttl.indexOf(0));
        printi(ttl);

        printi(Stream.of(1, 32, 3));


        final CoThread<Integer, Integer> ct = new CoThread<Integer, Integer>((yield, i) -> {
            i = yield.apply(i * 10);

            while (i != null) {
                i = yield.apply(i * 10);
            }

            return 42;
        });

        try {
            final var f1 = ct.apply(1);
            final var f3 = ct.apply(3);
            final var f2 = ct.apply(2);
            final var fnull = ct.apply(null);
            final var fdone1 = ct.apply(1337);
            final var fdone2 = ct.apply(1337);
            final var fdone3 = ct.apply(1337);
            final var fdone4 = ct.apply(1337);

            System.out.println(f1.get());
            System.out.println(f2.get());
            System.out.println(f3.get());
            System.out.println(fnull.get());
            System.out.println(fdone1.get());
            System.out.println(fdone2.get());
            System.out.println(fdone3.get());
            System.out.println(fdone4.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        try {
            throw null;
        } catch (Throwable e){
            System.out.println(e);
        }

//        for(int j = 0; j < 10; j++) {
//            var ttl = new TwoTailList<Integer>();
//            for (int i = 0; i < 100000; i++) {
//                ttl.add(i);
//            }
//
//            long start = System.currentTimeMillis();
//            for (int i = 0; i < 10000; i++) {
//                ttl.remove(ttl.size() / 2);
//            }
//            long stop = System.currentTimeMillis();
//
//            System.out.println("time: " + (stop - start));
//        }
    }
}