package com.bluespot.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.bluespot.demonstration.Demonstration;

/**
 * Draws a series of horizontal lines on the screen. The lines vary by the
 * amount of <em>stippling</em> used on them. Line stippling is what creates
 * dotted and dashed lines. In OpenGL, stippling is implemented by using a
 * factor and a pattern. The pattern is a {@code short} that describes when the
 * line is shown. The factor defines the scale of the pattern.
 * <p>
 * In this demonstration, the factor is increased, causing the pattern to become
 * larger and more pronounced.
 * <p>
 * It's important to note the ordering of these calls. Stippling must be enabled
 * and the stipple must be set through {@link GL#glLineStipple(int, short)}
 * before vertices are added,
 * 
 * @author Aaron Faanes
 * 
 */
public class StippleDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = 1L;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(StippleDemonstration.class);
	}

	@Override
	protected void render(final GL2 gl) {
		gl.glEnable(GL2.GL_LINE_STIPPLE);

		int factor = 1;
		for (float y = -90.0f; y < 90.0f; y += 20.0f) {
			gl.glLineStipple(factor, (short) 0x5555); // 5 in binary is a 1001
			// pattern.
			gl.glBegin(GL.GL_LINES);
			final float percentComplete = (y + 90) / 180;
			gl.glColor3f(.3f + percentComplete / 3.0f, 1.0f - percentComplete, percentComplete);
			gl.glVertex2f(-80.0f, y);
			gl.glVertex2f(80.0f, y);
			gl.glEnd();
			factor += 2;
		}
	}
}
