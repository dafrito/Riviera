/**
 * 
 */
package logic.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public class ChainingIterator<T> extends CompositeIterator<T> {

	public ChainingIterator(Iterator<? extends T> iterator) {
		super(iterator);
	}

	private void nextIterator() {
		this.lock();
		while (!this.iterators.isEmpty() && !this.iterators.get(0).hasNext()) {
			this.iterators.remove(0);
		}
	}

	@Override
	public boolean hasNext() {
		this.nextIterator();
		return !this.iterators.isEmpty();
	}

	@Override
	public T next() {
		this.nextIterator();
		if (this.iterators.isEmpty()) {
			throw new NoSuchElementException("Iterator cannot be iterated beyond its last element");
		}
		return this.iterators.get(0).next();
	}

	@Override
	public void remove() {
		this.nextIterator();
		if (this.iterators.isEmpty()) {
			throw new IllegalStateException("No iterator is available for removal");
		}
		this.iterators.get(0).remove();
	}

}
