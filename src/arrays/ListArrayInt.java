package arrays;

import java.util.AbstractList;

class ListArrayInt extends AbstractList<Integer> implements ListArray<Integer> {
    private int[] data;

    public ListArrayInt(int[] data) {
        this.data = data;
    }

    @Override
    public Integer get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}
