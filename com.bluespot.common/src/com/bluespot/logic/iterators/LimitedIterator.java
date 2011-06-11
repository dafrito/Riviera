/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that will return up to a specified number of elements. This is
 * useful to bound an unlimited iterator.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 */
public class LimitedIterator<T> implements Iterator<T> {

	private final Iterator<? extends T> iterator;
	private int charges;

	public LimitedIterator(Iterator<? extends T> underlying, int charges) {
		if (underlying == null) {
			throw new NullPointerException("underlying must not be null");
		}
		if (charges < 0) {
			throw new IllegalArgumentException("charges must be positive");
		}
		this.iterator = underlying;
		this.charges = charges;
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext() && this.charges > 0;
	}

	@Override
	public T next() {
		if (this.charges-- <= 0) {
			throw new NoSuchElementException("charges have been depleted");
		}
		return this.iterator.next();
	}

	@Override
	public void remove() {
		this.iterator.remove();
	}

}
