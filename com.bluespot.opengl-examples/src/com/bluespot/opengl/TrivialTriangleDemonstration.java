package com.bluespot.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.bluespot.demonstration.Demonstration;

/**
 * Draws two triangles on the screen. These triangles are "front-facing", which
 * isn't relevant here but will be relevant in the future. The facing of the
 * triangles is determined by the order of vertices. This combination of
 * ordering and facing determination is called <em>winding</em>. Clockwise
 * vertices are, by OpenGL convention, facing <em>away</em> from the viewer.
 * Counter-clockwise vertices, on the other hand, are facing outward towards the
 * viewer.
 * <p>
 * The order of vertices should be consistent in a scene, and unless you're
 * doing something weird, they should be ordered such that outward faces are
 * ordered counter-clockwise.
 * <p>
 * To reverse the facing behavior of OpenGL, use {@link GL#glFrontFace(int)}.
 * 
 * @author Aaron Faanes
 * 
 */
public class TrivialTriangleDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = 1627266097175449717L;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(TrivialTriangleDemonstration.class);
	}

	@Override
	protected void render(final GL2 gl, GLAutoDrawable drawable) {
		gl.glBegin(GL.GL_TRIANGLES);

		gl.glVertex2f(0, 0);
		gl.glVertex2f(25, 25);
		gl.glVertex2f(50, 0);

		gl.glVertex2f(-50, 0);
		gl.glVertex2f(-75, 50);
		gl.glVertex2f(-25, 0);

		gl.glEnd();
	}

}
