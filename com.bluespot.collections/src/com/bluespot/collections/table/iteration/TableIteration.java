package com.bluespot.collections.table.iteration;

import java.awt.Point;

import com.bluespot.collections.table.Table;
import com.bluespot.collections.table.TableIterator;

/**
 * A strategy for table iteration. Table iteration strategies should be
 * stateless singletons. You should include stateful behavior inside a
 * {@link TableIterator}.
 * 
 * @author Aaron Faanes
 * @see Table
 */
public interface TableIteration {

    /**
     * Compares the specified location, using the iterator's ordering to
     * determine which is logically smaller in magnitude.
     * <p>
     * Magnitude is defined as which location is closer in iterations to the
     * origin of this iterator strategy.
     * <p>
     * This is unrelated with distance in geometric terms. For example, in a
     * natural ordering (left-to-right, then top-to-bottom), the last point on
     * the first row is "smaller" than the first point on the second row, even
     * though the latter point is much closer geometrically to the origin.
     * <p>
     * Also, note that this method only tests in one "direction" of magnitude.
     * For example, a natural ordering would find one point in the "middle" of a
     * table much closer to the origin than the last possible point, even though
     * the last point is only one step away from the first.
     * 
     * @param table
     *            the table that is used to compare these points
     * @param a
     *            the point to compare. It does not need to be wrapped. It is
     *            unaffected by this method.
     * @param b
     *            the point to compare. It does not need to be wrapped. It is
     *            unaffected by this method.
     * @return <ul>
     *         <li>{@code -1} if {@code a} is smaller in magnitude than {@code
     *         b}. <li>{@code 0} if {@code a} is equivalent in magnitude to
     *         {@code b}. Since this method only compares in one direction, this
     *         means they refer to the same location in the table. They may not
     *         necessarily be equivalent points though, due to wrapping.<li>
     *         {@code 1} if {@code a} is larger in magnitude to {@code b}.
     *         </ul>
     */
    public int comparePoints(Table<?> table, Point a, Point b);

    /**
     * Performs whatever is necessary to retrieve the next location from this
     * iterator. The modified point will not be wrapped.
     * 
     * @param currentLocation
     *            the reference location. This point will be modified to refer
     *            to the next location.
     */
    public void next(Point currentLocation);

    /**
     * Performs whatever is necessary to retrieve the previous location from
     * this iterator. The modified point will not be wrapped.
     * 
     * @param currentLocation
     *            the reference location. This point will be modified to refer
     *            to the previous location.
     */
    public void previous(Point currentLocation);

    /**
     * Wraps the specified {@code unwrappedPoint} so that it is a location
     * contained in the specified table. This method creates a new point;
     * {@code unwrappedPoint} is unaffected.
     * 
     * @param table
     *            the table that is used to wrap {@code unwrappedPoint}
     * @param unwrappedPoint
     *            the point to wrap. This point is unaffected by this method.
     * @return a new, wrapped point
     */
    public Point wrap(Table<?> table, Point unwrappedPoint);

    /**
     * Wraps the specified {@code unwrappedPoint}, so that it is a location
     * contained in the specified table. This method replaces the given {@code
     * targetPoint}'s values with the wrapped values.
     * 
     * @param table
     *            the table that is used to wrap {@code unwrappedPoint}
     * @param unwrappedPoint
     *            the point to wrap. This point is unaffected by this method.
     * @param targetPoint
     *            the point that is replaced by the wrapped point's values. The
     *            original values of this point are destroyed.
     */
    public void wrap(Table<?> table, Point unwrappedPoint, Point targetPoint);
}
