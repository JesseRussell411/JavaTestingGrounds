package arrays;

import java.util.AbstractList;

class ListArrayBoolean extends AbstractList<Boolean> implements ListArray<Boolean> {
    private boolean[] data;

    public ListArrayBoolean(boolean[] data) {
        this.data = data;
    }

    @Override
    public Boolean get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}
