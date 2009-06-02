package com.bluespot.table;

import java.awt.Point;

/**
 * A {@code Table} implementation backed by a 2-dimensional array of fixed size.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            The type of element in this table
 * @see Table
 */
public class ArrayTable<T> extends AbstractTable<T> {

	protected final T[][] array;

	public ArrayTable(final int width, final int height) {
		this(width, height, (T) null);
	}

	@SuppressWarnings("unchecked")
	public ArrayTable(final int width, final int height, final T defaultValue) {
		super(defaultValue);
		this.array = (T[][]) new Object[height][width];
	}

	@Override
	public T get(final Point location) {
		final T value = this.array[location.y][location.x];
		if (value != null) {
			return value;
		}
		return super.get(location);
	}

	@Override
	public int getHeight() {
		return this.array.length;
	}

	@Override
	public int getWidth() {
		if (this.array.length == 0) {
			return 0;
		}
		return this.array[0].length;
	}

	@Override
	public T put(final Point location, final T element) {
		final T old = this.array[location.y][location.x];
		this.array[location.y][location.x] = element;
		return old != null ? old : this.getDefaultValue();
	}

}
