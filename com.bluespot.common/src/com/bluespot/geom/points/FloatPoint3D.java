/**
 * 
 */
package com.bluespot.geom.points;

import com.bluespot.geom.Vector;

/**
 * Represents a single point in space in {@code float} precision. Be aware that
 * while this class implements {@link #equals(Object)} appropriately, it may
 * yield unexpected results due to the inherent impreciseness of floating-point
 * values.
 * 
 * @author Aaron Faanes
 * 
 */
public final class FloatPoint3D extends AbstractPoint3D<FloatPoint3D> {

	public static FloatPoint3D mutable(final float x, final float y, final float z) {
		return new FloatPoint3D(true, x, y, z);
	}

	public static FloatPoint3D frozen(final float x, final float y, final float z) {
		return new FloatPoint3D(false, x, y, z);
	}

	public static FloatPoint3D mutable(FloatPoint3D point) {
		return new FloatPoint3D(true, point.x, point.y, point.z);
	}

	public static FloatPoint3D frozen(FloatPoint3D point) {
		return new FloatPoint3D(false, point.x, point.y, point.z);
	}

	/**
	 * Interpolates between this point and the destination. Offsets that are not
	 * between zero and one are handled specially:
	 * <ul>
	 * <li>If {@code offset <= 0}, a copy of {@code src} is returned
	 * <li>If {@code offset >= 1}, a copy of {@code dest} is returned
	 * </ul>
	 * This special behavior allows clients to reliably detect when
	 * interpolation is complete.
	 * 
	 * @param src
	 *            the starting point
	 * @param dest
	 *            the ending point
	 * @param offset
	 *            the percentage of distance between the specified points
	 * @return a mutable point that lies between src and dest
	 */
	public static FloatPoint3D interpolated(FloatPoint3D src, FloatPoint3D dest, final float offset) {
		if (src == null) {
			throw new NullPointerException("src must not be null");
		}
		if (dest == null) {
			throw new NullPointerException("dest must not be null");
		}
		if (offset <= 0f) {
			return src.toMutable();
		}
		if (offset >= 1f) {
			return dest.toMutable();
		}
		return mutable(src.x + (dest.x - src.x) * offset,
				src.y + (dest.y - src.y) * offset,
				src.z + (dest.z - src.z) * offset);
	}

	/**
	 * Represents a point at {@code (0, 0, 0)}.
	 */
	public static final FloatPoint3D ORIGIN = new FloatPoint3D(false, 0, 0, 0);

	private float z;
	private float y;
	private float x;

	/**
	 * Constructs a point using the specified coordinates. There are no
	 * restrictions on the values of these points except that none of them can
	 * be {@code NaN}.
	 * 
	 * @param mutable
	 *            whether this point can be directly modified
	 * @param x
	 *            the x-coordinate of this point
	 * @param y
	 *            the y-coordinate of this point
	 * @param z
	 *            the z-coordinate of this point
	 * @throws IllegalArgumentException
	 *             if any coordinate is {@code NaN}
	 */
	private FloatPoint3D(final boolean mutable, final float x, final float y, final float z) {
		super(mutable);
		if (java.lang.Float.isNaN(x)) {
			throw new IllegalArgumentException("x is NaN");
		}
		if (java.lang.Float.isNaN(y)) {
			throw new IllegalArgumentException("y is NaN");
		}
		if (java.lang.Float.isNaN(z)) {
			throw new IllegalArgumentException("z is NaN");
		}
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the x-coordinate of this point.
	 * 
	 * @return the x-coordinate of this point
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Sets the x position to the specified value.
	 * 
	 * @param x
	 *            the new x value
	 * @return the old x value
	 */
	public float setX(float x) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		float old = this.x;
		this.x = x;
		return old;
	}

	/**
	 * Returns the y-coordinate of this point.
	 * 
	 * @return the y-coordinate of this point
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * Sets the y position to the specified value.
	 * 
	 * @param y
	 *            the new y value
	 * @return the old y value
	 */
	public float setY(float y) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		float old = this.y;
		this.y = y;
		return old;
	}

	/**
	 * Returns the z-coordinate of this point.
	 * 
	 * @return the z-coordinate of this point
	 */
	public float getZ() {
		return this.z;
	}

	/**
	 * Sets the z position to the specified value.
	 * 
	 * @param z
	 *            the new z value
	 * @return the old z value
	 */
	public float setZ(float z) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		float old = this.z;
		this.z = z;
		return old;
	}

	@Override
	public void set(FloatPoint3D point) {
		this.setX(point.getX());
		this.setY(point.getY());
		this.setZ(point.getZ());
	}

	@Override
	public void set(com.bluespot.geom.points.Point3D.Axis axis, FloatPoint3D point) {
		switch (axis) {
		case X:
			this.setX(point.getX());
			break;

		case Y:
			this.setY(point.getY());
			break;

		case Z:
			this.setZ(point.getZ());
			break;
		}
	}

	/**
	 * Creates and returns a new {@link FloatPoint3D} that is this point
	 * translated by the specified {@link Vector}.
	 * 
	 * @param vector
	 *            the vector used to create the new point
	 * @return a new point that is this point translated by the specified vector
	 */
	public FloatPoint3D add(final Vector vector) {
		return new FloatPoint3D(false, this.getX() + (float) vector.getX(), this.getY() + (float) vector.getY(),
				this.getZ() + (float) vector.getZ());
	}

	@Override
	public FloatPoint3D toMutable() {
		return FloatPoint3D.mutable(x, y, z);
	}

	@Override
	public FloatPoint3D toFrozen() {
		if (!this.isMutable()) {
			return this;
		}
		return FloatPoint3D.frozen(x, y, z);
	}

	@Override
	public boolean at(FloatPoint3D point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return this.getX() == point.getX() &&
				this.getY() == point.getY() &&
				this.getZ() == point.getZ();
	}

	@Override
	public FloatPoint3D toInterpolated(FloatPoint3D dest, float offset) {
		if (dest == null) {
			throw new NullPointerException("dest must not be null");
		}
		if (offset >= 1f) {
			return dest.toMutable();
		}
		if (offset <= 0f) {
			return this.toMutable();
		}
		return mutable(this.x + (dest.x - this.x) * offset,
				this.y + (dest.y - this.y) * offset,
				this.z + (dest.z - this.z) * offset);
	}

	@Override
	public void interpolate(FloatPoint3D dest, float offset) {
		if (dest == null) {
			throw new NullPointerException("dest must not be null");
		}
		if (offset >= 1f) {
			this.set(dest);
		} else if (offset >= 0f) {
			this.x += (dest.x - this.x) * offset;
			this.y += (dest.y - this.y) * offset;
			this.z += (dest.z - this.z) * offset;
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FloatPoint3D)) {
			return false;
		}
		final FloatPoint3D other = (FloatPoint3D) obj;
		if (this.getX() != other.getX()) {
			return false;
		}
		if (this.getY() != other.getY()) {
			return false;
		}
		if (this.getZ() != other.getZ()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 11;
		result = 31 * result + java.lang.Float.floatToIntBits(this.getX());
		result = 31 * result + java.lang.Float.floatToIntBits(this.getY());
		result = 31 * result + java.lang.Float.floatToIntBits(this.getZ());
		return result;
	}

	@Override
	public String toString() {
		return String.format("Point3D.Float[%f, %f, %f]", this.getX(), this.getY(), this.getZ());
	}

}
