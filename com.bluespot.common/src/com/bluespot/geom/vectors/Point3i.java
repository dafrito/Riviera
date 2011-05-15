/**
 * 
 */
package com.bluespot.geom.vectors;

import com.bluespot.geom.Axis;

/**
 * A {@link Point3} in {@code int} precision.
 * 
 * @author Aaron Faanes
 * 
 * @see Point3f
 * @see Point3d
 */
public final class Point3i extends AbstractPoint3<Point3i> {

	private static final Point3i ORIGIN = new Point3i(false, 0, 0, 0);

	/**
	 * Returns a frozen point at the origin.
	 * 
	 * @return a frozen point at the origin.
	 */
	public static Point3i origin() {
		return ORIGIN;
	}

	public static Point3i mutable(int x, int y, int z) {
		return new Point3i(true, x, y, z);
	}

	public static Point3i frozen(int x, int y, int z) {
		return new Point3i(false, x, y, z);
	}

	public static Point3i mutable(Point3f point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return new Point3i(true, (int) point.getX(), (int) point.getY(), (int) point.getZ());
	}

	public static Point3i frozen(Point3f point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return new Point3i(false, (int) point.getX(), (int) point.getY(), (int) point.getZ());
	}

	public static Point3i mutable(Point3d point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return new Point3i(true, (int) point.getX(), (int) point.getY(), (int) point.getZ());
	}

