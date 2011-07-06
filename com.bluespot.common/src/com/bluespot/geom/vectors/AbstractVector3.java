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
	public V subtracted(V vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		V result = this.toMutable();
		result.subtract(vector);
		return result;
	}

	@Override
	public V subtracted(Axis axis, V vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		V result = this.toMutable();
		result.subtract(axis, vector);
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
	public V multiplied(double x, double y, double z) {
		V result = this.toMutable();
		result.multiply(x, y, z);
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
	public void invert() {
		this.multiply(-1);
	}

	@Override
	public void invert(Axis axis) {
		this.multiply(axis, -1);
	}

	@Override
	public V inverted() {
		return this.multiplied(-1);
	}

	@Override
	public V inverted(Axis axis) {
		return this.multiplied(axis, -1);
	}

	@Override
	public V normalized() {
		V result = this.toMutable();
		result.normalize();
		return result;
	}

	@Override
	public V interpolated(V dest, float offset) {
		V result = this.toMutable();
		result.interpolate(dest, offset);
		return result;
	}

	@Override
	public V crossed(V other) {
		V result = this.toMutable();
		result.cross(other);
		return result;
	}

	@Override
	public V cleared(Axis axis) {
		V result = this.toMutable();
		result.clear(axis);
		return result;
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
