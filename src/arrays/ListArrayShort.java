package arrays;

import java.util.AbstractList;

class ListArrayShort extends AbstractList<Short> implements ListArray<Short> {
    private short[] data;

    public ListArrayShort(short[] data) {
        this.data = data;
    }

    @Override
    public Short get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}
