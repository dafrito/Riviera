package com.bluespot.collections.table.iteration;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.vectors.Vector3i;

/**
 * Skeletal implementation of the {@link TableIteration} interface. Currently,
 * this class just simplifies wrapping.
 * 
 * @author Aaron Faanes
 */
public abstract class AbstractTableIteration implements TableIteration {

	/**
	 * Performs whatever is necessary to wrap the specified point.
	 * 
	 * @param table
	 *            the table used to wrap the specified point
	 * @param targetPoint
	 *            the point to wrap. This point is modified in this method.
	 */
	public abstract void doWrap(Table<?> table, Vector3i targetPoint);

	@Override
	public Vector3i wrap(final Table<?> table, final Vector3i unwrappedPoint) {
		final Vector3i targetPoint = Vector3i.mutable();
		this.wrap(table, unwrappedPoint, targetPoint);
		return targetPoint;
	}

	@Override
	public void wrap(final Table<?> table, final Vector3i unwrappedPoint, final Vector3i targetPoint) {
		targetPoint.set(unwrappedPoint);
		this.doWrap(table, targetPoint);
	}

}
