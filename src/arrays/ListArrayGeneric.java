package arrays;

import java.util.AbstractList;

class ListArrayGeneric<T> extends AbstractList<T> implements ListArray<T> {
    private T[] data;

    public ListArrayGeneric(T[] data) {
        this.data = data;
    }

    @Override
    public T get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}
