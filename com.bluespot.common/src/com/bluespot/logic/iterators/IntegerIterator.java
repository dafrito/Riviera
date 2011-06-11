/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} of {@link Integer} values.
 * 
 * @author Aaron Faanes
 * 
 */
public class IntegerIterator implements Iterator<Integer> {

	private int count;
	private final int increment;
	private final int lastValue;

	public IntegerIterator() {
		this(0, 1);
	}

	public IntegerIterator(final int initial) {
		this(initial, 1);
	}

	public IntegerIterator(final int initial, int increment) {
		if (increment == 0) {
			throw new IllegalArgumentException("Zero increment is not allowed");
		}
		this.count = initial;
		this.increment = increment;
		this.lastValue = this.count - this.increment;
	}

	/**
	 * Construct an {@link IntegerIterator} that begins at the specified value
	 * and increments using the specified increment value.
	 * 
	 * @param initial
	 *            the initial value that will be returned by the first call to
	 *            {@link #next()}
	 * @param increment
	 *            the increment used
	 * @param lastValue
	 *            the last value that will be returned
	 * @throws IllegalArgumentException
	 *             if {@code increment} is zero
	 */
	public IntegerIterator(final int initial, final int increment, final int lastValue) {
		if (increment == 0) {
			throw new IllegalArgumentException("Zero increment is not allowed");
		}
		this.count = initial;
		this.increment = increment;
		this.lastValue = lastValue;
		if (this.increment > 0 && this.count > this.lastValue) {
			throw new IllegalArgumentException("Increment must be negative if initial > lastValue");
		} else if (this.increment < 0 && this.count < this.lastValue) {
			throw new IllegalArgumentException("Increment must be positive if initial < lastValue");
		}
	}

	@Override
	public boolean hasNext() {
		if (this.increment > 0) {
			return this.count <= this.lastValue;
		}
		return this.count >= this.lastValue;
	}

	/**
	 * Retrieve the next {@link Integer} value.
	 * 
	 * @see #nextInt()
	 */
	@Override
	public Integer next() {
		return nextInt();
	}

	/**
	 * Retrieve the next integer value, as a primitive.
	 * 
	 * @return the next integer value
	 * 
	 * @see #next()
	 */
	public int nextInt() {
		if (!this.hasNext()) {
			throw new NoSuchElementException("No more integers available");
		}
		int returned = count;
		count += increment;
		return returned;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Removal is not supported");
	}

}
