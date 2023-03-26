package iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorEnumerator<T> implements Enumerator<T> {
    private final Iterator<T> iterator;
    private T current = null;
    private boolean hadNext = false;
    public IteratorEnumerator(Iterator<T> iterator){
        this.iterator = iterator;
    }

    public boolean moveNext() {
        hadNext = iterator.hasNext();

        if (hadNext){
            current = iterator.next();
            return true;
        } else return false;
    }

    public T current() {
        if (hadNext) {
            return current;
        } else {
            throw new NoSuchElementException();
        }
    }
}
