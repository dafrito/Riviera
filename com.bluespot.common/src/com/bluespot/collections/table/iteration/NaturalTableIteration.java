package com.bluespot.collections.table.iteration;

import java.awt.Point;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.vectors.Vector3i;

/**
 * A left-to-right, then top-to-bottom table ordering.
 * 
 * @author Aaron Faanes
 */
public class NaturalTableIteration extends AbstractTableIteration {

	private NaturalTableIteration() {
		// Do nothing
	}

	/**
	 * Compare two points. Natural iteration compares Y-axis values first, then
	 * uses X-axis values. If both of these are equal, these points are
	 * equivalent.
	 * <p>
	 * Remember that, due to wrapping, an equivalent point does not necessarily
	 * mean the point-values are equal.
	 * 
	 * @see TableIteration#comparePoints(Table, Vector3i, Vector3i)
	 */
	@Override
	public int comparePoints(final Table<?> table, final Vector3i unwrappedA, final Vector3i unwrappedB) {
		final Vector3i a = this.wrap(table, unwrappedA);
		final Vector3i b = this.wrap(table, unwrappedB);
		final int yDifference = a.y() - b.y();
		if (yDifference != 0) {
			return (int) Math.signum(yDifference);
		}
		return (int) Math.signum(a.x() - b.x());
	}

	/**
	 * Natural ordering means we wrap on the X-axis. Specifically, excessive
	 * X-axis values on either direction will potentially cause changes on our
	 * Y-position. On the other hand, excessive Y-axis values never affect our
	 * X-axis.
	 * <p>
	 * This method is essentially the reverse of
	 * {@link NaturalTableIteration#doWrap(Table, Point)}
	 */
	@Override
	public void doWrap(final Table<?> table, final Vector3i point) {
		int excessRows = point.x() / table.width();
		int x = point.x();
		int y = point.y();
		x %= table.width();
		if (x < 0) {
			// If the X-value is negative, we add the width to guarantee a
			// positive value.
			// We also must move one row upwards since subtracting a row always
			// means wrapping.
			excessRows--;
			x += table.width();
		}
		y += excessRows;
		y %= table.height();
		if (y < 0) {
			// If the Y-value is negative, we add the height to guarantee a
			// positive value.
			y += table.height();
		}
		point.set(x, y, 0);
	}

	@Override
	public void next(final Vector3i point) {
		point.addX(1);
	}

	@Override
	public void previous(final Vector3i point) {
		point.subtractX(1);
	}

	/**
	 * Returns the single instance of this iteration strategy.
	 * 
	 * @return the single instance of this strategy
	 */
	public static TableIteration getInstance() {
		if (NaturalTableIteration.instance == null) {
			NaturalTableIteration.instance = new NaturalTableIteration();
		}
		return NaturalTableIteration.instance;
	}

	private static TableIteration instance;

}
