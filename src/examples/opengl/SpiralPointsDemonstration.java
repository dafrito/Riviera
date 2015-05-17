package examples.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import demonstration.Demonstration;

/**
 * Creates a series of points aligned along a vertical spiral.
 * 
 * @author Aaron Faanes
 * 
 */
public class SpiralPointsDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = 5936974640074960409L;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(SpiralPointsDemonstration.class);
	}

	@Override
	protected void render(final GL2 gl, GLAutoDrawable drawable) {
		gl.glBegin(GL.GL_POINTS);
		float z = -50.0f;
		for (float angle = 0; angle <= 6 * Math.PI; angle += 0.1f) {
			final float x = 50.0f * (float) Math.sin(angle);
			final float y = 50.0f * (float) Math.cos(angle);

			gl.glVertex3f(x, y, z);
			z += 0.5f;
		}
		gl.glEnd();
	}

}
