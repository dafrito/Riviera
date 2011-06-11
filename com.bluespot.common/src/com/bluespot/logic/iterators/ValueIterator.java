/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.Iterator;

import com.bluespot.logic.values.Value;

/**
 * An {@link Iterator} that returns the values retrieved from
 * {@link Value#get()}.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public class ValueIterator<T> implements Iterator<T> {

	private final Value<? extends T> value;

	public ValueIterator(final Value<? extends T> value) {
		if (value == null) {
			throw new NullPointerException("value must not be null");
		}
		this.value = value;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public T next() {
		return this.value.get();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Removal is not supported");
	}

}
