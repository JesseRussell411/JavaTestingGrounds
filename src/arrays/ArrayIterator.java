package arrays;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
    private final T[] array;
    private int i;

    public ArrayIterator(T[] array) {
        this.array = array;
        i = 0;
    }

    public boolean hasNext() {
        return i < array.length;
    }

    public T next() {
        return array[i++];
    }
}
