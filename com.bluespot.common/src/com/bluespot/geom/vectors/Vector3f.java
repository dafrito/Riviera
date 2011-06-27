/**
 * 
 */
package com.bluespot.geom.vectors;

import com.bluespot.geom.Axis;

/**
 * A {@link Vector3} in {@code float} precision. Be aware that while this class
 * implements {@link #equals(Object)} appropriately, it may yield unexpected
 * results due to the inherent imprecision of floating-point values.
 * 
 * @author Aaron Faanes
 * 
 * @see Vector3d
 * @see Vector3i
 */
public final class Vector3f extends AbstractVector3<Vector3f> {

	/**
	 * Create a mutable {@link Vector3f} using the specified value for all axes.
	 * 
	 * @param v
	 *            the value used for all axes
	 * @return a mutable {@code Vector3f}
	 * @throw {@link IllegalArgumentException} if {@code v} is {@code NaN}
	 */
	public static Vector3f mutable(float v) {
		return Vector3f.mutable(v, v, v);
	}

	/**
	 * Create a frozen {@link Vector3f} using the specified value for all axes.
	 * 
	 * @param v
	 *            the value used for all axes
	 * @return a frozen {@code Vector3f}
	 * @throw {@link IllegalArgumentException} if {@code v} is {@code NaN}
	 */
	public static Vector3f frozen(float v) {
		return Vector3f.mutable(v, v, v);
	}

	public static Vector3f mutable(final float x, final float y, final float z) {
		return new Vector3f(true, x, y, z);
	}

	public static Vector3f frozen(final float x, final float y, final float z) {
		return new Vector3f(false, x, y, z);
	}

	public static Vector3f mutable(Vector3i vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3f(true, vector.getX(), vector.getY(), vector.getZ());
	}

	public static Vector3f frozen(Vector3i vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3f(false, vector.getX(), vector.getY(), vector.getZ());
	}

	public static Vector3f mutable(Vector3d vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3f(true, (float) vector.getX(), (float) vector.getY(), (float) vector.getZ());
	}

	public static Vector3f frozen(Vector3d vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3f(false, (float) vector.getX(), (float) vector.getY(), (float) vector.getZ());
	}

