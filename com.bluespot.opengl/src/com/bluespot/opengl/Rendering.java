package com.bluespot.opengl;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import com.bluespot.geom.Dimension3D;
import com.bluespot.geom.Point3D;

/**
 * A collection of utility methods simplifying common OpenGL operations.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Rendering {

    /**
     * Represents the matrix types available in OpenGL.
     * 
     * @author Aaron Faanes
     * 
     */
    public static enum Matrix {
        /**
         * Represents the modelview matrix stack.
         */
        MODELVIEW(GL.GL_MODELVIEW),

        /**
         * Represents the projection matrix stack.
         */
        PROJECTION(GL.GL_PROJECTION),

        /**
         * Represents the texture matrix stack.
         */
        TEXTURE(GL.GL_TEXTURE);

        private final int matrixConstant;

        private Matrix(final int matrixConstant) {
            this.matrixConstant = matrixConstant;
        }

        /**
         * Returns OpenGL's enum constant for this matrix type.
         * 
         * @return OpenGL's enum constant for this matrix type
         */
        public int getConstant() {
            return this.matrixConstant;
        }

        /**
         * Sets the specified OpenGL's matrix mode to this matrix type.
         * 
         * @param gl
         *            the OpenGL context that is affected by this operation
         */
        public void activate(final GL gl) {
            gl.glMatrixMode(this.getConstant());
        }

    }

    /**
     * Represents the set of buffers available in OpenGL.
     * 
     * @author Aaron Faanes
     * 
     */
    public static enum Buffer {
        /**
         * The buffer used for color writing.
         */
        COLOR(GL.GL_COLOR_BUFFER_BIT),

        /**
         * The buffer used for depth testing.
         */
        DEPTH(GL.GL_DEPTH_BUFFER_BIT),

        /**
         * The buffer used for stenciling operations.
         */
        STENCIL(GL.GL_STENCIL_BUFFER_BIT),

        /**
         * The accumulation buffer. This buffer is used in compositing
         * operations.
         */
        ACCUMULATION(GL.GL_ACCUM_BUFFER_BIT);

        private int bufferConstant;

        private Buffer(final int bufferConstant) {
            this.bufferConstant = bufferConstant;
        }

        /**
         * Returns OpenGL's enum constant for this buffer type.
         * 
         * @return OpenGL's enum constant for this buffer type
         */
        public int getConstant() {
            return this.bufferConstant;
        }

        /**
         * Clears this buffer.
         * 
         * @param gl
         *            the context used during this operation
         */
        public void clear(final GL gl) {
            gl.glClear(this.getConstant());
        }

    }

    private static final GLU glu = new GLU();

    private Rendering() {
        // Suppress default constructor to ensure non-instantiability.
        throw new AssertionError("Instantiation not allowed");
    }

    /**
     * Returns the OpenGL utility context used by this class.
     * 
     * @return the OpenGL utility context used by this class
     */
    public static GLU getGLU() {
        return Rendering.glu;
    }

    public static void applyCameraTransform(final Frame frame) {
        Rendering.getGLU().gluLookAt(frame.getLocation().getX(), frame.getLocation().getY(),
                frame.getLocation().getZ(), frame.getForward().getX(), frame.getForward().getY(),
                frame.getForward().getZ(), frame.getUp().getX(), frame.getUp().getY(), frame.getUp().getZ());
    }

    public static void setClearColor(final GL gl, final Color color) {
        gl.glClearColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
                color.getAlpha() / 255.0f);
    }

    /**
     * 
     * @param gl
     * @param point
     * @param view
     */
    public static void setOrthogonalMatrix(final GL gl, final Point3D.Double point, final Dimension3D.Double view) {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

    }
}
