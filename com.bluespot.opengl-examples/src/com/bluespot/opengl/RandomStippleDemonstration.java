package com.bluespot.opengl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.bluespot.demonstration.Demonstration;

/**
 * Draws a series of horizontal lines on the screen. The lines vary by the
 * amount of "stippling" used on them. In this case, the lines are randomly
 * stippled, producing an interesting effect.
 * 
 * @author Aaron Faanes
 * 
 */
public class RandomStippleDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = -2867155566784812813L;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(RandomStippleDemonstration.class);
	}

	private final Random random = new Random();

	private final List<Short> stipples = new ArrayList<Short>();
	private final List<Float> colors = new ArrayList<Float>();

	@Override
	public void init(final GLAutoDrawable drawable) {
		super.init(drawable);
		for (float y = -90.0f; y < 90.0f; y += 20.0f) {
			/*
			 * We don't actually use the y, just ensure that we produce enough
			 * random values
			 */
			this.stipples.add((short) this.random.nextInt());
			// This is a dirty hack-filled way to generate random lines.
			this.colors.add(this.random.nextFloat());
			this.colors.add(this.random.nextFloat());
			this.colors.add(this.random.nextFloat());
			this.colors.add(this.random.nextFloat());
			this.colors.add(this.random.nextFloat());
			this.colors.add(this.random.nextFloat());
		}
	}

	@Override
	protected void render(final GL2 gl) {
		gl.glEnable(GL2.GL_LINE_STIPPLE);

		final Iterator<Short> iter = this.stipples.iterator();
		final Iterator<Float> colorIter = this.colors.iterator();
		for (float y = -90.0f; y < 90.0f; y += 20.0f) {
			assert iter.hasNext();
			gl.glLineWidth(2);
			gl.glLineStipple(5, iter.next());
			// pattern.
			gl.glBegin(GL.GL_LINES);
			gl.glColor3f(colorIter.next(), colorIter.next(), colorIter.next());
			gl.glVertex2f(-80.0f, y);
			gl.glColor3f(colorIter.next(), colorIter.next(), colorIter.next());
			gl.glVertex2f(80.0f, y);
			gl.glEnd();
		}
	}
}
