/**
 * 
 */
package com.bluespot.geom.vectors;

import java.awt.Dimension;
import java.awt.Point;

import com.bluespot.geom.Axis;

/**
 * A {@link Vector3} in {@code int} precision.
 * 
 * @author Aaron Faanes
 * 
 * @see Vector3f
 * @see Vector3d
 */
public final class Vector3i extends AbstractVector3<Vector3i> {

	public static Vector3i mutable() {
		return mutable(0);
	}

	public static Vector3i frozen() {
		return frozen(0);
	}

	/**
	 * Create a mutable {@link Vector3i} using the specified value for all axes.
	 * 
	 * @param v
	 *            the value used for all axes
	 * @return a mutable {@code Vector3i}
	 * @throw {@link IllegalArgumentException} if {@code v} is {@code NaN}
	 */
	public static Vector3i mutable(int v) {
		return Vector3i.mutable(v, v, v);
	}

	/**
	 * Create a frozen {@link Vector3i} using the specified value for all axes.
	 * 
	 * @param v
	 *            the value used for all axes
	 * @return a frozen {@code Vector3i}
	 * @throw {@link IllegalArgumentException} if {@code v} is {@code NaN}
	 */
	public static Vector3i frozen(int v) {
		return Vector3i.frozen(v, v, v);
	}

	public static Vector3i mutable(int x, int y, int z) {
		return new Vector3i(true, x, y, z);
	}

	public static Vector3i mutable(int x, int y) {
		return mutable(x, y, 0);
	}

	public static Vector3i frozen(int x, int y, int z) {
		return new Vector3i(false, x, y, z);
	}

	public static Vector3i frozen(int x, int y) {
		return frozen(x, y, 0);
	}

	public static Vector3i mutable(Vector3f vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3i(true, (int) vector.x(), (int) vector.y(), (int) vector.z());
	}

	public static Vector3i frozen(Vector3f vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3i(false, (int) vector.x(), (int) vector.y(), (int) vector.z());
	}

	public static Vector3i mutable(Vector3d vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3i(true, (int) vector.x(), (int) vector.y(), (int) vector.z());
	}

	public static Vector3i frozen(Vector3d vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3i(false, (int) vector.x(), (int) vector.y(), (int) vector.z());
	}

	public static Vector3i mutable(Point point) {
		return Vector3i.mutable(point.x, point.y, 0);
	}

	public static Vector3i frozen(Point point) {
		return Vector3i.frozen(point.x, point.y, 0);
	}

	public static Vector3i mutable(Dimension dimension) {
		return Vector3i.mutable(dimension.width, dimension.height, 0);
	}

