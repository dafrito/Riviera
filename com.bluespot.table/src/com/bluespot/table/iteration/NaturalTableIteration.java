package com.bluespot.table.iteration;

import java.awt.Point;

import com.bluespot.table.Table;

/**
 * A left-to-right, then top-to-bottom table ordering.
 * 
 * @author Aaron Faanes
 */
public class NaturalTableIteration extends AbstractTableIteration {

    /**
     * Compare two points. Natural iteration compares Y-axis values first, then uses
     * X-axis values. If both of these are equal, these points are equivalent.
     * <p>
     * Remember that, due to wrapping, an equivalent point does not necessarily mean the
     * point-values are equal. 
     * 
     * @see TableIteration#comparePoints(Table, Point, Point)
     */
    public int comparePoints(Table<?> table, final Point unwrappedA, final Point unwrappedB) {
        final Point a = this.wrap(table, unwrappedA);
        final Point b = this.wrap(table, unwrappedB);
        int yDifference = a.y - b.y;
        if(yDifference != 0)
            return (int)Math.signum(yDifference);
        return (int)Math.signum(a.x - b.x);
    }

    public void next(Point point) {
        point.x++;
    }

    public void previous(Point point) {
        point.x--;
    }

    /**
     * Natural ordering means we wrap on the X-axis. Specifically, excessive
     * X-axis values on either direction will potentially cause changes on our
     * Y-position. On the other hand, excessive Y-axis values never affect our
     * X-axis.
     * <p>
     * This method is essentially the reverse of {@link NaturalTableIteration#doWrap(Table, Point)}
     */
    @Override
    public void doWrap(final Table<?> table, Point point) {
        int excessRows = point.x / table.getWidth();
        point.x %= table.getWidth();
        if(point.x < 0) {
            // If the X-value is negative, we add the width to guarantee a positive value.
            // We also must move one row upwards since subtracting a row always means wrapping.
            excessRows--;
            point.x += table.getWidth();
        }
        point.y += excessRows;
        point.y %= table.getHeight();
        if(point.y < 0) {
            // If the Y-value is negative, we add the height to guarantee a positive value.
            point.y += table.getHeight();
        }
    }

    private static TableIteration instance;

    public static TableIteration getInstance() {
        if(instance == null) {
            instance = new NaturalTableIteration();
        }
        return instance;
    }

}
