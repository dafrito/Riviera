/**
 * 
 */
package com.bluespot.geom.vectors;

import com.bluespot.geom.Axis;

/**
 * A basic {@link Vector3} implementation.
 * 
 * @author Aaron Faanes
 * 
 * @param <V>
 *            the type of this vector
 */
public abstract class AbstractVector3<V extends Vector3<V>> implements Vector3<V> {

	private final boolean mutable;

	protected AbstractVector3(boolean mutable) {
		this.mutable = mutable;
	}

	@Override
	public boolean isMutable() {
		return this.mutable;
	}

	@Override
	public V divide(double denominator) {
		this.multiply(1 / denominator);
		return getThis();
	}

	@Override
	public V divide(Axis axis, V vector) {
		V reciprocal = vector.toMutable();
		reciprocal.reciprocal();
		this.multiply(axis, reciprocal);
		return getThis();
	}

	@Override
	public V divide(Axis axis, double denominator) {
		this.multiply(axis, 1 / denominator);
		return getThis();
	}

	@Override
	public V negate() {
		this.multiply(-1);
		return getThis();
	}

	@Override
	public V negate(Axis axis) {
		this.multiply(axis, -1);
		return getThis();
	}

	@Override
	public V copy() {
		if (this.isMutable()) {
			return this.toMutable();
		} else {
			return this.getThis();
		}
	}

	/**
	 * Return a reference to this vector.
	 * 
	 * @return a reference to this vector
	 * @see #copy()
	 */
	protected abstract V getThis();
}