	/**
	 * Interpolates between this vector and the destination. Offsets that are
	 * not between zero and one are handled specially:
	 * <ul>
	 * <li>If {@code offset <= 0}, a copy of {@code src} is returned
	 * <li>If {@code offset >= 1}, a copy of {@code dest} is returned
	 * </ul>
	 * This special behavior allows clients to reliably detect when
	 * interpolation is complete.
	 * 
	 * @param src
	 *            the starting vector
	 * @param dest
	 *            the ending vector
	 * @param offset
	 *            the percentage of distance between the specified points
	 * @return a mutable vector that lies between src and dest
	 */
	public static Vector3f interpolated(Vector3f src, Vector3f dest, final float offset) {
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

	private static final Vector3f ORIGIN = Vector3f.frozen(0);

	/**
	 * Returns a frozen vector at the origin.
	 * 
	 * @return a frozen vector at the origin.
	 */
	public static Vector3f origin() {
		return ORIGIN;
	}

	private static final Vector3f UP = Vector3f.frozen(0, 1, 0);

	/**
	 * Returns a frozen vector that points up the y axis.
	 * 
	 * @return a frozen vector with components {@code (0, 1, 0)}
	 */
	public static Vector3f up() {
		return UP;
	}

	private static final Vector3f FORWARD = Vector3f.frozen(0, 0, -1);

	/**
	 * Returns a frozen vector that points down the z axis.
	 * 
	 * @return a frozen vector with components {@code (0, 0, -1)}
	 */
	public static Vector3f forward() {
		return FORWARD;
	}

	/**
	 * Return a frozen vector with values of 1 at the specified axes. This is
	 * normally used to create unit vectors, but {@code axis} values of multiple
	 * axes are allowed.
	 * 
	 * @param axis
	 *            the axes with values of 1
	 * @return a frozen unit vector
	 */
	public static Vector3f unit(Axis axis) {
		return origin().with(axis, 1).toFrozen();
	}

	private static final Vector3f LEFT = Vector3f.frozen(1, 0, 0);

	/**
	 * Returns a frozen vector that points down the positive x axis.
	 * 
	 * @return a frozen vector with components {@code (1, 0, 0)}
	 */
	public static Vector3f left() {
		return LEFT;
	}

	private static final Vector3f RIGHT = LEFT.inverted().toFrozen();

	/**
	 * Returns a frozen vector that points down the negative x axis.
	 * 
	 * @return a frozen vector with components {@code (-1, 0, 0)}
	 */
	public static final Vector3f right() {
		return RIGHT;
	}

	private static final Vector3f DOWN = UP.inverted().toFrozen();

	/**
	 * Returns a frozen vector that points down the negative Y axis.
	 * 
	 * @return a frozen vector with components {@code (0, -1, 0)}
	 */
	public static final Vector3f down() {
		return DOWN;
	}

	private float z;
	private float y;
	private float x;

	/**
	 * Constructs a vector using the specified coordinates. There are no
	 * restrictions on the values of these points except that none of them can
	 * be {@code NaN}.
	 * 
	 * @param mutable
	 *            whether this vector can be directly modified
	 * @param x
	 *            the x-coordinate of this vector
	 * @param y
	 *            the y-coordinate of this vector
	 * @param z
	 *            the z-coordinate of this vector
	 * @throws IllegalArgumentException
	 *             if any coordinate is {@code NaN}
	 */
	private Vector3f(final boolean mutable, final float x, final float y, final float z) {
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
	 * Returns the x-coordinate of this vector.
	 * 
	 * @return the x-coordinate of this vector
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		float old = this.x;
		this.x = value;
		return old;
	}

	/**
	 * Returns a translated mutable vector. The returned vector will be at the
	 * same position as this one, but with the x value set to the specified
	 * value.
	 * 
	 * @param value
	 *            the new x value
	 * @return a mutable vector that uses the specified value for its x axis
	 */
	public Vector3f withX(float value) {
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		Vector3f result = this.toMutable();
		result.setX(value);
		return result;
	}

	/**
	 * Add the specified x value to this vector.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old x value
	 */
	public float addX(float offset) {
		return this.setX(this.getX() + offset);
	}

	/**
	 * Return a mutable vector that has the same position as this one, except
	 * for the specified translation.
	 * 
	 * @param offset
	 *            the value to add
	 * @return a vector at {@code (x + offset, y, z)}
	 */
	public Vector3f addedX(float offset) {
		Vector3f vector = this.toMutable();
		vector.addX(offset);
		return vector;
	}

	/**
	 * Subtract the specified value from this vector's X axis.
	 * 
	 * @param offset
	 *            the value to subtract
	 * @return the old value at the X axis
	 * @see #subtractedX(int)
	 * @throw UnsupportedOperationException if this vector is not mutable
	 * @throw IllegalArgumentException if {@code offset} is NaN
	 */
	public float subtractX(float offset) {
		return this.setX(this.getX() - offset);
	}

	/**
	 * Return a mutable vector at this vector's position, but with the specified
	 * translation.
	 * 
	 * @param offset
	 *            the value to subtract
	 * @return a mutable vector at {@code (x - offset, y, z)}
	 * @see #subtractX(int)
	 * @throw IllegalArgumentException if {@code offset} is NaN
	 */
	public Vector3f subtractedX(float offset) {
		return this.withX(this.getX() - offset);
	}

	/**
	 * Multiply the specified x value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old x value
	 */
	public float multiplyX(double factor) {
		return this.setX((float) (this.getX() * factor));
	}

	/**
	 * Return a mutable copy of this vector, with a multiplied x value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable vector at {@code (x * offset, y, z)}
	 */
	public Vector3f mulipliedX(double factor) {
		Vector3f vector = this.toMutable();
		vector.multiplyX(factor);
		return vector;
	}

	/**
	 * Returns the y-coordinate of this vector.
	 * 
	 * @return the y-coordinate of this vector
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		float old = this.y;
		this.y = value;
		return old;
	}

	/**
	 * Returns a translated mutable vector. The returned vector will be at the
	 * same position as this one, but with the y value set to the specified
	 * value.
	 * 
	 * @param value
	 *            the new y value
	 * @return a mutable vector that uses the specified value for its y axis
	 */
	public Vector3f withY(float value) {
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		Vector3f result = this.toMutable();
		result.setY(value);
		return result;
	}

	/**
	 * Add the specified y value to this vector.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old y value
	 */
	public float addY(float offset) {
		return this.setY(this.getY() + offset);
	}

	/**
	 * Return a mutable vector that has the same position as this one, except
	 * for the specified translation.
	 * 
	 * @param offset
	 *            the value to add
	 * @return a vector at {@code (x, y + offset, z)}
	 */
	public Vector3f addedY(float offset) {
		Vector3f vector = this.toMutable();
		vector.addY(offset);
		return vector;
	}

	/**
	 * Subtract the specified value from this vector's Y axis.
	 * 
	 * @param offset
	 *            the value to subtract
	 * @return the old value at the Y axis
	 * @see #subtractedY(int)
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public float subtractY(float offset) {
		return this.setY(this.getY() - offset);
	}

	/**
	 * Return a mutable vector at this vector's position, but with the specified
	 * translation.
	 * 
	 * @param offset
	 *            the value to subtract
	 * @return a mutable vector at {@code (x, y - offset, z)}
	 * @see #subtractY(int)
	 */
	public Vector3f subtractedY(float offset) {
		return this.withY(this.getY() - offset);
	}

	/**
	 * Multiply the specified y value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old y value
	 */
	public double multiplyY(double factor) {
		return this.setY((float) (this.getY() * factor));
	}

	/**
	 * Return a mutable copy of this vector, with a multiplied y value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable vector at {@code (x, y * offset, z)}
	 */
	public Vector3f mulipliedY(double factor) {
		Vector3f vector = this.toMutable();
		vector.multiplyY(factor);
		return vector;
	}

	/**
	 * Returns the z-coordinate of this vector.
	 * 
	 * @return the z-coordinate of this vector
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		float old = this.z;
		this.z = value;
		return old;
	}

	/**
	 * Returns a translated mutable vector. The returned vector will be at the
	 * same position as this one, but with the z value set to the specified
	 * value.
	 * 
	 * @param value
	 *            the new z value
	 * @return a mutable vector that uses the specified value for its z axis
	 */
	public Vector3f withZ(float value) {
		if (Float.isNaN(value)) {
			throw new IllegalArgumentException("value must not be NaN");
		}
		Vector3f result = this.toMutable();
		result.setZ(value);
		return result;
	}

	/**
	 * Add the specified z value to this vector.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old z value
	 */
	public float addZ(float offset) {
		return this.setZ(this.getZ() + offset);
	}

	/**
	 * Return a mutable vector that has the same position as this one, except
	 * for the specified translation.
	 * 
	 * @param offset
	 *            the value to add
	 * @return a vector at {@code (x, y, z + offset)}
	 */
	public Vector3f addedZ(float offset) {
		Vector3f vector = this.toMutable();
		vector.addZ(offset);
		return vector;
	}

	/**
	 * Subtract the specified value from this vector's Z axis.
	 * 
	 * @param offset
	 *            the value to subtract
	 * @return the old value at the Z axis
	 * @see #subtractedZ(int)
	 * @throw UnsupportedOperationException if this vector is not mutable
	 * @throw IllegalArgumentException if {@code offset} is NaN
	 */
	public float subtractZ(float offset) {
		return this.setZ(this.getZ() - offset);
	}

	/**
	 * Return a mutable vector at this vector's position, but with the specified
	 * translation.
	 * 
	 * @param offset
	 *            the value to subtract
	 * @return a mutable vector at {@code (x, y, z - offset)}
	 * @see #subtractZ(int)
	 * @throw IllegalArgumentException if {@code offset} is NaN
	 */
	public Vector3f subtractedZ(float offset) {
		return this.withZ(this.getZ() - offset);
	}

	/**
	 * Multiply the specified z value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old z value
	 */
	public double multiplyZ(double factor) {
		return this.setZ((float) (this.getZ() * factor));
	}

	/**
	 * Return a mutable copy of this vector, with a multiplied z value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable vector at {@code (x, y, z * offset)}
	 */
	public Vector3f mulipliedZ(double factor) {
		Vector3f vector = this.toMutable();
		vector.multiplyZ(factor);
		return vector;
	}

	@Override
	public void set(Vector3f vector) {
		this.setX(vector.getX());
		this.setY(vector.getY());
		this.setZ(vector.getZ());
	}

	/**
	 * Sets all of this vector's values to the specified value.
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
	 * Sets all of this vector's values to the specified values.
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
	public void set(Axis axis, Vector3f vector) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		switch (axis) {
		case X:
			this.setX(vector.getX());
			return;
		case Y:
			this.setY(vector.getY());
			return;
		case Z:
			this.setZ(vector.getZ());
			return;
		case XY:
			this.setX(vector.getX());
			this.setY(vector.getY());
			return;
		case XZ:
			this.setX(vector.getX());
			this.setZ(vector.getZ());
			return;
		case YZ:
			this.setY(vector.getY());
			this.setZ(vector.getZ());
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
			throw new UnsupportedOperationException("vector is not mutable");
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
	 * Return a mutable copy of this vector, with the copy's axis values set to
	 * the specified value.
	 * 
	 * @param axis
	 *            the axes that are modified
	 * @param value
	 *            the new axis value
	 * @return a modified, mutable copy of this vector
	 */
	public Vector3f with(Axis axis, float value) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		Vector3f result = this.toMutable();
		result.set(axis, value);
		return result;
	}

