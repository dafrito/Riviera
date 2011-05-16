/**
 * 
 */
package com.bluespot.opengl;

import java.awt.Font;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.bluespot.demonstration.Demonstration;
import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * @author Aaron Faanes
 * 
 */
public class TextDemo extends AbstractGLDemonstration {

	private static final long serialVersionUID = 1L;
	private TextRenderer renderer;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(TextDemo.class);
	}

	@Override
	public void init(final GLAutoDrawable drawable) {
		super.init(drawable);
		if (renderer != null) {
			renderer.dispose();
		}
		renderer = new TextRenderer(new Font("Inconsolata", Font.PLAIN, 18), true, true);
	}

	@Override
	protected void render(GL2 gl, GLAutoDrawable drawable) {
		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		renderer.draw("No time.", 25, 25);
		renderer.endRendering();
	}

}
