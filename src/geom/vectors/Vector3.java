package geom.vectors;

import java.awt.Dimension;
import java.awt.Point;

import geom.Axis;
import geom.algebra.Algebraic;

/**
 * Represents a three-dimensional vector, of varying precision. Vectors may or
 * may not be mutable, but mutability must never change for a given vector.
 * Vectors may represent a position, a velocity, or even a dimension. Initially,
 * these quantities were represented by independent classes, but they all simply
 * referred to this hierarchy internally, so I removed the indirection.
 * <p>
 * This vector interface uses a slightly unusual recursive type definition. This
 * lets us have a common interface for vectors, while still avoiding the
 * performance penalties of boxing. Unfortunately, that means there are many
 * methods that are not part of this interface, but are still implied for
 * {@link Vector3} implementations. Refer to {@link Vector3i}, {@link Vector3d},
 * or {@link Vector3f} for further reference.
 * <p>
 * Most operations support several different variants:
 * 
 * <pre>
 * // Operate on this object, using all values from the specified vector.
 * void add(V value);
 * 
 * // Operate on this object, using the specified value for all axes.
 * void add(primitive value);
 * 
 * // Operate on this object, using the values from the specified vector
 * // to modify this object's values at the specified axes. 
 * void add(Axis axis, V value);
 * 
 * // Operate on this object, using the primitive value to modify this
 * // vector's values at the specified axes. 
 * void add(Axis axis, primitive value);
 * </pre>
 * 
 * @author Aaron Faanes
 * @param <V>
 *            the type of vector object. This should be recursive.
 * @see AbstractVector3
 * @see Vector3d
 * @see Vector3f
 * @see Vector3i
 * @see Vectors
 */
public interface Vector3<V extends Vector3<V>> extends Algebraic<V> {

	/**
	 * Return whether this vector can be directly modified. This value is a
	 * constant for a given instance.
	 * 
	 * @return {@code true} if this vector can be directly modified
	 */
	public boolean isMutable();

	/**
	 * Copy the specified vector to this vector.
	 * 
	 * @param vector
	 *            the vector that will be copied
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V set(V vector);

	/**
	 * Copy another vector's value at the specified axis.
	 * 
	 * @param axis
	 *            the axis to copy
	 * @param vector
	 *            the vector from which to copy
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V set(Axis axis, V vector);

	/**
	 * Add the specified vector's value to this vector.
	 * 
	 * @param vector
	 *            the vector that will be added
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	@Override
	public V add(V vector);

	/**
	 * Add another vector's value at the specified axis.
	 * 
	 * @param axis
	 *            the axis to copy
	 * @param vector
	 *            the vector from which to copy
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V add(Axis axis, V vector);

	/**
	 * Subtract the specified vector from this vector.
	 * 
	 * @param vector
	 *            the vector that will be used in this operation. It will not be
	 *            modified.
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is not mutable
	 */
	@Override
	public V subtract(V vector);

	/**
	 * Subtract the specified vector from this vector at the specified axes.
	 * 
	 * @param axis
	 *            the axes that will be modified by this operation
	 * @param vector
	 *            the vector that will be used in this operation. It will not be
	 *            modified.
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is not mutable
	 */
	public V subtract(Axis axis, V vector);

	/**
	 * Multiply this vector's values by the specified vector's values. This
	 * modified vector will become {@code (x*vector.x, y*vector.y, z*vector.z)}
	 * 
	 * @param vector
	 *            the vector that will be added
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	@Override
	public V multiply(V vector);

	/**
	 * Multiply this vector's values by the specified factor. This modified
	 * vector will become {@code (x*factor, y*factor, z*factor)}
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V multiply(double factor);

	/**
	 * Multiply this vector's values by the specified factors.
	 * 
	 * @param x
	 *            the multiplying factor of the X axis
	 * @param y
	 *            the multiplying factor of the Y axis
	 * @param z
	 *            the multiplying factor of the Z axis
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V multiply(double x, double y, double z);

	/**
	 * Multiply this vector's values at the specified axis, using the values
	 * from the specified vector.
	 * 
	 * @param axis
	 *            the axis to multiply
	 * @param vector
	 *            the vector used in multiplication
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V multiply(Axis axis, V vector);

	/**
	 * Multiply this vector's values at the specified axis by the specified
	 * factor.
	 * 
	 * @param axis
	 *            the axis to multiply
	 * @param factor
	 *            the factor of multiplication
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V multiply(Axis axis, double factor);

	/**
	 * Divide this vector's values by the specified vector's values. This
	 * modified vector will become {@code (x/vector.x, y/vector.y, z/vector.z)}
	 * 
	 * @param vector
	 *            the vector that will be divided
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	@Override
	public V divide(V vector);

	/**
	 * Divide this vector's values by the specified denominator. This modified
	 * vector will become {@code (x/denominator, y/denominator, z/denominator)}
	 * 
	 * @param denominator
	 *            the denominator for all values
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V divide(double denominator);

	/**
	 * Divide this vector's values by the specified denominator.
	 * 
	 * @param x
	 *            the denominator of the X axis
	 * @param y
	 *            the denominator of the Y axis
	 * @param z
	 *            the denominator of the Z axis
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V divide(double x, double y, double z);

	/**
	 * Divide this vector's values at the specified axis, using the values from
	 * the specified vector.
	 * 
	 * @param axis
	 *            the axis to divide
	 * @param vector
	 *            the vector used in this operation. It is not modified.
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V divide(Axis axis, V vector);

	/**
	 * Divide this vector's values at the specified axis by the specified
	 * factor.
	 * 
	 * @param axis
	 *            the axis to divide
	 * @param denominator
	 *            the denominator for the specified axis
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V divide(Axis axis, double denominator);

	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector
	 */
	public double length();

