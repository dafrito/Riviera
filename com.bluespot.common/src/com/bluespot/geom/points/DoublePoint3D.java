/**
 * 
 */
package com.bluespot.geom.points;

/**
 * Represents a single point in space in {@code double} precision. Be aware that
 * while this class implements {@link #equals(Object)} appropriately, it may
 * yield unexpected results due to the inherent imprecision of floating-point
 * values.
 * 
 * @author Aaron Faanes
 * 
 */
public class DoublePoint3D extends AbstractPoint3D<DoublePoint3D> {

	public static DoublePoint3D mutable(double x, final double y, final double z) {
		return new DoublePoint3D(true, x, y, z);
	}

	public static DoublePoint3D frozen(double x, final double y, final double z) {
		return new DoublePoint3D(false, x, y, z);
	}

	public static DoublePoint3D mutable(DoublePoint3D point) {
		return new DoublePoint3D(true, point.x, point.y, point.z);
	}

	public static DoublePoint3D frozen(DoublePoint3D point) {
		return new DoublePoint3D(false, point.x, point.y, point.z);
	}

	public static DoublePoint3D interpolated(DoublePoint3D src, DoublePoint3D dest, float offset) {
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

	private double z;
	private double y;
	private double x;

	/**
	 * Constructs a point using the specified coordinates. There are no
	 * restrictions on the values of these points except that none of them can
	 * be {@code NaN}.
	 * 
	 * @param mutable
	 *            whether this point is mutable
	 * @param x
	 *            the x-coordinate of this point
	 * @param y
	 *            the y-coordinate of this point
	 * @param z
	 *            the z-coordinate of this point
	 * @throws IllegalArgumentException
	 *             if any coordinate is {@code NaN}
	 */
	private DoublePoint3D(final boolean mutable, final double x, final double y, final double z) {
		super(mutable);
		if (java.lang.Double.isNaN(x)) {
			throw new IllegalArgumentException("x is NaN");
		}
		if (java.lang.Double.isNaN(y)) {
			throw new IllegalArgumentException("y is NaN");
		}
		if (java.lang.Double.isNaN(z)) {
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
	public double getX() {
		return this.x;
	}

	/**
	 * Sets the x position to the specified value.
	 * 
	 * @param x
	 *            the new x value
	 * @return the old x value
	 */
	public double setX(double x) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		double old = this.x;
		this.x = x;
		return old;
	}

	/**
	 * Add the specified x value to this point.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old x value
	 */
	public double addX(double offset) {
		return this.setX(this.getX() + offset);
	}

	/**
	 * Return a mutable point that has the same position as this one, except for
	 * the specified translation.
	 * 
	 * @param offset
	 *            the value to add
	 * @return a point at {@code (x + offset, y, z)}
	 */
	public DoublePoint3D addedX(double offset) {
		DoublePoint3D point = this.toMutable();
		point.addX(offset);
		return point;
	}

	/**
	 * Returns the y-coordinate of this point.
	 * 
	 * @return the y-coordinate of this point
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Sets the y position to the specified value.
	 * 
	 * @param y
	 *            the new y value
	 * @return the old y value
	 */
	public double setY(double y) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		double old = this.y;
		this.y = y;
		return old;
	}

	/**
	 * Add the specified y value to this point.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old y value
	 */
	public double addY(double offset) {
		return this.setY(this.getY() + offset);
	}

	/**
	 * Return a mutable point that has the same position as this one, except for
	 * the specified translation.
	 * 
	 * @param offset
	 *            the value to add
	 * @return a point at {@code (x, y + offset, z)}
	 */
	public DoublePoint3D addedY(double offset) {
		DoublePoint3D point = this.toMutable();
		point.addY(offset);
		return point;
	}

	/**
	 * Returns the z-coordinate of this point.
	 * 
	 * @return the z-coordinate of this point
	 */
	public double getZ() {
		return this.z;
	}

	/**
	 * Sets the z position to the specified value.
	 * 
	 * @param z
	 *            the new z value
	 * @return the old z value
	 */
	public double setZ(double z) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		double old = this.z;
		this.z = z;
		return old;
	}

	/**
	 * Add the specified z value to this point.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old z value
	 */
	public double addZ(double offset) {
		return this.setZ(this.getZ() + offset);
	}

	/**
	 * Return a mutable point that has the same position as this one, except for
	 * the specified translation.
	 * 
	 * @param offset
	 *            the value to add
	 * @return a point at {@code (x, y, z + offset)}
	 */
	public DoublePoint3D addedZ(double offset) {
		DoublePoint3D point = this.toMutable();
		point.addZ(offset);
		return point;
	}

	@Override
	public void set(DoublePoint3D point) {
		this.setX(point.getX());
		this.setY(point.getY());
		this.setZ(point.getZ());
	}

	@Override
	public void set(Point3D.Axis axis, DoublePoint3D point) {
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

	@Override
	public DoublePoint3D toMutable() {
		return DoublePoint3D.mutable(x, y, z);
	}

	@Override
	public DoublePoint3D toFrozen() {
		if (!this.isMutable()) {
			return this;
		}
		return DoublePoint3D.frozen(x, y, z);
	}

	@Override
	public boolean at(DoublePoint3D point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return this.getX() == point.getX() &&
				this.getY() == point.getY() &&
				this.getZ() == point.getZ();
	}

	@Override
	public DoublePoint3D toInterpolated(DoublePoint3D dest, float offset) {
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
	public void interpolate(DoublePoint3D dest, float offset) {
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
		if (!(obj instanceof DoublePoint3D)) {
			return false;
		}
		final DoublePoint3D other = (DoublePoint3D) obj;
		if (this.isMutable() != other.isMutable()) {
			return false;
		}
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
		int result = 13;
		result = 31 * result + (this.isMutable() ? 1 : 0);
		final long xLong = java.lang.Double.doubleToLongBits(this.getX());
		final long yLong = java.lang.Double.doubleToLongBits(this.getY());
		final long zLong = java.lang.Double.doubleToLongBits(this.getZ());
		result = 31 * result + (int) (xLong ^ (xLong >>> 32));
		result = 31 * result + (int) (yLong ^ (yLong >>> 32));
		result = 31 * result + (int) (zLong ^ (zLong >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return String.format("Point3D.Double[%s (%f, %f, %f)]", this.isMutable() ? "mutable" : "frozen", this.getX(), this.getY(), this.getZ());
	}

}
