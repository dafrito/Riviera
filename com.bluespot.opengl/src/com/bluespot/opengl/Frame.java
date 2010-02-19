package com.bluespot.opengl;

import javax.media.opengl.GL;

import com.bluespot.geom.Point3D;
import com.bluespot.geom.Vector;

public class Frame {
    private final Point3D.Float location;
    private final Vector up;
    private final Vector forward;

    /**
     * Constructs a {@link Frame} positioned at the origin and facing forward.
     */
    public Frame() {
        this(Point3D.Float.ORIGIN);
    }

    /**
     * Constructs a {@link Frame} positioned at the specified location and
     * facing forward.
     * 
     * @param location
     *            the location of this frame
     */
    public Frame(final Point3D.Float location) {
        this(location, Vector.UP, Vector.FORWARD);
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
    public Frame(final Point3D.Float location, final Vector up, final Vector forward) {
        if (location == null) {
            throw new NullPointerException("location is null");
        }
        if (up == null) {
            throw new NullPointerException("up is null");
        }
        if (forward == null) {
            throw new NullPointerException("forward is null");
        }
        this.location = location;
        this.up = up;
        this.forward = forward;
    }

    public Frame translate(final Vector vector) {
        return new Frame(this.location.add(vector), this.up, this.forward);
    }

    public Frame moveForward() {
        return this.translate(this.getForward());
    }

    /**
     * Returns the {@link Float Point3D.Float} that represents the location of
     * this frame.
     * 
     * @return the {@code Point3D.Float} that represents the location of this
     *         frame
     */
    public Point3D.Float getLocation() {
        return this.location;
    }

    /**
     * Returns the {@link Vector} that represents the Y-axis for this frame.
     * 
     * @return the {@code Vector} that represents the Y-axis for this frame
     */
    public Vector getUp() {
        return this.up;
    }

    /**
     * Returns the {@link Vector} that represents the Z-axis for this frame.
     * 
     * @return the {@code Vector} that represents the Z-axis for this frame
     */
    public Vector getForward() {
        return this.forward;
    }

    private double[] matrix;

    private double[] getMatrix() {
        if (this.matrix == null) {
            final double[] calculated = new double[16];
            final Vector xAxis = this.getUp().crossProduct(this.getForward());

            // X axis
            calculated[0] = xAxis.getX();
            calculated[1] = xAxis.getY();
            calculated[2] = xAxis.getZ();
            calculated[3] = 0.0f;

            // Y axis
            calculated[4] = this.getUp().getX();
            calculated[5] = this.getUp().getY();
            calculated[6] = this.getUp().getZ();
            calculated[7] = 0.0f;

            // Z axis
            calculated[8] = this.getForward().getX();
            calculated[9] = this.getForward().getY();
            calculated[10] = this.getForward().getZ();
            calculated[11] = 0.0f;

            // Translation
            calculated[12] = this.getLocation().getX();
            calculated[13] = this.getLocation().getY();
            calculated[14] = this.getLocation().getZ();
            calculated[15] = 1.0f;

            this.matrix = calculated;
        }
        return this.matrix;
    }

    public void transform(final GL gl) {
        gl.glMultMatrixd(this.getMatrix(), 0);
    }
}
