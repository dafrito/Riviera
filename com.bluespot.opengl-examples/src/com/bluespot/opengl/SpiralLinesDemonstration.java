package com.bluespot.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.bluespot.demonstration.Demonstration;

/**
 * Creates a series of lines that form a disc.
 * 
 * @author Aaron Faanes
 * 
 */
public class SpiralLinesDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = 1L;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(SpiralLinesDemonstration.class);
	}

	@Override
	protected void render(final GL2 gl, GLAutoDrawable drawable) {
		gl.glBegin(GL.GL_LINES);
		for (float angle = 0; angle < Math.PI; angle += Math.PI / 20.0f) {
			float x = 50.0f * (float) Math.sin(angle);
			float y = 50.0f * (float) Math.cos(angle);
			gl.glVertex2f(x, y);

			x = 50.0f * (float) Math.sin(Math.PI + angle);
			y = 50.0f * (float) Math.cos(Math.PI + angle);
			gl.glVertex2f(x, y);
		}
		gl.glEnd();
	}
}
