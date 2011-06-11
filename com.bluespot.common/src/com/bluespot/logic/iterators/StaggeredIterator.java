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

	private Iterator<Iterator<? extends T>> iterIterator;

	private T next;

	public StaggeredIterator(Iterator<? extends T> iterator) {
		super(iterator);
	}

	private void seek() {
		this.lock();
		while (!this.iterators.isEmpty()) {
			if (this.iterIterator == null || !this.iterIterator.hasNext()) {
				this.iterIterator = this.iterators.iterator();
			}
			Iterator<? extends T> current = this.iterIterator.next();
			if (current.hasNext()) {
				this.next = current.next();
				return;
			} else {
				this.iterIterator.remove();
			}
		}
	}

	@Override
	public boolean hasNext() {
		if (!this.isLocked()) {
			this.seek();
		}
		return !this.iterators.isEmpty();
	}

	@Override
	public T next() {
		if (this.iterators.isEmpty()) {
			throw new NoSuchElementException("No more elements in iterator");
		}
		T rv = this.next;
		this.seek();
		return rv;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Removal is not supported");
	}

}
