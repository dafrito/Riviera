/**
 * 
 */
package com.bluespot.geom.algebra;

/**
 * An object that supports basic algebraic operations.
 * 
 * @author Aaron Faanes
 * @param <V>
 *            the type of this value. It must be recursive.
 * @see Vector3
 */
public interface Algebraic<V extends Algebraic<V>> {

	/**
	 * Add the specified value to this value.
	 * 
	 * @param value
	 *            the value that will be added. It must not be modified by this
	 *            operation
	 * @return {@code this}
	 */
	public V add(V value);

	/**
	 * Subtract the specified value from this value.
	 * 
	 * @param value
	 *            the value that will be subtracted. It must not be modified by
	 *            this operation.
	 * @return {@code this}
	 */
	public V subtract(V value);

	/**
	 * Multiply this value by the specified multiple.
	 * 
	 * @param multiple
	 *            the multiple that will be multipled. It must not be modified
	 *            by this operation.
	 * @return {@code this}
	 */
	public V multiply(V multiple);

	/**
	 * Divide this value by the specified denominator.
	 * 
	 * @param denominator
	 *            the denominator that will be applied. It must not be modified
	 *            by this operation.
	 * @return {@code this}
	 */
	public V divide(V denominator);

	/**
	 * Return a copy of this value.
	 * 
	 * @return a copy of this value
	 */
	public V copy();

}