	public static Point3i frozen(Point3d point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return new Point3i(false, (int) point.getX(), (int) point.getY(), (int) point.getZ());
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
	public static Point3i interpolated(Point3i src, Point3i dest, float offset) {
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
	private Point3i(final boolean mutable, final int x, final int y, final int z) {
		super(mutable);
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
	 * @param value
	 *            the new x value
	 * @return the old x value
	 */
	public int setX(int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		int old = this.x;
		this.x = value;
		return old;
	}

	/**
	 * Returns a translated mutable point. The returned point will be at the
	 * same position as this one, but with the x value set to the specified
	 * value.
	 * 
	 * @param value
	 *            the new x value
	 * @return a mutable point that uses the specified value for its x axis
	 */
	public Point3i withX(int value) {
		Point3i result = this.toMutable();
		result.setX(value);
		return result;
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
	public Point3i addedX(int offset) {
		Point3i point = this.toMutable();
		point.addX(offset);
		return point;
	}

	/**
	 * Multiply the specified x value of this point.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old x value
	 */
	public double multiplyX(double factor) {
		return this.setX((int) Math.round(this.getX() * factor));
	}

	/**
	 * Return a mutable copy of this point, with a multiplied x value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable point at {@code (x * offset, y, z)}
	 */
	public Point3i mulipliedX(double factor) {
		Point3i point = this.toMutable();
		point.multiplyX(factor);
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
	 * @param value
	 *            the new y value
	 * @return the old y value
	 */
	public int setY(int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		int old = this.y;
		this.y = value;
		return old;
	}

	/**
	 * Returns a translated mutable point. The returned point will be at the
	 * same position as this one, but with the y value set to the specified
	 * value.
	 * 
	 * @param value
	 *            the new y value
	 * @return a mutable point that uses the specified value for its y axis
	 */
	public Point3i withY(int value) {
		Point3i result = this.toMutable();
		result.setY(value);
		return result;
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
	public Point3i addedY(int offset) {
		Point3i point = this.toMutable();
		point.addY(offset);
		return point;
	}

	/**
	 * Multiply the specified y value of this point.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old y value
	 */
	public double multiplyY(double factor) {
		return this.setY((int) Math.round(this.getY() * factor));
	}

	/**
	 * Return a mutable copy of this point, with a multiplied y value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable point at {@code (x, y * offset, z)}
	 */
	public Point3i mulipliedY(double factor) {
		Point3i point = this.toMutable();
		point.multiplyY(factor);
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
	 * @param value
	 *            the new z value
	 * @return the old z value
	 */
	public int setZ(int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		int old = this.z;
		this.z = value;
		return old;
	}

	/**
	 * Returns a translated mutable point. The returned point will be at the
	 * same position as this one, but with the z value set to the specified
	 * value.
	 * 
	 * @param value
	 *            the new z value
	 * @return a mutable point that uses the specified value for its z axis
	 */
	public Point3i withZ(int value) {
		Point3i result = this.toMutable();
		result.setZ(value);
		return result;
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
	public Point3i addedZ(int offset) {
		Point3i point = this.toMutable();
		point.addZ(offset);
		return point;
	}

	/**
	 * Multiply the specified z value of this point.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old z value
	 */
	public double multiplyZ(double factor) {
		return this.setZ((int) Math.round(this.getZ() * factor));
	}

	/**
	 * Return a mutable copy of this point, with a multiplied z value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable point at {@code (x, y, z * offset)}
	 */
	public Point3i mulipliedZ(double factor) {
		Point3i point = this.toMutable();
		point.multiplyZ(factor);
		return point;
	}

	@Override
	public void set(Point3i point) {
		this.setX(point.getX());
		this.setY(point.getY());
		this.setZ(point.getZ());
	}

	/**
	 * Sets all of this point's values to the specified value.
	 * 
	 * @param value
	 *            the value that will be used
	 */
	public void set(int value) {
		this.setX(value);
		this.setY(value);
		this.setZ(value);
	}

	/**
	 * Sets all of this point's values to the specified values.
	 * 
	 * @param x
	 *            the new x value
	 * @param y
	 *            the new y value
	 * @param z
	 *            the new z value
	 */
	public void set(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	@Override
	public void set(Axis axis, Point3i point) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (point == null) {
			throw new NullPointerException("Point must not be null");
		}
		switch (axis) {
		case X:
			this.setX(point.getX());
			return;
		case Y:
			this.setY(point.getY());
			return;
		case Z:
			this.setZ(point.getZ());
			return;
		case XY:
			this.setX(point.getX());
			this.setY(point.getY());
			return;
		case XZ:
			this.setX(point.getX());
			this.setZ(point.getZ());
			return;
		case YZ:
			this.setY(point.getY());
			this.setZ(point.getZ());
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	/**
	 * Sets values at the specified axes to the specified value.
	 * 
	 * @param axis
	 *            the axes that will be modified
	 * @param value
	 *            the added value
	 */
	public void set(Axis axis, int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.setX(value);
			return;
		case Y:
			this.setY(value);
			return;
		case Z:
			this.setZ(value);
			return;
		case XY:
			this.setX(value);
			this.setY(value);
			return;
		case XZ:
			this.setX(value);
			this.setZ(value);
			return;
		case YZ:
			this.setY(value);
			this.setZ(value);
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	/**
	 * Return a mutable copy of this point, with the copy's axis values set to
	 * the specified value.
	 * 
	 * @param axis
	 *            the axes that are modified
	 * @param value
	 *            the new axis value
	 * @return a modified, mutable copy of this point
	 */
	public Point3i with(Axis axis, int value) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		Point3i result = this.toMutable();
		result.set(axis, value);
		return result;
	}

	@Override
	public void add(Point3i point) {
		this.addX(point.getX());
		this.addY(point.getY());
		this.addZ(point.getZ());
	}

	/**
	 * Adds the specified value to all of this point's values.
	 * 
	 * @param value
	 *            the value that will be used
	 */
	public void add(int value) {
		this.addX(value);
		this.addY(value);
		this.addZ(value);
	}

	@Override
	public void add(Axis axis, Point3i point) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (point == null) {
			throw new NullPointerException("Point must not be null");
		}
		switch (axis) {
		case X:
			this.addX(point.getX());
			return;
		case Y:
			this.addY(point.getY());
			return;
		case Z:
			this.addZ(point.getZ());
			return;
		case XY:
			this.addX(point.getX());
			this.addY(point.getY());
			return;
		case XZ:
			this.addX(point.getX());
			this.addZ(point.getZ());
			return;
		case YZ:
			this.addY(point.getY());
			this.addZ(point.getZ());
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	/**
	 * Adds the specified value to the specified axes.
	 * 
	 * @param axis
	 *            the axes that will be modified
	 * @param value
	 *            the added value
	 */
	public void add(Axis axis, int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.addX(value);
			return;
		case Y:
			this.addY(value);
			return;
		case Z:
			this.addZ(value);
			return;
		case XY:
			this.addX(value);
			this.addY(value);
			return;
		case XZ:
			this.addX(value);
			this.addZ(value);
			return;
		case YZ:
			this.addY(value);
			this.addZ(value);
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	/**
	 * Returns a mutable point that's translated by the specified amount.
	 * 
	 * @param value
	 *            the value that will be used
	 * @return a mutable point that's at this position, but translated by the
	 *         specified amount
	 */
	public Point3i added(int value) {
		Point3i result = this.toMutable();
		result.add(value);
		return result;
	}

	/**
	 * Returns a mutable point at this position, plus the specified translation.
	 * 
	 * @param axis
	 *            the axes that will be translated
	 * @param value
	 *            the added value
	 * @return a mutable point translated from this position
	 */
	public Point3i added(Axis axis, int value) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		Point3i result = this.toMutable();
		result.add(axis, value);
		return result;
	}

	@Override
	public void multiply(Point3i point) {
		this.multiplyX(point.getX());
		this.multiplyY(point.getY());
		this.multiplyZ(point.getZ());
	}

	@Override
	public void multiply(double factor) {
		this.multiplyX(factor);
		this.multiplyY(factor);
		this.multiplyZ(factor);
	}

	@Override
	public void multiply(Axis axis, Point3i point) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (point == null) {
			throw new NullPointerException("Point must not be null");
		}
		switch (axis) {
		case X:
			this.multiplyX(point.getX());
			return;
		case Y:
			this.multiplyY(point.getY());
			return;
		case Z:
			this.multiplyZ(point.getZ());
			return;
		case XY:
			this.multiplyX(point.getX());
			this.multiplyY(point.getY());
			return;
		case XZ:
			this.multiplyX(point.getX());
			this.multiplyZ(point.getZ());
			return;
		case YZ:
			this.multiplyY(point.getY());
			this.multiplyZ(point.getZ());
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public void multiply(Axis axis, double factor) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.multiplyX(factor);
			return;
		case Y:
			this.multiplyY(factor);
			return;
		case Z:
			this.multiplyZ(factor);
			return;
		case XY:
			this.multiplyX(factor);
			this.multiplyY(factor);
			return;
		case XZ:
			this.multiplyX(factor);
			this.multiplyZ(factor);
			return;
		case YZ:
			this.multiplyY(factor);
			this.multiplyZ(factor);
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public void interpolate(Point3i dest, float offset) {
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
	public void clear() {
		this.set(0);
	}

	@Override
	public void clear(Axis axis) {
		this.set(axis, 0);
	}

	@Override
	public Point3i toMutable() {
		return Point3i.mutable(x, y, z);
	}

	@Override
	public Point3i toFrozen() {
		if (!this.isMutable()) {
			return this;
		}
		return Point3i.frozen(x, y, z);
	}

	@Override
	public boolean at(Point3i point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return this.getX() == point.getX() &&
				this.getY() == point.getY() &&
				this.getZ() == point.getZ();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Point3i)) {
			return false;
		}
		final Point3i other = (Point3i) obj;
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