	/**
	 * Invert this vector. All components are multiplied by {@code -1}.
	 * 
	 * @return {@code this}
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public V negate();

	/**
	 * Invert the specified components of this vector.
	 * 
	 * @param axis
	 *            the axis to invert
	 * @return {@code this}
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public V negate(Axis axis);

	/**
	 * Convert this vector to its multiplicative inverse, such that it will now
	 * be <code>(1/x, 1/y, 1/z)</code>.
	 * 
	 * @return {@code this}
	 */
	public V reciprocal();

	/**
	 * Convert the specified fields to their multiplicative inverses.
	 * 
	 * @param axis
	 *            the axis to convert
	 * @return {@code this}
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public V reciprocal(Axis axis);

	/**
	 * Normalizes this vector, such that its new length will be one.
	 * 
	 * @return {@code this}
	 */
	public V normalize();

	/**
	 * Interpolates between this vector and the destination. This vector will be
	 * modified as a result of this operation. Offsets that are not between zero
	 * and one are handled specially:
	 * <ul>
	 * <li>If {@code offset <= 0}, nothing is modified
	 * <li>If {@code offset >= 1}, this vector is set to {@code destination}
	 * </ul>
	 * This special behavior allows clients to reliably detect when
	 * interpolation is complete.
	 * 
	 * @param dest
	 *            the final vector
	 * @param offset
	 *            the percentage of distance traveled
	 * @return {@code this}
	 * @throws UnsupportedOperationException
	 *             if this vector is immutable
	 */
	public V interpolate(V dest, float offset);

	/**
	 * Modify this vector by calculating a cross product with the specified
	 * vector.
	 * 
	 * @param other
	 *            the vector used to calculate the cross product
	 * @return {@code this}
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public V cross(V other);

	/**
	 * Clear all values on this vector.
	 * 
	 * @return {@code this}
	 */
	public V clear();

	/**
	 * Clear values for the specified axis.
	 * 
	 * @param axis
	 *            the axis whose values will be cleared
	 * @return {@code this}
	 */
	public V clear(Axis axis);

	/**
	 * Return a new {@link Dimension} initialized with this vector's values.
	 * 
	 * @return a new {@link Dimension}
	 */
	public Dimension toDimension();

	/**
	 * Return a new {@link Point} initialized with this vector's values.
	 * 
	 * @return a new {@link Point}
	 */
	public Point toPoint();

	/**
	 * Create and return a new, mutable instance of this vector. The returned
	 * vector will have the same position. New instances will be created even if
	 * this vector is already mutable.
	 * 
	 * @return a new mutable instance of this vector
	 * @see #copy()
	 * @see #toFrozen()
	 */
	public V toMutable();

	/**
	 * Return an immutable instance of this vector. If the vector is already
	 * immutable, then that vector may be returned directly.
	 * 
	 * @return an immutable instance of this vector
	 * @see #copy()
	 * @see #toMutable()
	 */
	public V toFrozen();

	/**
	 * Returns a copy of this vector. The returned vector will be mutable if and
	 * only if this vector is mutable; immutable vectors may be returned without
	 * copying.
	 * 
	 * @return a copy of this vector
	 * @see #toMutable()
	 * @see #toFrozen()
	 */
	@Override
	public V copy();

	/**
	 * Return whether this vector is at the same location as the specified
	 * vector. This behaves similar to {@link #equals(Object)} but ignores the
	 * mutability of the specified vector.
	 * 
	 * @param vector
	 *            the vector that will be compared against
	 * @return {@code true} if this vector is at the same location as the
	 *         specified vector
	 */
	public boolean at(V vector);

}
