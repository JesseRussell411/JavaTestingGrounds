package iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EnumeratorIterator<E> implements Iterator<E> {
    private final Enumerator<E> enumerator;
    private boolean movedNext = false;

    public EnumeratorIterator(Enumerator<E> enumerator) {
        this.enumerator = enumerator;
    }


    @Override
    public boolean hasNext() {
        if (!movedNext) {
            movedNext = enumerator.moveNext();
        }
        return movedNext;
    }

    @Override
    public E next() {
        if (!movedNext) {
            movedNext = enumerator.moveNext();
        }

        if (movedNext) {
            movedNext = false;
            return enumerator.current();
        } else {
            throw new NoSuchElementException();
        }
    }
}
