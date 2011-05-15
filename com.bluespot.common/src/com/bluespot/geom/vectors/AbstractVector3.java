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
	public V with(Axis axis, V vector) {
		if (axis == null) {
			throw new NullPointerException("axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		V result = this.toMutable();
		result.set(axis, vector);
		return result;
	}

	@Override
	public V added(V vector) {
		V result = this.toMutable();
		result.add(vector);
		return result;
	}

	@Override
	public V added(Axis axis, V vector) {
		if (axis == null) {
			throw new NullPointerException("axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		V result = this.toMutable();
		result.add(axis, vector);
		return result;
	}

	@Override
	public V multiplied(V vector) {
		V result = this.toMutable();
		result.multiply(vector);
		return result;
	}

	@Override
	public V multiplied(double factor) {
		V result = this.toMutable();
		result.multiply(factor);
		return result;
	}

	@Override
	public V multiplied(Axis axis, V vector) {
		V result = this.toMutable();
		result.multiply(axis, vector);
		return result;
	}

	@Override
	public V multiplied(Axis axis, double factor) {
		V result = this.toMutable();
		result.multiply(axis, factor);
		return result;
	}

	@Override
	public V interpolated(V dest, float offset) {
		V result = this.toMutable();
		result.interpolate(dest, offset);
		return result;
	}

	@Override
	public V cleared(Axis axis) {
		V result = this.toMutable();
		result.clear(axis);
		return result;
	}

}
