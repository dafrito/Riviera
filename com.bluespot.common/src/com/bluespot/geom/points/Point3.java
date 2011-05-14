package com.bluespot.geom.points;

import com.bluespot.geom.Axis;

/**
 * Represents a single point in space, in varying precision. Points may or may
 * not be mutable, but mutability must never change for a given object.
 * <p>
 * Points use a slightly unusual recursive type definition. This lets us have a
 * common interface for points, while still avoiding the performance penalties
 * of boxing.
 * 
 * @author Aaron Faanes
 * @param <P>
 *            the type of point object. This should be recursive.
 * @see AbstractPoint3
 * @see Point3d
 * @see Point3f
 * @see Point3i
 */
public interface Point3<P extends Point3<?>> {

	/**
	 * Set this point's values to the specified point.
	 * 
	 * @param point
	 *            the point that will be copied
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void set(P point);

	/**
	 * Add the specified point's value to this point.
	 * 
	 * @param point
	 *            the point that will be added
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void add(P point);

	/**
	 * Return a mutable point that has the specified point's values added to it.
	 * 
	 * @param point
	 *            the point that will be added
	 * @return a mutable point at this position, but translated by the specified
	 *         point's values
	 */
	public P added(P point);

	/**
	 * Copy another point's value at the specified axis.
	 * 
	 * @param axis
	 *            the axis to copy
	 * @param point
	 *            the point from which to copy
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void set(Axis axis, P point);

	/**
	 * Add another point's value at the specified axis.
	 * 
	 * @param axis
	 *            the axis to copy
	 * @param point
	 *            the point from which to copy
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void add(Axis axis, P point);

	/**
	 * Return a mutable point at this position, but with the specified
	 * translation.
	 * 
	 * @param axis
	 *            the axis to copy
	 * @param point
	 *            the point from which to copy
	 * @return a mutable point translated from this point
	 */
	public P added(Axis axis, P point);

	/**
	 * Clear all values on this point.
	 */
	public void clear();

	/**
	 * Clear values for the specified axis.
	 * 
	 * @param axis
	 *            the axis whose values will be cleared
	 */
	public void clear(Axis axis);

	/**
	 * Returns a mutable point that has zeros for the specified axis.
	 * 
	 * @param axis
	 *            the axis whose values will be cleared
	 * @return a mutable point at this point's position, but with zeros for the
	 *         specified axes
	 */
	public P cleared(Axis axis);

	/**
	 * Return whether this point can be directly modified. This value is a
	 * constant for a given instance.
	 * 
	 * @return {@code true} if this point can be directly modified
	 */
	public boolean isMutable();

	/**
	 * Create and return a new, mutable instance of this point. The returned
	 * point will have the same position. New instances will be created even if
	 * this point is already mutable.
	 * 
	 * @return a new mutable instance of this point
	 */
	public P toMutable();

	/**
	 * Return an immutable instance of this point. If the point is already
	 * immutable, then that point may be returned directly.
	 * 
	 * @return an immutable instance of this point
	 */
	public P toFrozen();

	/**
	 * Return whether this point is at the same location as the specified point.
	 * This behaves similar to {@link #equals(Object)} but ignores the
	 * mutability of the specified point.
	 * 
	 * @param point
	 *            the point that will be compared against
	 * @return {@code true} if this point is at the same location as the
	 *         specified point
	 */
	public boolean at(P point);

	/**
	 * Return a mutable point that lies between this point and the specified
	 * destination. The offset may be any value, but interpolation always occurs
	 * between this point and the specified one: large or negative offset are
	 * handled specially:
	 * <ul>
	 * <li>If {@code offset <= 0}, this point should be returned
	 * <li>If {@code offset >= 1}, {@code destination} should be returned
	 * </ul>
	 * Returning copies instead of always interpolating allows clients to
	 * reliably detect when interpolation is complete.
	 * 
	 * @param dest
	 *            the final point
	 * @param offset
	 *            the percentage of distance traveled.
	 * @return a point that lies between this point and the destination
	 * @see #interpolate(Point3, float)
	 */
	public P interpolated(P dest, float offset);

	/**
	 * Interpolates between this point and the destination. This point will be
	 * modified as a result of this operation. Offsets that are not between zero
	 * and one are handled specially:
	 * <ul>
	 * <li>If {@code offset <= 0}, nothing is modified
	 * <li>If {@code offset >= 1}, this point is set to {@code destination}
	 * </ul>
	 * This special behavior allows clients to reliably detect when
	 * interpolation is complete.
	 * 
	 * @param dest
	 *            the final point
	 * @param offset
	 *            the percentage of distance traveled
	 * @see #interpolated(Point3, float)
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void interpolate(P dest, float offset);

}