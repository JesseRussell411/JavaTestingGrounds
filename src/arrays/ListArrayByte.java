package arrays;

import java.util.AbstractList;

class ListArrayByte extends AbstractList<Byte> implements ListArray<Byte> {
    private byte[] data;

    public ListArrayByte(byte[] data) {
        this.data = data;
    }

    @Override
    public Byte get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}