	public static Vector3i frozen(Dimension dimension) {
		return Vector3i.frozen(dimension.width, dimension.height, 0);
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
	public static Vector3i interpolated(Vector3i src, Vector3i dest, float offset) {
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

	private static final Vector3i ORIGIN = Vector3i.frozen(0);

	/**
	 * Returns a frozen vector at the origin.
	 * 
	 * @return a frozen vector at the origin.
	 */
	public static Vector3i origin() {
		return ORIGIN;
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
	public static Vector3i unit(Axis axis) {
		return origin().copy().set(axis, 1).toFrozen();
	}

	private static final Vector3i UP = Vector3i.frozen(0, 1, 0);

	/**
	 * Returns a frozen vector that points up the y axis.
	 * 
	 * @return a frozen vector with components {@code (0, 1, 0)}
	 */
	public static Vector3i up() {
		return UP;
	}

	private static final Vector3i FORWARD = Vector3i.frozen(0, 0, -1);

	/**
	 * Returns a frozen vector that points down the z axis.
	 * 
	 * @return a frozen vector with components {@code (0, 0, -1)}
	 */
	public static Vector3i forward() {
		return FORWARD;
	}

	private static final Vector3i LEFT = Vector3i.frozen(-1, 0, 0);

	/**
	 * Returns a frozen vector that points down the negative x axis.
	 * 
	 * @return a frozen vector with components {@code (-1, 0, 0)}
	 */
	public static final Vector3i left() {
		return LEFT;
	}

	private static final Vector3i RIGHT = Vector3i.frozen(1, 0, 0);

	/**
	 * Returns a frozen vector that points down the positive x axis.
	 * 
	 * @return a frozen vector with components {@code (1, 0, 0)}
	 */
	public static Vector3i right() {
		return RIGHT;
	}

	private static final Vector3i DOWN = UP.toMutable().negate().toFrozen();

	/**
	 * Returns a frozen vector that points down the negative Y axis.
	 * 
	 * @return a frozen vector with components {@code (0, -1, 0)}
	 */
	public static final Vector3i down() {
		return DOWN;
	}

	private int z;
	private int y;
	private int x;

	/**
	 * Constructs a vector using the specified coordinates.
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
	private Vector3i(final boolean mutable, final int x, final int y, final int z) {
		super(mutable);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the x-coordinate of this vector.
	 * 
	 * @return the x-coordinate of this vector
	 */
	public int x() {
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		int old = this.x;
		this.x = value;
		return old;
	}

	/**
	 * Add the specified x value to this vector.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old x value
	 */
	public int addX(int offset) {
		return this.setX(this.x() + offset);
	}

	/**
	 * Subtract the specified value from this vector's X axis.
	 * 
	 * @param offset
	 *            the value to subtract
	 * @return the old value at the X axis
	 * @see #subtractedX(int)
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public int subtractX(int offset) {
		return this.setX(this.x() - offset);
	}

	/**
	 * Multiply the specified x value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old x value
	 */
	public int multiplyX(double factor) {
		return this.setX((int) Math.round(this.x() * factor));
	}

	public int divideX(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setX((int) Math.round(this.x() / denominator));
	}

	public int floorDivideX(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setX((int) Math.floor(this.x() / denominator));
	}

	public int ceilDivideX(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setX((int) Math.ceil(this.x() / denominator));
	}

	public int moduloX(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setX((int) Math.round(this.x() % denominator));
	}

	public int maxX(int max) {
		return this.setX(Math.max(max, this.x()));
	}

	public int minX(int min) {
		return this.setX(Math.min(min, this.x()));
	}

	/**
	 * Returns the y-coordinate of this vector.
	 * 
	 * @return the y-coordinate of this vector
	 */
	public int y() {
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		int old = this.y;
		this.y = value;
		return old;
	}

	/**
	 * Add the specified y value to this vector.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old y value
	 */
	public int addY(int offset) {
		return this.setY(this.y() + offset);
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
	public int subtractY(int offset) {
		return this.setY(this.y() - offset);
	}

	/**
	 * Multiply the specified y value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old y value
	 */
	public int multiplyY(double factor) {
		return this.setY((int) Math.round(this.y() * factor));
	}

	public int divideY(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setY((int) Math.round(this.y() / denominator));
	}

	public int floorDivideY(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setY((int) Math.floor(this.y() / denominator));
	}

	public int ceilDivideY(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setY((int) Math.ceil(this.y() / denominator));
	}

	public int moduloY(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setY((int) Math.round(this.y() % denominator));
	}

	public int maxY(int max) {
		return this.setY(Math.max(max, this.y()));
	}

	public int minY(int min) {
		return this.setY(Math.min(min, this.y()));
	}

	/**
	 * Returns the z-coordinate of this vector.
	 * 
	 * @return the z-coordinate of this vector
	 */
	public int z() {
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		int old = this.z;
		this.z = value;
		return old;
	}

	/**
	 * Add the specified z value to this vector.
	 * 
	 * @param offset
	 *            the value to add
	 * @return the old z value
	 */
	public int addZ(int offset) {
		return this.setZ(this.z() + offset);
	}

	/**
	 * Subtract the specified value from this vector's Z axis.
	 * 
	 * @param offset
	 *            the value to subtract
	 * @return the old value at the Z axis
	 * @see #subtractedZ(int)
	 * @throw UnsupportedOperationException if this vector is not mutable
	 */
	public int subtractZ(int offset) {
		return this.setZ(this.z() - offset);
	}

	/**
	 * Multiply the specified z value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old z value
	 */
	public int multiplyZ(double factor) {
		return this.setZ((int) Math.round(this.z() * factor));
	}

	public int divideZ(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setZ((int) Math.round(this.z() / denominator));
	}

	public int floorDivideZ(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setZ((int) Math.floor(this.z() / denominator));
	}

	public int ceilDivideZ(double denominator) {
		if (Double.isNaN(denominator)) {
			throw new IllegalArgumentException("denominator must not be NaN");
		}
		return this.setZ((int) Math.ceil(this.z() / denominator));
	}

	public int moduloZ(double denominator) {
		return this.setZ((int) Math.round(this.z() % denominator));
	}

	public int maxZ(int max) {
		return this.setZ(Math.max(max, this.z()));
	}

	public int minZ(int min) {
		return this.setZ(Math.min(min, this.z()));
	}

	@Override
	public Vector3i set(Vector3i vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		this.setX(vector.x());
		this.setY(vector.y());
		this.setZ(vector.z());
		return this;
	}

	/**
	 * Sets all of this vector's values to the specified value.
	 * 
	 * @param value
	 *            the value that will be used
	 * @return {@code this}
	 */
	public Vector3i set(int value) {
		this.setX(value);
		this.setY(value);
		this.setZ(value);
		return this;
	}

	/**
	 * Sets the x and y components to the specified values.
	 * 
	 * @param x
	 *            the new x value
	 * @param y
	 *            the new y value
	 * @return {@code this}
	 */
	public Vector3i set(int x, int y) {
		this.setX(x);
		this.setY(y);
		return this;
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
	 * @return {@code this}
	 */
	public Vector3i set(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		return this;
	}

	@Override
	public Vector3i set(Axis axis, Vector3i vector) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		switch (axis) {
		case X:
			this.setX(vector.x());
			return this;
		case Y:
			this.setY(vector.y());
			return this;
		case Z:
			this.setZ(vector.z());
			return this;
		case XY:
			this.setX(vector.x());
			this.setY(vector.y());
			return this;
		case XZ:
			this.setX(vector.x());
			this.setZ(vector.z());
			return this;
		case YZ:
			this.setY(vector.y());
			this.setZ(vector.z());
			return this;
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
	 * @return {@code this}
	 */
	public Vector3i set(Axis axis, int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.setX(value);
			return this;
		case Y:
			this.setY(value);
			return this;
		case Z:
			this.setZ(value);
			return this;
		case XY:
			this.setX(value);
			this.setY(value);
			return this;
		case XZ:
			this.setX(value);
			this.setZ(value);
			return this;
		case YZ:
			this.setY(value);
			this.setZ(value);
			return this;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public Vector3i add(Vector3i vector) {
		this.addX(vector.x());
		this.addY(vector.y());
		this.addZ(vector.z());
		return this;
	}

	/**
	 * Adds the specified value to all of this vector's values.
	 * 
	 * @param value
	 *            the value that will be used
	 * @return {@code this}
	 */
	public Vector3i add(int value) {
		this.addX(value);
		this.addY(value);
		this.addZ(value);
		return this;
	}

	@Override
	public Vector3i add(Axis axis, Vector3i vector) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		switch (axis) {
		case X:
			this.addX(vector.x());
			return this;
		case Y:
			this.addY(vector.y());
			return this;
		case Z:
			this.addZ(vector.z());
			return this;
		case XY:
			this.addX(vector.x());
			this.addY(vector.y());
			return this;
		case XZ:
			this.addX(vector.x());
			this.addZ(vector.z());
			return this;
		case YZ:
			this.addY(vector.y());
			this.addZ(vector.z());
			return this;
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
	 * @return {@code this}
	 */
	public Vector3i add(Axis axis, int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.addX(value);
			return this;
		case Y:
			this.addY(value);
			return this;
		case Z:
			this.addZ(value);
			return this;
		case XY:
			this.addX(value);
			this.addY(value);
			return this;
		case XZ:
			this.addX(value);
			this.addZ(value);
			return this;
		case YZ:
			this.addY(value);
			this.addZ(value);
			return this;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public Vector3i subtract(Vector3i vector) {
		this.subtractX(vector.x());
		this.subtractY(vector.y());
		this.subtractZ(vector.z());
		return this;
	}

	/**
	 * Subtracts the specified value from each of this vector's values.
	 * 
	 * @param value
	 *            the value that will be used
	 * @return {@code this}
	 */
	public Vector3i subtract(int value) {
		this.subtractX(value);
		this.subtractY(value);
		this.subtractZ(value);
		return this;
	}

	@Override
	public Vector3i subtract(Axis axis, Vector3i vector) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		switch (axis) {
		case X:
			this.subtractX(vector.x());
			return this;
		case Y:
			this.subtractY(vector.y());
			return this;
		case Z:
			this.subtractZ(vector.z());
			return this;
		case XY:
			this.subtractX(vector.x());
			this.subtractY(vector.y());
			return this;
		case XZ:
			this.subtractX(vector.x());
			this.subtractZ(vector.z());
			return this;
		case YZ:
			this.subtractY(vector.y());
			this.subtractZ(vector.z());
			return this;
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
	 * @return {@code this}
	 */
	public Vector3i subtract(Axis axis, int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.subtractX(value);
			return this;
		case Y:
			this.subtractY(value);
			return this;
		case Z:
			this.subtractZ(value);
			return this;
		case XY:
			this.subtractX(value);
			this.subtractY(value);
			return this;
		case XZ:
			this.subtractX(value);
			this.subtractZ(value);
			return this;
		case YZ:
			this.subtractY(value);
			this.subtractZ(value);
			return this;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public Vector3i multiply(Vector3i vector) {
		this.multiplyX(vector.x());
		this.multiplyY(vector.y());
		this.multiplyZ(vector.z());
		return this;
	}

	@Override
	public Vector3i multiply(double factor) {
		this.multiplyX(factor);
		this.multiplyY(factor);
		this.multiplyZ(factor);
		return this;
	}

	@Override
	public Vector3i multiply(double x, double y, double z) {
		this.multiplyX(x);
		this.multiplyY(y);
		this.multiplyZ(z);
		return this;
	}

	@Override
	public Vector3i multiply(Axis axis, Vector3i vector) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		switch (axis) {
		case X:
			this.multiplyX(vector.x());
			return this;
		case Y:
			this.multiplyY(vector.y());
			return this;
		case Z:
			this.multiplyZ(vector.z());
			return this;
		case XY:
			this.multiplyX(vector.x());
			this.multiplyY(vector.y());
			return this;
		case XZ:
			this.multiplyX(vector.x());
			this.multiplyZ(vector.z());
			return this;
		case YZ:
			this.multiplyY(vector.y());
			this.multiplyZ(vector.z());
			return this;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public Vector3i multiply(Axis axis, double factor) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.multiplyX(factor);
			return this;
		case Y:
			this.multiplyY(factor);
			return this;
		case Z:
			this.multiplyZ(factor);
			return this;
		case XY:
			this.multiplyX(factor);
			this.multiplyY(factor);
			return this;
		case XZ:
			this.multiplyX(factor);
			this.multiplyZ(factor);
			return this;
		case YZ:
			this.multiplyY(factor);
			this.multiplyZ(factor);
			return this;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public Vector3i divide(Vector3i vector) {
		return this.divide(vector.x(), vector.y(), vector.z());
	}

	@Override
	public Vector3i divide(double x, double y, double z) {
		this.divideX(x);
		this.divideY(y);
		this.divideZ(z);
		return this;
	}

	public Vector3i floorDivide(Vector3i vector) {
		return this.floorDivide(vector.x(), vector.y(), vector.z());
	}

	public Vector3i floorDivide(double denominator) {
		this.floorDivide(denominator, denominator, denominator);
		return this;
	}

	public Vector3i floorDivide(double x, double y, double z) {
		this.floorDivideX(x);
		this.floorDivideY(y);
		this.floorDivideZ(z);
		return this;
	}

	public Vector3i ceilDivide(Vector3i vector) {
		return this.ceilDivide(vector.x(), vector.y(), vector.z());
	}

	public Vector3i ceilDivide(double denominator) {
		this.ceilDivide(denominator, denominator, denominator);
		return this;
	}

	public Vector3i ceilDivide(double x, double y, double z) {
		this.ceilDivideX(x);
		this.ceilDivideY(y);
		this.ceilDivideZ(z);
		return this;
	}

	public Vector3i modulo(Vector3i vector) {
		return this.modulo(vector.x(), vector.y(), vector.z());
	}

	public Vector3i modulo(double x, double y, double z) {
		this.moduloX(x);
		this.moduloY(y);
		this.moduloZ(z);
		return this;
	}

	public Vector3i min(int min) {
		return this.min(min, min, min);
	}

	public Vector3i min(Vector3i min) {
		return this.min(min.x(), min.y(), min.z());
	}

	public Vector3i min(int x, int y, int z) {
		this.minX(x);
		this.minY(y);
		this.minZ(z);
		return this;
	}

	public Vector3i max(int max) {
		return this.max(max, max, max);
	}

	public Vector3i max(Vector3i max) {
		return this.max(max.x(), max.y(), max.z());
	}

	public Vector3i max(int x, int y, int z) {
		this.maxX(x);
		this.maxY(y);
		this.maxZ(z);
		return this;
	}

	@Override
	public double length() {
		return Math.sqrt(Math.pow(this.x(), 2) + Math.pow(this.y(), 2) + Math.pow(this.z(), 2));
	}

	public int area() {
		return this.x * this.y;
	}

	public int volume() {
		return this.x * this.y * this.z;
	}

	@Override
	public Vector3i normalize() {
		float len = (float) this.length();
		this.set(Math.round(this.x() / len),
				Math.round(this.y() / len),
				Math.round(this.z() / len));
		return this;
	}

	@Override
	public Vector3i reciprocal() {
		this.setX(1 / this.x());
		this.setY(1 / this.y());
		this.setZ(1 / this.z());
		return this;
	}

	@Override
	public Vector3i reciprocal(Axis axis) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("vector is not mutable");
		}
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		switch (axis) {
		case X:
			this.setX(1 / this.x());
			return this;
		case Y:
			this.setY(1 / this.y());
			return this;
		case Z:
			this.setZ(1 / this.z());
			return this;
		case XY:
			this.setX(1 / this.x());
			this.setY(1 / this.y());
			return this;
		case XZ:
			this.setX(1 / this.x());
			this.setZ(1 / this.z());
			return this;
		case YZ:
			this.setY(1 / this.y());
			return this;
		}
		throw new IllegalArgumentException("Axis is invalid");
	}

	@Override
	public Vector3i interpolate(Vector3i dest, float offset) {
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
		return this;
	}

	@Override
	public Vector3i cross(Vector3i other) {
		this.set(this.y() * other.z() - other.y() * this.z(),
				-this.x() * other.z() + other.x() * this.z(),
				this.x() * other.y() - other.x() * this.y());
		return this;
	}

	@Override
	public Vector3i clear() {
		return this.set(0);
	}

	@Override
	public Vector3i clear(Axis axis) {
		return this.set(axis, 0);
	}

	@Override
	public Dimension toDimension() {
		return new Dimension(x, y);
	}

	@Override
	public Point toPoint() {
		return new Point(x, y);
	}

	@Override
	public Vector3i toMutable() {
		return Vector3i.mutable(x, y, z);
	}

	@Override
	public Vector3i toFrozen() {
		if (!this.isMutable()) {
			return this;
		}
		return Vector3i.frozen(x, y, z);
	}

	@Override
	public Vector3i getThis() {
		return this;
	}

	@Override
	public boolean at(Vector3i vector) {
		if (vector == null) {
			return false;
		}
		return this.x() == vector.x() &&
				this.y() == vector.y() &&
				this.z() == vector.z();
	}

	public boolean at(int x, int y, int z) {
		return this.x() == x &&
				this.y() == y &&
				this.z() == z;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Vector3i)) {
			return false;
		}
		final Vector3i other = (Vector3i) obj;
		if (this.isMutable() != other.isMutable()) {
			return false;
		}
		if (this.x() != other.x()) {
			return false;
		}
		if (this.y() != other.y()) {
			return false;
		}
		if (this.z() != other.z()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + (this.isMutable() ? 1 : 0);
		result = 31 * result + this.x();
		result = 31 * result + this.y();
		result = 31 * result + this.z();
		return result;
	}

	@Override
	public String toString() {
		return String.format("Vector3i[%s (%d, %d, %d)]", this.isMutable() ? "mutable" : "frozen", this.x(), this.y(), this.z());
	}

}
