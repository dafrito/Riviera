package com.bluespot.opengl;

import javax.media.opengl.GL2;

import com.bluespot.demonstration.Demonstration;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Shows very basic transformations.
 * 
 * @author Aaron Faanes
 * 
 */
public class TransformDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = 1L;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(TransformDemonstration.class);
	}

	@Override
	protected void render(final GL2 gl) {
		final GLUT glut = new GLUT();
		gl.glRotatef(45, 1, 1, 1);
		glut.glutWireCube(30.0f);
		gl.glScalef(2, 2, 2);
		glut.glutWireCube(30.0f);
		gl.glTranslatef(0, 40, 0);
		glut.glutWireCube(30.0f);
	}

}
