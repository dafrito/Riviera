/**
 * 
 */
package com.bluespot.geom.vectors;

import com.bluespot.geom.Axis;

/**
 * A mathematical vector.
 * 
 * @author Aaron Faanes
 * 
 * @param <V>
 *            the type of this vector
 */
public interface Vector3<V extends Vector3<?>> {

	/**
	 * Returns a new {@link Vector3} that is the inverse of this vector. All
	 * dimensions are multiplied by {@code -1}.
	 * 
	 * @return a new {@code Vector3} that is the inverse of this vector
	 */
	public V inverted();

	/**
	 * Invert this vector.
	 * 
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public void invert();

	public void clear();

	public void clear(Axis axis);

	public V cleared(Axis axis);

	/**
	 * Returns a new {@link Vector3} that is the cross product of this vector
	 * with the specified vector.
	 * 
	 * @param other
	 *            the other vector used in the calculation of the cross product
	 * @return a new {@code Vector3} that represents the cross product of these
	 *         two vectors
	 * @throws NullPointerException
	 *             if {@code other} is null
	 */
	public V crossed(V other);

	/**
	 * Modify this vector by calculating a cross product with the specified
	 * vector.
	 * 
	 * @param other
	 *            the vector used to calculate the cross product
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public void cross(V other);

}