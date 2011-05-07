package com.bluespot.opengl;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

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

	/**
	 * Helper method that uses
	 * {@link GLU#gluLookAt(double, double, double, double, double, double, double, double, double)
	 * GLU.gluLookAt(...)} in combination with a {@link Frame}.
	 * 
	 * @param frame
	 *            the frame that will be used to transform the view matrix
	 */
	public static void applyCameraTransform(final Frame frame) {
		Rendering.getGLU().gluLookAt(frame.getLocation().getX(), frame.getLocation().getY(),
				frame.getLocation().getZ(), frame.getForward().getX(), frame.getForward().getY(),
				frame.getForward().getZ(), frame.getUp().getX(), frame.getUp().getY(), frame.getUp().getZ());
	}

	/**
	 * Sets GL's clear color to the specified {@link Color}.
	 * 
	 * @param gl
	 *            the {@link GL} context used to set the clear color
	 * @param color
	 *            the color that will become the new clear color
	 */
	public static void setClearColor(final GL gl, final Color color) {
		gl.glClearColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
				color.getAlpha() / 255.0f);
	}

	/**
	 * Sets the specified OpenGL context to use an orthogonal matrix. The size
	 * of the projection is determined by the specified rectangle.
	 * 
	 * @param gl
	 *            the OpenGL context that is modified by this operation
	 * @param view
	 *            the rectangle that encompasses the new orthogonal view
	 */
	public static void setOrthogonalMatrix(final GL gl, final Rectangle2D.Double view) {
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		getGLU().gluOrtho2D(view.x, view.x + view.width, view.y, view.y + view.height);
	}
}
