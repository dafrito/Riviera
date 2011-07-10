/**
 * 
 */
package com.bluespot.collections.table.iteration;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.vectors.Vector3i;

/**
 * A {@link TableIterator} that orders elements in a natural reading order.
 * <p>
 * 
 * <pre>
 * 1 2 3
 * 4 5 6
 * </pre>
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public class NaturalTableIterator<T> extends AbstractTableIterator<T> {

	private boolean started = false;

	private final Vector3i previous = Vector3i.mutable();

	public NaturalTableIterator(Table<T> table) {
		super(table);
	}

	private boolean hasStarted() {
		return started;
	}

	@Override
	public Vector3i offset() {
		return position.toMutable().subtract(previous);
	}

	@Override
	public boolean hasNext() {
		if (!this.hasStarted() && table.size() > 0) {
			return true;
		}
		if (position.y() < table.height() - 1) {
			return true;
		}
		return position.x() < table.width() - 1;
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
		position.addX(1);
		if (position.x() <= table.width() - 1) {
			return;
		}
		position.addY(1);
		position.setX(0);
		if (position.y() >= table.height()) {
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
