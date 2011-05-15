package com.bluespot.geom.vectors;

import com.bluespot.geom.Axis;

/**
 * Represents a single point in space, in varying precision. Points may or may
 * not be mutable, but mutability must never change for a given object.
 * <p>
 * Points use a slightly unusual recursive type definition. This lets us have a
 * common interface for points, while still avoiding the performance penalties
 * of boxing. Unfortunately, that means there are many methods that are not part
 * of this interface, but are still implied for {@link Point3} implementations.
 * Refer to {@link Point3i}, {@link Point3d}, or {@link Point3f} for reference.
 * <p>
 * Most operations support several different variants:
 * 
 * <pre>
 * // Operate on this object, using all values from the specified point.
 * void add(P value);
 * 
 * // Operate on this object, using the specified value for all axes.
 * void add(primitive value);
 * 
 * // Operate on this object, using the values from the specified point
 * // to modify this point's values at the specified axes. 
 * void add(Axis axis, P value);
 * 
 * // Operate on this object, using the primitive value to modify this
 * // point's values at the specified axes. 
 * void add(Axis axis, primitive value);
 * 
 * // Operate on a copy, using all values from the specified point. 
 * P added(P value)
 *  
 * // Operate on a copy, using the specified value for all axes.
 * P added(primitive value);
 * 
 * // Operate on a copy, using the values from the specified point
 * // to modify the copy's values at the specified axes. 
 * P added(Axis axis, P value);
 * 
 * // Operate on a copy, using the primitive value to modify the
 * // copy's values at the specified axes. 
 * P added(Axis axis, primitive value);
 * </pre>
 * 
 * Some operations may be omitted if they don't make sense. They should also be
 * omitted if some other more common mechanism provides them. For example,
 * {@code cleared()} is already implemented through an {@code origin()} method.
 * <p>
 * The naming convention for operations is to name the creating methods with the
 * -ed suffix (inverted, cleared, etc.), though exceptions exist: setX's
 * immutable variant is withX. Be pragmatic and use what reads well.
 * 
 * @author Aaron Faanes
 * @param <P>
 *            the type of point object. This should be recursive.
 * @see AbstractPoint3
 * @see Point3d
 * @see Point3f
 * @see Point3i
 * @see Points
 */
public interface Point3<P extends Point3<P>> {

	/**
	 * Return whether this point can be directly modified. This value is a
	 * constant for a given instance.
	 * 
	 * @return {@code true} if this point can be directly modified
	 */
	public boolean isMutable();

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
	 * Return a mutable point that uses the specified point's values for the
	 * specified axes.
	 * 
	 * @param axis
	 *            the axes that will be modified
	 * @param point
	 *            the point that will be added
	 * @return a modified, mutable copy of this point
	 */
	public P with(Axis axis, P point);

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
	 * Return a mutable point that has the specified point's values added to it.
	 * 
	 * @param point
	 *            the point that will be added
	 * @return a mutable point at this position, but translated by the specified
	 *         point's values
	 */
	public P added(P point);

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
	 * Multiply this point's values by the specified point's values. This
	 * modified point will become {@code (x*point.x, y*point.y, z*point.z)}
	 * 
	 * @param point
	 *            the point that will be added
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void multiply(P point);

	/**
	 * Multiply this point's values by the specified factor. This modified point
	 * will become {@code (x*factor, y*factor, z*factor)}
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void multiply(double factor);

	/**
	 * Multiply this point's values at the specified axis, using the values from
	 * the specified point.
	 * 
	 * @param axis
	 *            the axis to multiply
	 * @param point
	 *            the point used in multiplication
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void multiply(Axis axis, P point);

	/**
	 * Multiply this point's values at the specified axis by the specified
	 * factor.
	 * 
	 * @param axis
	 *            the axis to multiply
	 * @param factor
	 *            the factor of multiplication
	 * @throws UnsupportedOperationException
	 *             if this point is immutable
	 */
	public void multiply(Axis axis, double factor);

	/**
	 * Return a mutable copy of this point, multiplied by the specified points
	 * values.
	 * 
	 * @param point
	 *            the point that will be added
	 * @return a mutable point at this position, but translated by the specified
	 *         point's values
	 */
	public P multiplied(P point);

	/**
	 * Return a mutable copy of this point, multiplied by the specified factor
	 * for all axes.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable copy of this point, multiplied by the specified factor
	 */
	public P multiplied(double factor);

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
	public P multiplied(Axis axis, P point);

	/**
	 * * Return a mutable copy of this point, multiplied by the specified factor
	 * for the specified axes.
	 * 
	 * @param axis
	 *            the axis that will be multiplied
	 * @param factor
	 *            the factor of multiplication
	 * @return a modified, mutable copy of this point
	 */
	public P multiplied(Axis axis, double factor);

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

}
