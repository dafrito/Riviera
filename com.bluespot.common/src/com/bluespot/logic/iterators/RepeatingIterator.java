/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An iterator that loops over its iterated values.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public class RepeatingIterator<T> implements Iterator<T> {

	private Iterator<? extends T> iterator;
	private List<T> elements = new ArrayList<T>();
	private int index = 0;

	public RepeatingIterator(Iterator<? extends T> underlying) {
		if (underlying == null) {
			throw new NullPointerException("underlying must not be null");
		}
		if (!underlying.hasNext()) {
			throw new NullPointerException("underlying must contain at least one element");
		}
		this.iterator = underlying;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public T next() {
		if (this.iterator != null) {
			if (this.iterator.hasNext()) {
				T value = this.iterator.next();
				this.elements.add(value);
				return value;
			} else {
				this.iterator = null;
				return this.next();
			}
		}
		T rv = this.elements.get(index);
		index = (index + 1) % this.elements.size();
		return rv;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Removal is not supported");
	}

}
