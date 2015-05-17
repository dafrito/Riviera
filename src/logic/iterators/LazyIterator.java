/**
 * 
 */
package logic.iterators;

import java.util.Iterator;

/**
 * An {@link Iterator} that creates its iterator immediately before iterating.
 * This allows actions to be taken on the iterable during previous steps.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * @see Iterators#saving(Iterator)
 */
public class LazyIterator<T> implements Iterator<T> {

	private final Iterable<? extends T> iterable;
	private Iterator<? extends T> iterator;

	public LazyIterator(Iterable<? extends T> iterable) {
		if (iterable == null) {
			throw new NullPointerException("iterable must not be null");
		}
		this.iterable = iterable;
	}

	private void getIterator() {
		if (this.iterator == null) {
			this.iterator = this.iterable.iterator();
		}
	}

	@Override
	public boolean hasNext() {
		this.getIterator();
		return this.iterator.hasNext();
	}

	@Override
	public T next() {
		this.getIterator();
		return this.iterator.next();
	}

	@Override
	public void remove() {
		this.getIterator();
		this.iterator.remove();
	}

}
