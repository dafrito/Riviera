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
 * Demonstrates the use of the scissor test. {@link GL#glScissor} creates a clip
 * rectangle that can be used to optimize rendering. In this case, we use the
 * simple clear operation, but since we're using scissor tests, it only clears
 * the portion allowed by the test.
 * 
 * @author Aaron Faanes
 * 
 */
public class ScissorDemonstration extends AbstractGLDemonstration {

	private static final long serialVersionUID = 1L;

	private final List<Integer> nums = new ArrayList<Integer>();
	private final List<Float> colors = new ArrayList<Float>();

	/**
	 * The number of cleared scissored rectangles generated and used in this
	 * demonstration. It must be a positive number, and preferably not large
	 * since I don't know how this will work if we "overflow" in scissors.
	 */
	public static int NUM_SCISSORS = 20;

	/**
	 * Launches this demonstration.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launchWrapped(ScissorDemonstration.class);
	}

	@Override
	public void init(final GLAutoDrawable drawable) {
		final Random random = new Random();
		this.colors.add(0.0f);
		this.colors.add(0.0f);
		this.colors.add(0.0f);
		for (int i = 0; i < NUM_SCISSORS; i++) {
			this.nums.add(random.nextInt(100));
			this.colors.add(random.nextFloat());
			this.colors.add(random.nextFloat());
			this.colors.add(random.nextFloat());
		}
	}

	@Override
	protected void render(final GL2 gl, GLAutoDrawable drawable) {
		final int width = this.getWidth();
		final int height = this.getHeight();
		gl.glEnable(GL.GL_SCISSOR_TEST);
		int encroachment = 0;
		final Iterator<Integer> iter = this.nums.iterator();
		final Iterator<Float> colorIter = this.colors.iterator();
		for (int i = 0; i < NUM_SCISSORS; i++) {
			gl.glScissor(encroachment, encroachment, width - encroachment, height - encroachment);
			gl.glClearColor(colorIter.next(), colorIter.next(), colorIter.next(), 1.0f);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
			encroachment += iter.next();
		}
	}
}
