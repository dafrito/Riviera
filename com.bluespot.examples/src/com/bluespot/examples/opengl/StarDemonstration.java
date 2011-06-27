package com.bluespot.examples.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.bluespot.demonstration.Demonstration;

/**
 * Demonstrates a four-cornered star drawn using a wireframe.
 * {@link GL#glEdgeFlag} is used to hide borders between polygons that shouldn't
 * be shown.
 * 
 * @author Aaron Faanes
 * 
 * @see #drawInsignificantEdges
 */
public class StarDemonstration extends AbstractGLDemonstration {

	/**
	 * Whether to draw the lines in insignificant edges. There's no real magic
	 * here; edges that are insignificant are explicitly set to {@code false}
	 * with this flag.
	 */
	public final boolean drawInsignificantEdges = false;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(StarDemonstration.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void render(final GL2 gl, GLAutoDrawable drawable) {

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBegin(GL.GL_TRIANGLES);

		gl.glEdgeFlag(this.drawInsignificantEdges);
		gl.glVertex2f(-20, 0);
		gl.glEdgeFlag(true);
		gl.glVertex2f(20, 0);
		gl.glVertex2f(0, 40);

		gl.glVertex2f(-20, 0);
		gl.glVertex2f(-60, -20);
		gl.glEdgeFlag(this.drawInsignificantEdges);
		gl.glVertex2f(-20, -40);
		gl.glEdgeFlag(true);

		gl.glVertex2f(-20, -40);
		gl.glVertex2f(0, -80);
		gl.glEdgeFlag(this.drawInsignificantEdges);
		gl.glVertex2f(20, -40);
		gl.glEdgeFlag(true);

		gl.glVertex2f(20, -40);
		gl.glVertex2f(60, -20);
		gl.glEdgeFlag(this.drawInsignificantEdges);
		gl.glVertex2f(20, 0);
		gl.glEdgeFlag(true);

		gl.glEdgeFlag(this.drawInsignificantEdges);
		gl.glVertex2f(-20, 0);
		gl.glVertex2f(-20, -40);
		gl.glVertex2f(20, 0);

		gl.glVertex2f(-20, -40);
		gl.glVertex2f(20, -40);
		gl.glVertex2f(20, 0);
		gl.glEdgeFlag(true);

		gl.glEnd();

	}
}
