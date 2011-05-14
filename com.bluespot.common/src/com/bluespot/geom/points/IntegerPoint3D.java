/**
 * 
 */
package com.bluespot.geom.points;

/**
 * Represents a single point in space in {@code int} precision.
 * 
 * @author Aaron Faanes
 * 
 */
public final class IntegerPoint3D extends AbstractPoint3D<IntegerPoint3D> {

	public static IntegerPoint3D mutable(int x, int y, int z) {
		return new IntegerPoint3D(true, x, y, z);
	}

	public static IntegerPoint3D frozen(int x, int y, int z) {
		return new IntegerPoint3D(false, x, y, z);
	}

	public static IntegerPoint3D mutable(IntegerPoint3D point) {
		return new IntegerPoint3D(true, point.x, point.y, point.z);
	}

	public static IntegerPoint3D frozen(IntegerPoint3D point) {
		return new IntegerPoint3D(false, point.x, point.y, point.z);
	}

	public static IntegerPoint3D interpolated(IntegerPoint3D src, IntegerPoint3D dest, float offset) {
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
		return mutable(src.x + (int) ((dest.x - src.x) * offset),
				src.y + (int) ((dest.y - src.y) * offset),
				src.z + (int) ((dest.z - src.z) * offset));
	}

	private int z;
	private int y;
	private int x;

	/**
	 * Constructs a point using the specified coordinates.
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
	private IntegerPoint3D(final boolean mutable, final int x, final int y, final int z) {
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
	public int getX() {
		return this.x;
	}

	/**
	 * Sets the x position to the specified value.
	 * 
	 * @param x
	 *            the new x value
	 * @return the old x value
	 */
	public int setX(int x) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		int old = this.x;
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
	public int addX(int offset) {
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
	public IntegerPoint3D addedX(int offset) {
		IntegerPoint3D point = this.toMutable();
		point.addX(offset);
		return point;
	}

	/**
	 * Returns the y-coordinate of this point.
	 * 
	 * @return the y-coordinate of this point
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Sets the y position to the specified value.
	 * 
	 * @param y
	 *            the new y value
	 * @return the old y value
	 */
	public int setY(int y) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		int old = this.y;
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
	public int addY(int offset) {
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
	public IntegerPoint3D addedY(int offset) {
		IntegerPoint3D point = this.toMutable();
		point.addY(offset);
		return point;
	}

	/**
	 * Returns the z-coordinate of this point.
	 * 
	 * @return the z-coordinate of this point
	 */
	public int getZ() {
		return this.z;
	}

	/**
	 * Sets the z position to the specified value.
	 * 
	 * @param z
	 *            the new z value
	 * @return the old z value
	 */
	public int setZ(int z) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		int old = this.z;
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
	public int addZ(int offset) {
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
	public IntegerPoint3D addedZ(int offset) {
		IntegerPoint3D point = this.toMutable();
		point.addZ(offset);
		return point;
	}

	@Override
	public void set(IntegerPoint3D point) {
		this.setX(point.getX());
		this.setY(point.getY());
		this.setZ(point.getZ());
	}

	@Override
	public void set(com.bluespot.geom.points.Point3D.Axis axis, IntegerPoint3D point) {
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
	public IntegerPoint3D toMutable() {
		return IntegerPoint3D.mutable(x, y, z);
	}

	@Override
	public IntegerPoint3D toFrozen() {
		if (!this.isMutable()) {
			return this;
		}
		return IntegerPoint3D.frozen(x, y, z);
	}

	@Override
	public boolean at(IntegerPoint3D point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return this.getX() == point.getX() &&
				this.getY() == point.getY() &&
				this.getZ() == point.getZ();
	}

	@Override
	public IntegerPoint3D interpolated(IntegerPoint3D dest, float offset) {
		if (dest == null) {
			throw new NullPointerException("dest must not be null");
		}
		if (offset >= 1f) {
			return dest.toMutable();
		}
		if (offset <= 0f) {
			return this.toMutable();
		}
		return mutable(this.x + (int) ((dest.x - this.x) * offset),
				this.y + (int) ((dest.y - this.y) * offset),
				this.z + (int) ((dest.z - this.z) * offset));
	}

	@Override
	public void interpolate(IntegerPoint3D dest, float offset) {
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
		if (!(obj instanceof IntegerPoint3D)) {
			return false;
		}
		final IntegerPoint3D other = (IntegerPoint3D) obj;
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
		int result = 17;
		result = 31 * result + (this.isMutable() ? 1 : 0);
		result = 31 * result + this.getX();
		result = 31 * result + this.getY();
		result = 31 * result + this.getZ();
		return result;
	}

	@Override
	public String toString() {
		return String.format("Point3D.Integer[%s (%d, %d, %d)]", this.isMutable(), this.getX(), this.getY(), this.getZ());
	}

}
