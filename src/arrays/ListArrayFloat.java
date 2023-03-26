package arrays;

import java.util.AbstractList;

class ListArrayFloat extends AbstractList<Float> implements ListArray<Float> {
    private float[] data;

    public ListArrayFloat(float[] data) {
        this.data = data;
    }

    @Override
    public Float get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}
