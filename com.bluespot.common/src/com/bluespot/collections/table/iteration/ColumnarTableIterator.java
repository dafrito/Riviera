/**
 * 
 */
package com.bluespot.collections.table.iteration;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.vectors.Vector3i;

/**
 * A {@link TableIterator} that iterates column-first.
 * 
 * <pre>
 * 1 4
 * 2 5
 * 3 6
 * </pre>
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public class ColumnarTableIterator<T> extends AbstractTableIterator<T> {

	private boolean started = false;

	private final Vector3i previous = Vector3i.mutable();

	public ColumnarTableIterator(Table<T> table) {
		super(table);
	}

	private boolean hasStarted() {
		return started;
	}

	@Override
	public Vector3i offset() {
		return position.subtracted(previous);
	}

	@Override
	public boolean hasNext() {
		if (!this.hasStarted() && table.size() > 0) {
			return true;
		}
		if (position.x() < table.width() - 1) {
			return true;
		}
		return position.y() < table.height() - 1;
	}

	@Override
	public T next() {
		if (this.hasStarted()) {
			previous.set(position);
			this.doNext();
		}
		started = true;
		return table.get(position);
	}

	protected void doNext() {
		position.addY(1);
		if (position.y() < table.height()) {
			return;
		}
		position.addX(1);
		position.setY(0);
		if (position.x() >= table.width()) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean hasPrevious() {
		if (position == null) {
			return false;
		}
		if (position.y() > 0) {
			return true;
		}
		return position.x() > 0;
	}

	@Override
	public T previous() {
		if (this.hasStarted()) {
			previous.set(position);
			this.doPrevious();
		}
		return table.get(position);
	}

	protected void doPrevious() {
		position.subtractX(1);
		if (position.x() >= 0) {
			return;
		}
		position.subtractY(1);
		position.setX(this.table.width() - 1);
		if (position.y() < 0) {
			throw new IndexOutOfBoundsException();
		}
	}
}
