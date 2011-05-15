/**
 * 
 */
package com.bluespot.geom.vectors;

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

	private static final Vector3i ORIGIN = new Vector3i(false, 0, 0, 0);

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
		return Vector3i.mutable(v, v, v);
	}

	/**
	 * Returns a frozen vector at the origin.
	 * 
	 * @return a frozen vector at the origin.
	 */
	public static Vector3i origin() {
		return ORIGIN;
	}

	public static Vector3i mutable(int x, int y, int z) {
		return new Vector3i(true, x, y, z);
	}

	public static Vector3i frozen(int x, int y, int z) {
		return new Vector3i(false, x, y, z);
	}

	public static Vector3i mutable(Vector3f vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3i(true, (int) vector.getX(), (int) vector.getY(), (int) vector.getZ());
	}

	public static Vector3i frozen(Vector3f vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3i(false, (int) vector.getX(), (int) vector.getY(), (int) vector.getZ());
	}

	public static Vector3i mutable(Vector3d vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3i(true, (int) vector.getX(), (int) vector.getY(), (int) vector.getZ());
	}

	public static Vector3i frozen(Vector3d vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
		}
		return new Vector3i(false, (int) vector.getX(), (int) vector.getY(), (int) vector.getZ());
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		int old = this.x;
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
	public Vector3i withX(int value) {
		Vector3i result = this.toMutable();
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
	public int addX(int offset) {
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
	public Vector3i addedX(int offset) {
		Vector3i vector = this.toMutable();
		vector.addX(offset);
		return vector;
	}

	/**
	 * Multiply the specified x value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old x value
	 */
	public double multiplyX(double factor) {
		return this.setX((int) Math.round(this.getX() * factor));
	}

	/**
	 * Return a mutable copy of this vector, with a multiplied x value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable vector at {@code (x * offset, y, z)}
	 */
	public Vector3i mulipliedX(double factor) {
		Vector3i vector = this.toMutable();
		vector.multiplyX(factor);
		return vector;
	}

	/**
	 * Returns the y-coordinate of this vector.
	 * 
	 * @return the y-coordinate of this vector
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		int old = this.y;
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
	public Vector3i withY(int value) {
		Vector3i result = this.toMutable();
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
	public int addY(int offset) {
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
	public Vector3i addedY(int offset) {
		Vector3i vector = this.toMutable();
		vector.addY(offset);
		return vector;
	}

	/**
	 * Multiply the specified y value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old y value
	 */
	public double multiplyY(double factor) {
		return this.setY((int) Math.round(this.getY() * factor));
	}

	/**
	 * Return a mutable copy of this vector, with a multiplied y value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable vector at {@code (x, y * offset, z)}
	 */
	public Vector3i mulipliedY(double factor) {
		Vector3i vector = this.toMutable();
		vector.multiplyY(factor);
		return vector;
	}

	/**
	 * Returns the z-coordinate of this vector.
	 * 
	 * @return the z-coordinate of this vector
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
			throw new UnsupportedOperationException("vector is not mutable");
		}
		int old = this.z;
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
	public Vector3i withZ(int value) {
		Vector3i result = this.toMutable();
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
	public int addZ(int offset) {
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
	public Vector3i addedZ(int offset) {
		Vector3i vector = this.toMutable();
		vector.addZ(offset);
		return vector;
	}

	/**
	 * Multiply the specified z value of this vector.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return the old z value
	 */
	public double multiplyZ(double factor) {
		return this.setZ((int) Math.round(this.getZ() * factor));
	}

	/**
	 * Return a mutable copy of this vector, with a multiplied z value.
	 * 
	 * @param factor
	 *            the factor of multiplication
	 * @return a mutable vector at {@code (x, y, z * offset)}
	 */
	public Vector3i mulipliedZ(double factor) {
		Vector3i vector = this.toMutable();
		vector.multiplyZ(factor);
		return vector;
	}

	@Override
	public void set(Vector3i vector) {
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
	public void set(int value) {
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
	 */
	public void set(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	@Override
	public void set(Axis axis, Vector3i vector) {
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
	public void set(Axis axis, int value) {
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
	public Vector3i with(Axis axis, int value) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		Vector3i result = this.toMutable();
		result.set(axis, value);
		return result;
	}

	@Override
	public void add(Vector3i vector) {
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
	public void add(int value) {
		this.addX(value);
		this.addY(value);
		this.addZ(value);
	}

	@Override
	public void add(Axis axis, Vector3i vector) {
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
	public void add(Axis axis, int value) {
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
	public Vector3i added(int value) {
		Vector3i result = this.toMutable();
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
	public Vector3i added(Axis axis, int value) {
		if (axis == null) {
			throw new NullPointerException("Axis must not be null");
		}
		Vector3i result = this.toMutable();
		result.add(axis, value);
		return result;
	}

	@Override
	public void multiply(Vector3i vector) {
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
	public void multiply(Axis axis, Vector3i vector) {
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
	public void interpolate(Vector3i dest, float offset) {
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
	public boolean at(Vector3i vector) {
		if (vector == null) {
			throw new NullPointerException("vector must not be null");
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
		if (!(obj instanceof Vector3i)) {
			return false;
		}
		final Vector3i other = (Vector3i) obj;
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
		return String.format("Vector3i[%s (%d, %d, %d)]", this.isMutable(), this.getX(), this.getY(), this.getZ());
	}

}
