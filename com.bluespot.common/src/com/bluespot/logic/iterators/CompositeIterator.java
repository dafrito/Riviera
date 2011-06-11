/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public abstract class CompositeIterator<T> implements Iterator<T> {

	protected final List<Iterator<? extends T>> iterators = new LinkedList<Iterator<? extends T>>();
	private boolean locked = false;

	protected CompositeIterator(Iterator<? extends T> iterator) {
		this.add(iterator);
	}

	public CompositeIterator<T> add(Iterator<? extends T> iterator) {
		if (this.locked) {
			throw new IllegalStateException("Iterator no longer accepts new iterators");
		}
		if (iterator == null) {
			throw new NullPointerException("iterator must not be null");
		}
		this.iterators.add(iterator);
		return this;
	}

	public CompositeIterator<T> addAll(Iterable<? extends Iterator<? extends T>> iterators) {
		if (this.locked) {
			throw new IllegalStateException("Iterator no longer accepts new iterators");
		}
		if (iterators == null) {
			throw new NullPointerException("iterators must not be null");
		}
		for (Iterator<? extends T> iter : iterators) {
			this.add(iter);
		}
		return this;
	}

	protected void lock() {
		this.locked = true;
	}

	protected boolean isLocked() {
		return this.locked;
	}
}
