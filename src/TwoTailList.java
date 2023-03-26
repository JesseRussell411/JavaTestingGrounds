import java.util.*;

/**
 * A list that can be efficiently added to at both ends.
 * Items can be pushed onto index 0 just as quickly as they can be added at the end.
 *
 * @param <T>
 */
public class TwoTailList<T> extends AbstractList<T> {
    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    public transient Object[] data;
    private transient int size = 0;
    private transient int offset;

    public TwoTailList(int initialCapacity) {
        data = new Object[initialCapacity];
        offset = newOffset(data.length, 0);
    }

    public TwoTailList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public int size() {
        return size;
    }

    public void trimToSize() {
        final var newData = new Object[size];

        System.arraycopy(data, offset, newData, 0, size);

        data = newData;
        offset = 0;
    }

    private int requireIndexInBounds(int index) {
        if (0 <= index && index < size) {
            return index;
        } else {
            throw new IndexOutOfBoundsException(index);
        }
    }

    private static int newOffset(int capacity, int size) {
        return Numbers.ceilingDivision(capacity, 2) - Numbers.ceilingDivision(size, 2);
    }

    private Object[] reallocate(int neededCapacity) {
        int newCapacity = Math.max(1, data.length);
        while (newCapacity < neededCapacity) {
            newCapacity = newCapacity * 2;
        }
        return new Object[newCapacity];
    }

    private int freeSpacePreceding() {
        return offset;
    }

    private int freeSpaceFollowing() {
        return data.length - (offset + size);
    }

    @Override
    public T get(int index) {
        requireIndexInBounds(index);
        return (T) data[offset + index];
    }


    @Override
    public T set(int index, T element) {
        requireIndexInBounds(index);
        data[offset + index] = element;
        return element;
    }

    private void growLeft(int newSize) {
        if (freeSpacePreceding() < newSize - size) {
            final var newData = reallocate(newSize);
            final var newOffset = newOffset(newData.length, newSize);
            System.arraycopy(data, offset, newData, newOffset + 1, newSize - 1);

            offset = newOffset;
            data = newData;
            size = newSize;
        } else {
            offset -= (size - newSize);
            size = newSize;
        }
    }

    private void growRight(int newSize){
        if (freeSpaceFollowing() < (newSize - size)) {
            final var newData = reallocate(newSize);
            final var newOffset = newOffset(newData.length, newSize);
            System.arraycopy(data, offset, newData, newOffset, newSize - 1);
            offset = newOffset;
            data = newData;
            size = newSize;
        } else {
            size = newSize;
        }
    }

    /**
     * Adds the element to the end of the list.
     *
     * @param element element whose presence in this collection is to be ensured
     * @return true
     */
    @Override
    public boolean add(T element) {
        growRight(size + 1);
        data[offset + size - 1] = element;
        return true;
    }

    /**
     * Adds the element to the start of the list at index 0. All following items are shifted forward to
     * accommodate it. Of course, they're not really shifted forward internally, the time complexity
     * of this method is O(1) unless the internal array needs to be reallocated in which case it's
     * O(n), with n being the length of the list.
     *
     * @param element element whose presence in this collection is to be ensured.
     * @return true
     */
    public boolean push(T element) {
        growLeft(size + 1);
        data[offset] = element;
        return true;
    }

    @Override
    public T remove(int index) {
        requireIndexInBounds(index);
        final T removedElement = (T) data[index + offset];

        final int effectiveIndex = index + offset;
        if (index > size / 2) {
            // move data to the right of the index left
            final int end = offset + size;
            System.arraycopy(data, effectiveIndex + 1, data, effectiveIndex, end - effectiveIndex - 1);
            size -= 1;
        } else {
            // move data to the left of the index right
            System.arraycopy(data, offset, data, offset + 1, index);
            size -= 1;
            offset += 1;
        }

        return removedElement;
    }

    @Override
    public boolean remove(Object element) {
        final int index = indexOf(element);
        if (index == -1) return false;
        remove(index);
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TwoTailList<?> other && size() != other.size()) return false;
        if (obj instanceof ArrayList<?> other && size() != other.size()) return false;
        if (obj instanceof LinkedList<?> other && size() != other.size()) return false;
        return super.equals(obj);
    }

    public ArrayList<T> toArrayList() {
        final var al = new ArrayList<T>(size);
        al.addAll(this);
        return al;
    }

    public void insert(int index, T element) {
        if (index != size) requireIndexInBounds(index);

        if (index > size / 2) {
            // move data to the right of the index to make room
            growRight(size + 1);
            final int effectiveIndex = index + offset;
            final int end = offset + size;
            System.arraycopy(data, effectiveIndex, data, effectiveIndex + 1, end - effectiveIndex);
            data[effectiveIndex] = element;
        } else {
            // move data to the left the index to make room
            growLeft(size + 1);
            System.arraycopy(data, offset + 1, data, offset, index);
            final int effectiveIndex = index + offset;
            data[effectiveIndex] = element;
        }
    }
}
