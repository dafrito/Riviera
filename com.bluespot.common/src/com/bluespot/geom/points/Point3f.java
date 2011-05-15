/**
 * 
 */
package com.bluespot.geom.points;

import com.bluespot.geom.Axis;
import com.bluespot.geom.vectors.Vector3d;

/**
 * A {@link Point3} in {@code float} precision. Be aware that while this class
 * implements {@link #equals(Object)} appropriately, it may yield unexpected
 * results due to the inherent imprecision of floating-point values.
 * 
 * @author Aaron Faanes
 * 
 * @see Point3d
 * @see Point3i
 */
public final class Point3f extends AbstractPoint3<Point3f> {

	private static final Point3f ORIGIN = new Point3f(false, 0, 0, 0);

	/**
	 * Returns a frozen point at the origin.
	 * 
	 * @return a frozen point at the origin.
	 */
	public static Point3f origin() {
		return ORIGIN;
	}

	public static Point3f mutable(final float x, final float y, final float z) {
		return new Point3f(true, x, y, z);
	}

	public static Point3f frozen(final float x, final float y, final float z) {
		return new Point3f(false, x, y, z);
	}

	public static Point3f mutable(Point3f point) {
		return new Point3f(true, point.x, point.y, point.z);
	}

	public static Point3f frozen(Point3f point) {
		return new Point3f(false, point.x, point.y, point.z);
	}

	public static Point3f mutable(Point3i point) {
		return new Point3f(true, point.getX(), point.getY(), point.getZ());
	}

	public static Point3f frozen(Point3i point) {
		return new Point3f(false, point.getX(), point.getY(), point.getZ());
	}

	public static Point3f mutable(Point3d point) {
		return new Point3f(true, (float) point.getX(), (float) point.getY(), (float) point.getZ());
	}

	public static Point3f frozen(Point3d point) {
		return new Point3f(false, (float) point.getX(), (float) point.getY(), (float) point.getZ());
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
	public static Point3f interpolated(Point3f src, Point3f dest, final float offset) {
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
	private Point3f(final boolean mutable, final float x, final float y, final float z) {
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
	 * @param value
	 *            the new x value
	 * @return the old x value
	 */
	public float setX(float value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		float old = this.x;
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
	public Point3f withX(float value) {
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		Point3f result = this.toMutable();
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
	public float addX(float offset) {
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
	public Point3f addedX(float offset) {
		Point3f point = this.toMutable();
		point.addX(offset);
		return point;
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
	 * @param value
	 *            the new y value
	 * @return the old y value
	 */
	public float setY(float value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		float old = this.y;
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
	public Point3f withY(float value) {
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		Point3f result = this.toMutable();
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
	public float addY(float offset) {
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
	public Point3f addedY(float offset) {
		Point3f point = this.toMutable();
		point.addY(offset);
		return point;
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
	 * @param value
	 *            the new z value
	 * @return the old z value
	 */
	public float setZ(float value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Point is not mutable");
		}
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		float old = this.z;
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
	public Point3f withZ(float value) {
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		Point3f result = this.toMutable();
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
	public float addZ(float offset) {
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
	public Point3f addedZ(float offset) {
		Point3f point = this.toMutable();
		point.addZ(offset);
		return point;
	}

	@Override
	public void set(Point3f point) {
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
	public void set(float value) {
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
	 * @throw IllegalArgumentException if any value is NaN. All values are
	 *        checked before any are used.
	 */
	public void set(float x, float y, float z) {
		if (Float.isNaN(x)) {
			throw new IllegalArgumentException("x must not be NaN");
		}
		if (Float.isNaN(y)) {
			throw new IllegalArgumentException("y must not be NaN");
		}
		if (Float.isNaN(z)) {
			throw new IllegalArgumentException("z must not be NaN");
		}
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	@Override
	public void add(Point3f point) {
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
	public void add(float value) {
		this.addX(value);
		this.addY(value);
		this.addZ(value);
	}

	@Override
	public Point3f added(Point3f point) {
		Point3f result = this.toMutable();
		result.add(point);
		return result;
	}

	/**
	 * Returns a mutable point that's translated by the specified amount.
	 * 
	 * @param value
	 *            the value that will be used
	 * @return a mutable point that's at this position, but translated by the
	 *         specified amount
	 */
	public Point3f added(float value) {
		Point3f result = this.toMutable();
		result.add(value);
		return result;
	}

	@Override
	public void set(Axis axis, Point3f point) {
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
	public void set(Axis axis, float value) {
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

	@Override
	public void add(Axis axis, Point3f point) {
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
	public void add(Axis axis, float value) {
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

	@Override
	public Point3f added(Axis axis, Point3f point) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (point == null) {
			throw new NullPointerException("Point must not be null");
		}
		Point3f result = this.toMutable();
		result.add(axis, point);
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
	public Point3f added(Axis axis, float value) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		Point3f result = this.toMutable();
		result.add(axis, value);
		return result;
	}

	@Override
	public void clear() {
		this.set(0f);
	}

	@Override
	public void clear(Axis axis) {
		this.set(axis, 0f);
	}

	@Override
	public Point3f cleared(Axis axis) {
		Point3f result = this.toMutable();
		result.clear(axis);
		return result;
	}

	/**
	 * Creates and returns a new {@link Point3f} that is this point translated
	 * by the specified {@link Vector3d}.
	 * 
	 * @param vector
	 *            the vector used to create the new point
	 * @return a new point that is this point translated by the specified vector
	 */
	public Point3f add(final Vector3d vector) {
		return new Point3f(false, this.getX() + (float) vector.getX(), this.getY() + (float) vector.getY(),
				this.getZ() + (float) vector.getZ());
	}

	@Override
	public Point3f toMutable() {
		return Point3f.mutable(x, y, z);
	}

	@Override
	public Point3f toFrozen() {
		if (!this.isMutable()) {
			return this;
		}
		return Point3f.frozen(x, y, z);
	}

	@Override
	public boolean at(Point3f point) {
		if (point == null) {
			throw new NullPointerException("point must not be null");
		}
		return this.getX() == point.getX() &&
				this.getY() == point.getY() &&
				this.getZ() == point.getZ();
	}

	@Override
	public Point3f interpolated(Point3f dest, float offset) {
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
	public void interpolate(Point3f dest, float offset) {
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
		if (!(obj instanceof Point3f)) {
			return false;
		}
		final Point3f other = (Point3f) obj;
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
		int result = 11;
		result = 31 * result + (this.isMutable() ? 1 : 0);
		result = 31 * result + java.lang.Float.floatToIntBits(this.getX());
		result = 31 * result + java.lang.Float.floatToIntBits(this.getY());
		result = 31 * result + java.lang.Float.floatToIntBits(this.getZ());
		return result;
	}

	@Override
	public String toString() {
		return String.format("Point3D.Float[%s (%f, %f, %f)]", this.isMutable() ? "mutable" : "frozen", this.getX(), this.getY(), this.getZ());
	}

}
