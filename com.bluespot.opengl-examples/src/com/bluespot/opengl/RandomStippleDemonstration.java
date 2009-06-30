package com.bluespot.opengl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.media.opengl.GL;
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

    @Override
    public void init(final GLAutoDrawable drawable) {
        super.init(drawable);
        for (float y = -90.0f; y < 90.0f; y += 20.0f) {
            /*
             * We don't actually use the y, just ensure that we produce enough
             * random values
             */
            this.stipples.add((short) this.random.nextInt());
        }
    }

    @Override
    protected void render(final GL gl) {
        gl.glEnable(GL.GL_LINE_STIPPLE);

        final Iterator<Short> iter = this.stipples.iterator();
        for (float y = -90.0f; y < 90.0f; y += 20.0f) {
            assert iter.hasNext();
            gl.glLineWidth(2);
            gl.glLineStipple(5, iter.next());
            // pattern.
            gl.glBegin(GL.GL_LINES);
            gl.glVertex2f(-80.0f, y);
            gl.glVertex2f(80.0f, y);
            gl.glEnd();
        }
    }
}
