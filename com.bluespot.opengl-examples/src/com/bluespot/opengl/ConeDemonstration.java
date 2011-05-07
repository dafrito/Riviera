package com.bluespot.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.bluespot.demonstration.Demonstration;

/**
 * Draws a intentionally incorrectly ordered cone that demonstrates some
 * interesting results using depth and back-face culling. Specifically, this
 * demonstrates two separate features of OpenGL:
 * <ul>
 * <li>{@link GL#GL_DEPTH_TEST} which, if enabled, will properly order vertices
 * based on z-order. If this is disabled, then vertices are shown in the order
 * they're rendered. Disabling this effect in this demonstration will cause the
 * bottom to permanently be on top, producing a disorienting "wobble" effect. If
 * this is enabled, and back-face culling is disabled, the cone will appear
 * correctly.
 * <li>{@link GL#GL_CULL_FACE} culls all back-facing polygons. In this example,
 * this will cause a significant amount of the cone to become invisible during
 * rotation because of the winding of our vertices. Depth testing being enabled
 * will affect what areas are improperly invisible but will not correct the
 * underlying winding problem.
 * <li>{@link GL#GL_BACK} determines how back-facing polygons are drawn. This
 * option ostensibly has no effect if back-face culling is enabled, since
 * back-facing polygons are never drawn. It can be set to fill the polygons or
 * show the polygons as a wireframe.
 * </ul>
 * In order to see the cone drawn correctly, all boolean values must be set to
 * {@code true}.
 * 
 * @author Aaron Faanes
 * 
 */
public class ConeDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = 1L;

	/**
	 * Sets whether depth testing is used when rendering pixels. If {@code true}
	 * , the polygon will appear correctly. If {@code false}, polygons that are
	 * drawn later in the rendering process will occlude polygons drawn earlier.
	 * In this demonstration, this means that the bottom of the cone will always
	 * occlude the cone portion.
	 */
	public final boolean useDepthTesting = true;

	/**
	 * Sets whether back-facing polygons are drawn. If this is {@code true} and
	 * {@link #correctWindingOrder} is {@code false}, the cone will appear
	 * incorrectly. This is intentional to demonstrate how occlusion is done.
	 */
	public final boolean backFaceCulling = true;

	/**
	 * Draws back-facing surfaces as a wireframe. Otherwise, they're drawn as
	 * filled surfaces. This has no effect if {@link #backFaceCulling} is
	 * {@code true}.
	 */
	public final boolean drawBacksAsWireFrame = true;

	/**
	 * Corrects the winding order so that the cone can be drawn properly. If
	 * {@link #backFaceCulling} is {@code true}, this must be also be {@code
	 * true} for the cone to appear correctly.
	 */
	public final boolean correctWindingOrder = true;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(ConeDemonstration.class);
	}

	@Override
	public void init(final GLAutoDrawable drawable) {
		super.init(drawable);
		final GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_FLAT);
	}

	@Override
	protected void render(final GL gl) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glFrontFace(GL.GL_CW);

		if (this.useDepthTesting) {
			gl.glEnable(GL.GL_DEPTH_TEST);
		} else {
			gl.glDisable(GL.GL_DEPTH_TEST);
		}

		if (this.backFaceCulling) {
			gl.glEnable(GL.GL_CULL_FACE);
		} else {
			gl.glDisable(GL.GL_CULL_FACE);
			if (this.drawBacksAsWireFrame) {
				gl.glPolygonMode(GL.GL_BACK, GL.GL_LINE);
			} else {
				gl.glPolygonMode(GL.GL_BACK, GL.GL_FILL);
			}
		}

		int pivot = 1;
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glVertex3f(0, 0, 75);
		for (int i = 0; i <= 16; i++) {
			final float angle = i * (float) Math.PI / 8.0f;
			final float x = 50.0f * (float) Math.sin(angle);
			final float y = 50.0f * (float) Math.cos(angle);

			if (pivot % 2 == 0) {
				gl.glColor3f(0, 1, 0);
			} else {
				gl.glColor3f(1, 0, 0);
			}
			pivot++;

			gl.glVertex2f(x, y);
		}
		gl.glEnd();

		if (this.correctWindingOrder) {
			gl.glFrontFace(GL.GL_CCW);
		}
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glVertex2f(0, 0);
		for (int i = 0; i <= 16; i++) {
			final float angle = i * (float) Math.PI / 8.0f;
			final float x = 50.0f * (float) Math.sin(angle);
			final float y = 50.0f * (float) Math.cos(angle);

			if (pivot % 2 == 0) {
				gl.glColor3f(0, 1, 0);
			} else {
				gl.glColor3f(1, 0, 0);
			}
			pivot++;

			gl.glVertex2f(x, y);
		}
		gl.glEnd();

	}
}
