package com.bluespot.collections.table;

import com.bluespot.geom.vectors.Vector3i;

/**
 * Skeletal implementation of the {@link TableIterator} interface.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            Type of element contained in this iterator's parent table
 * @see StrategyTableIterator
 */
public abstract class AbstractTableIterator<T> implements TableIterator<T> {

	/**
	 * The current position of this iterator
	 */
	protected Vector3i currentPoint;

	/**
	 * The table used in iteration
	 */
	protected final Table<T> table;

	/**
	 * Constructs an iterator over the specified table.
	 * 
	 * @param table
	 *            the table used for iteration
	 */
	public AbstractTableIterator(final Table<T> table) {
		this.table = table;
	}

	@Override
	public T get() {
		if (this.currentPoint == null) {
			this.next();
		}
		return this.table.get(this.currentPoint);
	}

	@Override
	public Vector3i getLocation() {
		if (this.currentPoint == null) {
			this.next();
		}
		return this.currentPoint.toFrozen();
	}

	@Override
	public void getLocation(final Vector3i targetPoint) {
		if (this.currentPoint == null) {
			this.next();
		}
		targetPoint.set(this.currentPoint);
	}

	@Override
	public T put(final T value) {
		if (this.currentPoint == null) {
			this.next();
		}
		return this.table.put(this.currentPoint, value);
	}

	@Override
	public void remove() {
		if (this.currentPoint == null) {
			this.next();
		}
		this.table.remove(this.currentPoint);
	}
}
