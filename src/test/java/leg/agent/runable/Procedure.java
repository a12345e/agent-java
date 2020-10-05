package leg.agent.runable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Procedure<T>  implements Iterable <T>{
    @Override
    public Iterator<T> iterator() {
        return null;
    }

    public class ProcdureIterator<E> implements Iterator<E> {
        List<E> iteratee;
        public ProcdureIterator(List<E> iteratee) {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }
    }
}