	@Override
	public void add(Vector3f vector) {
		this.addX(vector.getX());
		this.addY(vector.getY());
		this.addZ(vector.getZ());
	}

	/**
	 * Adds the specified value to all of this vector's values.
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
	public void add(Axis axis, Vector3f vector) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		switch (axis) {
		case X:
			this.addX(vector.getX());
			return;
		case Y:
			this.addY(vector.getY());
			return;
		case Z:
			this.addZ(vector.getZ());
			return;
		case XY:
			this.addX(vector.getX());
			this.addY(vector.getY());
			return;
		case XZ:
			this.addX(vector.getX());
			this.addZ(vector.getZ());
			return;
		case YZ:
			this.addY(vector.getY());
			this.addZ(vector.getZ());
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
			throw new UnsupportedOperationException("vector is not mutable");
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
	 * Returns a mutable vector that's translated by the specified amount.
	 * 
	 * @param value
	 *            the value that will be used
	 * @return a mutable vector that's at this position, but translated by the
	 *         specified amount
	 */
	public Vector3f added(float value) {
		Vector3f result = this.toMutable();
		result.add(value);
		return result;
	}

	/**
	 * Returns a mutable vector at this position, plus the specified
	 * translation.
	 * 
	 * @param axis
	 *            the axes that will be translated
	 * @param value
	 *            the added value
	 * @return a mutable vector translated from this position
	 */
	public Vector3f added(Axis axis, float value) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		Vector3f result = this.toMutable();
		result.add(axis, value);
		return result;
	}

	@Override
	public void subtract(Vector3f vector) {
		this.subtractX(vector.getX());
		this.subtractY(vector.getY());
		this.subtractZ(vector.getZ());
	}

	/**
	 * Subtracts the specified value from each of this vector's values.
	 * 
	 * @param value
	 *            the value that will be used
	 */
	public void subtract(float value) {
		this.subtractX(value);
		this.subtractY(value);
		this.subtractZ(value);
	}

	@Override
	public void subtract(Axis axis, Vector3f vector) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		switch (axis) {
		case X:
			this.subtractX(vector.getX());
			return;
		case Y:
			this.subtractY(vector.getY());
			return;
		case Z:
			this.subtractZ(vector.getZ());
			return;
		case XY:
			this.subtractX(vector.getX());
			this.subtractY(vector.getY());
			return;
		case XZ:
			this.subtractX(vector.getX());
			this.subtractZ(vector.getZ());
			return;
		case YZ:
			this.subtractY(vector.getY());
			this.subtractZ(vector.getZ());
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	/**
	 * Subtracts the specified value from the specified axes.
	 * 
	 * @param axis
	 *            the axes that will be modified
	 * @param value
	 *            the subtracted value
	 */
	public void subtract(Axis axis, float value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.subtractX(value);
			return;
		case Y:
			this.subtractY(value);
			return;
		case Z:
			this.subtractZ(value);
			return;
		case XY:
			this.subtractX(value);
			this.subtractY(value);
			return;
		case XZ:
			this.subtractX(value);
			this.subtractZ(value);
			return;
		case YZ:
			this.subtractY(value);
			this.subtractZ(value);
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	/**
	 * Returns a mutable vector that's translated by the specified amount.
	 * 
	 * @param value
	 *            the value that will be used
	 * @return a mutable vector that's at this position, but translated by the
	 *         specified amount
	 */
	public Vector3f subtracted(float value) {
		Vector3f result = this.toMutable();
		result.add(value);
		return result;
	}

	/**
	 * Returns a mutable vector at this position, minus the specified
	 * translation.
	 * 
	 * @param axis
	 *            the axes that will be translated
	 * @param value
	 *            the subtracted value
	 * @return a mutable vector translated from this position
	 */
	public Vector3f subtracted(Axis axis, float value) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		Vector3f result = this.toMutable();
		result.subtract(axis, value);
		return result;
	}

	@Override
	public void multiply(Vector3f vector) {
		this.multiplyX(vector.getX());
		this.multiplyY(vector.getY());
		this.multiplyZ(vector.getZ());
	}

	@Override
	public void multiply(double factor) {
		this.multiplyX(factor);
		this.multiplyY(factor);
		this.multiplyZ(factor);
	}

	@Override
	public void multiply(Axis axis, Vector3f vector) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		switch (axis) {
		case X:
			this.multiplyX(vector.getX());
			return;
		case Y:
			this.multiplyY(vector.getY());
			return;
		case Z:
			this.multiplyZ(vector.getZ());
			return;
		case XY:
			this.multiplyX(vector.getX());
			this.multiplyY(vector.getY());
			return;
		case XZ:
			this.multiplyX(vector.getX());
			this.multiplyZ(vector.getZ());
			return;
		case YZ:
			this.multiplyY(vector.getY());
			this.multiplyZ(vector.getZ());
			return;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public void multiply(Axis axis, double factor) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("vector is not mutable");
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
	public double length() {
		return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2) + Math.pow(this.getZ(), 2));
	}

	@Override
	public void normalize() {
		float len = (float) this.length();
		this.set(this.getX() / len,
				this.getY() / len,
				this.getZ() / len);
	}

	@Override
	public void interpolate(Vector3f dest, float offset) {
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
	public void cross(Vector3f other) {
		this.set(this.getY() * other.getZ() - other.getY() * this.getZ(),
				-this.getX() * other.getZ() + other.getX() * this.getZ(),
				this.getX() * other.getY() - other.getX() * this.getY());
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
	public Vector3f toMutable() {
		return Vector3f.mutable(x, y, z);
	}

	@Override
	public Vector3f toFrozen() {
		if (!this.isMutable()) {
			return this;
		}
		return Vector3f.frozen(x, y, z);
	}

	@Override
	public boolean at(Vector3f vector) {
		if (vector == null) {
			return false;
		}
		return this.getX() == vector.getX() &&
				this.getY() == vector.getY() &&
				this.getZ() == vector.getZ();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Vector3f)) {
			return false;
		}
		final Vector3f other = (Vector3f) obj;
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
		return String.format("Vector3f[%s (%f, %f, %f)]", this.isMutable() ? "mutable" : "frozen", this.getX(), this.getY(), this.getZ());
	}

}
