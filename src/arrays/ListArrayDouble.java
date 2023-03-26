package arrays;

import java.util.AbstractList;

class ListArrayDouble extends AbstractList<Double> implements ListArray<Double> {
    private double[] data;

    public ListArrayDouble(double[] data) {
        this.data = data;
    }

    @Override
    public Double get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}
