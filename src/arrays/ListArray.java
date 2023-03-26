package arrays;

import java.util.List;

public interface ListArray<T> extends List<T> {
    static <T> ListArrayGeneric<T> over(T[] data) {
        return new ListArrayGeneric<>(data);
    }

    static ListArrayBoolean over(boolean[] data) {
        return new ListArrayBoolean(data);
    }

    static ListArrayByte over(byte[] data) {
        return new ListArrayByte(data);
    }

    static ListArrayChar over(char[] data) {
        return new ListArrayChar(data);
    }

    static ListArrayDouble over(double[] data) {
        return new ListArrayDouble(data);
    }

    static ListArrayFloat over(float[] data) {
        return new ListArrayFloat(data);
    }

    static ListArrayInt over(int[] data) {
        return new ListArrayInt(data);
    }

    static ListArrayLong over(long[] data) {
        return new ListArrayLong(data);
    }

    static ListArrayShort over(short[] data) {
        return new ListArrayShort(data);
    }
}


















