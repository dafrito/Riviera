package com.bluespot.collections.table.iteration;

import java.awt.Point;

import com.bluespot.collections.table.Table;

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
	public abstract void doWrap(Table<?> table, Point targetPoint);

	public Point wrap(final Table<?> table, final Point unwrappedPoint) {
		final Point targetPoint = new Point();
		this.wrap(table, unwrappedPoint, targetPoint);
		return targetPoint;
	}

	public void wrap(final Table<?> table, final Point unwrappedPoint, final Point targetPoint) {
		targetPoint.setLocation(unwrappedPoint);
		this.doWrap(table, targetPoint);
	}

}
