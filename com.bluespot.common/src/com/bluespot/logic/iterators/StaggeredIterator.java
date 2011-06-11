/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public class StaggeredIterator<T> extends CompositeIterator<T> {

	private int index = 0;

	public StaggeredIterator(Iterator<? extends T> iterator) {
		super(iterator);
	}

	private void nextIterator() {
		this.lock();
		while (!this.iterators.isEmpty() && !this.iterators.get(index).hasNext()) {
			this.iterators.remove(index);
		}
	}

	@Override
	public boolean hasNext() {
		this.nextIterator();
		return this.iterators.get(index).hasNext();
	}

	@Override
	public T next() {
		this.nextIterator();
		if (this.iterators.isEmpty()) {
			throw new NoSuchElementException("Iterator cannot be iterated beyond its last element");
		}
		Iterator<? extends T> iter = this.iterators.get(index);
		index = (index + 1) % this.iterators.size();
		return iter.next();
	}

	@Override
	public void remove() {
		this.nextIterator();
		if (this.iterators.isEmpty()) {
			throw new IllegalStateException("No iterator is available for removal");
		}
		this.iterators.get(index).remove();
	}

}
