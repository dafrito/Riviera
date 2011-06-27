/**
 * 
 */
package com.bluespot.examples.opengl;

import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.bluespot.demonstration.Demonstration;
import com.bluespot.geom.vectors.Vector3f;

/**
 * A demonstration showing a small number of randomly sized and positioned
 * quads.
 * 
 * @author Aaron Faanes
 * 
 */
public class QuadDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = 1L;

	private static final int NUM_QUADS = 60;

	private final Random random = new Random();

	public static void main(String[] args) {
		Demonstration.launchWrapped(QuadDemonstration.class);
	}

	public QuadDemonstration() {
		for (int i = 0; i < NUM_QUADS; i++) {
			vertices[i] = Vector3f.frozen(
					-100f + random.nextFloat() * 200,
					-100f + random.nextFloat() * 200,
					-10f + random.nextFloat() * 10f);
			colors[i] = Vector3f.frozen(
					random.nextFloat(), random.nextFloat(), 1);
			sizes[i] = random.nextFloat() * 60;
		}
	}

	private Vector3f[] vertices = new Vector3f[NUM_QUADS];
	private float[] sizes = new float[NUM_QUADS];
	private Vector3f[] colors = new Vector3f[NUM_QUADS];

	@Override
	protected void render(GL2 gl, GLAutoDrawable drawable) {
		gl.glBegin(GL2.GL_QUADS);
		for (int i = 0; i < NUM_QUADS; i++) {
			float x = vertices[i].getX();
			float y = vertices[i].getY();
			float z = vertices[i].getZ();
			float size = sizes[i];
			gl.glColor3f(colors[i].getX(), colors[i].getY(), colors[i].getZ());
			gl.glVertex3f(x, y, z);
			gl.glVertex3f(x + size, y, z);
			gl.glVertex3f(x + size, y + size, z);
			gl.glVertex3f(x, y + size, z);
		}
		gl.glEnd();
	}
}
