package arrays;

import java.util.AbstractList;

class ListArrayChar extends AbstractList<Character> implements ListArray<Character> {
    private char[] data;

    public ListArrayChar(char[] data) {
        this.data = data;
    }

    @Override
    public Character get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }
}
