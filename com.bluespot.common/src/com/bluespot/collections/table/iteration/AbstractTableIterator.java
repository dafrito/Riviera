package com.bluespot.collections.table.iteration;

import com.bluespot.collections.table.Table;
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
	protected final Vector3i position = Vector3i.mutable();

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
		if (this.position == null) {
			this.next();
		}
		return this.table.get(this.position);
	}

	@Override
	public Vector3i location() {
		if (this.position == null) {
			this.next();
		}
		return this.position.toFrozen();
	}

	@Override
	public T put(final T value) {
		if (this.position == null) {
			this.next();
		}
		return this.table.put(this.position, value);
	}

	@Override
	public void remove() {
		if (this.position == null) {
			this.next();
		}
		this.table.remove(this.position);
	}
}
