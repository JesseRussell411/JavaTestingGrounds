package arrays;

import java.util.AbstractList;

class ListArrayLong extends AbstractList<Long> implements ListArray<Long> {
    private long[] data;

    public ListArrayLong(long[] data) {
        this.data = data;
    }

    @Override
    public Long get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}
