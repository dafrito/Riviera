package com.bluespot.collections.table.iteration;

import java.awt.Point;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.vectors.Vector3i;

/**
 * A top-to-bottom, then left-to-right table ordering.
 * 
 * @author Aaron Faanes
 */
public class ColumnarTableIteration extends AbstractTableIteration {

	private ColumnarTableIteration() {
		// Do nothing
	}

	/**
	 * Compare two points. Columnar iteration compares X-axis values first, then
	 * uses Y-axis values. If both of these are equal, these points are
	 * equivalent.
	 * <p>
	 * Remember that, due to wrapping, an equivalent point does not necessarily
	 * mean the point-values are equal.
	 * 
	 * @see TableIteration#comparePoints(Table, Point, Point)
	 */
	@Override
	public int comparePoints(final Table<?> table, final Vector3i unwrappedA, final Vector3i unwrappedB) {
		final Vector3i a = this.wrap(table, unwrappedA);
		final Vector3i b = this.wrap(table, unwrappedB);
		final int yDifference = a.x() - b.x();
		if (yDifference != 0) {
			return (int) Math.signum(yDifference);
		}
		return (int) Math.signum(a.y() - b.y());
	}

	/**
	 * Wrap a specified point. Columnar ordering means we wrap on the Y-axis.
	 * Specifically, excessive Y-axis values on either direction will
	 * potentially cause changes on our X-position. On the other hand, excessive
	 * X-axis values never affect our Y-axis.
	 * <p>
	 * This method is essentially the reverse of
	 * {@link NaturalTableIteration#doWrap(Table, Vector3i)}
	 */
	@Override
	public void doWrap(final Table<?> table, final Vector3i point) {
		int excessColumns = point.y() / table.height();
		int x = point.x();
		int y = point.y();
		y %= table.height();
		if (y < 0) {
			// If the Y-value is negative, we add the height to guarantee a
			// positive value.
			// We also must move one column backwards since subtracting a column
			// always means wrapping.
			excessColumns--;
			y += table.height();
		}
		x += excessColumns;
		x %= table.width();
		if (x < 0) {
			// If the X-value is negative, we add the width to guarantee a
			// positive value.
			x += table.width();
		}
		point.setX(x);
		point.setY(y);
	}

	@Override
	public void next(final Vector3i targetPoint) {
		targetPoint.addY(1);
	}

	@Override
	public void previous(final Vector3i targetPoint) {
		targetPoint.subtractY(1);
	}

	/**
	 * Returns the single instance of this iteration strategy.
	 * 
	 * @return the single instance of this strategy
	 */
	public static TableIteration getInstance() {
		if (ColumnarTableIteration.instance == null) {
			ColumnarTableIteration.instance = new ColumnarTableIteration();
		}
		return ColumnarTableIteration.instance;
	}

	private static TableIteration instance;

}
