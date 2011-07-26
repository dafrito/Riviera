package com.bluespot.opengl;

import javax.media.opengl.GL2;

import com.bluespot.geom.vectors.Vector3f;

public class Frame {
	private final Vector3f location;
	private final Vector3f up;
	private final Vector3f forward;

	/**
	 * Constructs a {@link Frame} positioned at the origin and facing forward.
	 */
	public Frame() {
		this(Vector3f.origin());
	}

	/**
	 * Constructs a {@link Frame} positioned at the specified location and
	 * facing forward.
	 * 
	 * @param location
	 *            the location of this frame
	 */
	public Frame(final Vector3f location) {
		this(location, Vector3f.up(), Vector3f.forward());
	}

	/**
	 * Constructs a {@link Frame} positioned at the specified location and
	 * oriented using the specified vectors.
	 * 
	 * @param location
	 *            the location of this frame
	 * @param up
	 *            the vector representing the y-axis for this frame
	 * @param forward
	 *            the vector representing the z-axis for this frame
	 * @throws NullPointerException
	 *             if any argument is null
	 */
	public Frame(final Vector3f location, final Vector3f up, final Vector3f forward) {
		if (location == null) {
			throw new NullPointerException("location is null");
		}
		if (up == null) {
			throw new NullPointerException("up is null");
		}
		if (forward == null) {
			throw new NullPointerException("forward is null");
		}
		this.location = location.toMutable();
		this.up = up.toFrozen();
		this.forward = forward.toFrozen();
	}

	public void translate(final Vector3f vector) {
		this.location.add(vector);
	}

	public void moveForward() {
		this.translate(this.getForward());
	}

	/**
	 * Returns the {@link Vector3f} that represents the location of this frame.
	 * 
	 * @return the {@code Vector3f} that represents the location of this frame
	 */
	public Vector3f getLocation() {
		return this.location;
	}

	/**
	 * Returns the {@link Vector3f} that represents the Y-axis for this frame.
	 * 
	 * @return the {@code Vector3f} that represents the Y-axis for this frame
	 */
	public Vector3f getUp() {
		return this.up;
	}

	/**
	 * Returns the {@link Vector3f} that represents the Z-axis for this frame.
	 * 
	 * @return the {@code Vector3f} that represents the Z-axis for this frame
	 */
	public Vector3f getForward() {
		return this.forward;
	}

	private double[] matrix;

	private double[] getMatrix() {
		if (this.matrix == null) {
			final double[] calculated = new double[16];
			final Vector3f xAxis = this.getUp().toMutable().cross(this.getForward());

			// X axis
			calculated[0] = xAxis.x();
			calculated[1] = xAxis.y();
			calculated[2] = xAxis.z();
			calculated[3] = 0.0f;

			// Y axis
			calculated[4] = this.getUp().x();
			calculated[5] = this.getUp().y();
			calculated[6] = this.getUp().z();
			calculated[7] = 0.0f;

			// Z axis
			calculated[8] = this.getForward().x();
			calculated[9] = this.getForward().y();
			calculated[10] = this.getForward().z();
			calculated[11] = 0.0f;

			// Translation
			calculated[12] = this.getLocation().x();
			calculated[13] = this.getLocation().y();
			calculated[14] = this.getLocation().z();
			calculated[15] = 1.0f;

			this.matrix = calculated;
		}
		return this.matrix;
	}

	public void transform(final GL2 gl) {
		gl.glMultMatrixd(this.getMatrix(), 0);
	}
}
