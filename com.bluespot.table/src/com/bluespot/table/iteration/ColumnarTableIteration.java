package com.bluespot.table.iteration;

import java.awt.Point;

import com.bluespot.table.Table;

/**
 * A top-to-bottom, then left-to-right table ordering.
 * 
 * @author Aaron Faanes
 */
public class ColumnarTableIteration extends AbstractTableIteration {

    @Override
    public void next(Point targetPoint) {
        targetPoint.y++;
    }

    @Override
    public void previous(Point targetPoint) {
        targetPoint.y--;
    }

    /**
     * Wrap a specified point. Columnar ordering means we wrap on the Y-axis.
     * Specifically, excessive Y-axis values on either direction will
     * potentially cause changes on our X-position. On the other hand, excessive
     * X-axis values never affect our Y-axis.
     * <p>
     * This method is essentially the reverse of
     * {@link NaturalTableIteration#doWrap(Table, Point)}
     */
    @Override
    public void doWrap(final Table<?> table, Point point) {
        int excessColumns = point.y / table.getHeight();
        point.y %= table.getHeight();
        if(point.y < 0) {
            // If the Y-value is negative, we add the height to guarantee a
            // positive value.
            // We also must move one column backwards since subtracting a column
            // always means wrapping.
            excessColumns--;
            point.y += table.getHeight();
        }
        point.x += excessColumns;
        point.x %= table.getWidth();
        if(point.x < 0) {
            // If the X-value is negative, we add the width to guarantee a
            // positive value.
            point.x += table.getWidth();
        }
    }

    /**
     * Compare two points. Columnar iteration compares X-axis values first, then uses
     * Y-axis values. If both of these are equal, these points are equivalent.
     * <p>
     * Remember that, due to wrapping, an equivalent point does not necessarily mean the
     * point-values are equal. 
     * 
     * @see TableIteration#comparePoints(Table, Point, Point)
     */
    @Override
    public int comparePoints(Table<?> table, final Point unwrappedA, final Point unwrappedB) {
        final Point a = this.wrap(table, unwrappedA);
        final Point b = this.wrap(table, unwrappedB);
        int yDifference = a.x - b.x;
        if(yDifference != 0)
            return (int)Math.signum(yDifference);
        return (int)Math.signum(a.y - b.y);
    }

    private static TableIteration instance;

    public static TableIteration getInstance() {
        if(instance == null) {
            instance = new ColumnarTableIteration();
        }
        return instance;
    }

}
