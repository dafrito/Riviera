package com.bluespot.opengl;

import javax.media.opengl.GL;

import com.bluespot.demonstration.Demonstration;

/**
 * Demonstrates stippling used in combination with the spiral vertices earlier.
 * This example shows how we can leverage the generic nature of vertices to
 * create an endless amount of effects. It also shows how stippling can be used
 * to create an effect that is similar to {@link StippleDemonstration}.
 * 
 * @author Aaron Faanes
 * 
 */
public class StippledSpiralDemonstration extends AbstractGLDemonstration {

    private static final long serialVersionUID = 1L;

    /**
     * Launches this demonstration.
     * 
     * @param args
     *            unused
     */
    public static void main(final String[] args) {
        Demonstration.launchWrapped(StippledSpiralDemonstration.class);
    }

    @Override
    protected void render(final GL gl) {
        gl.glEnable(GL.GL_LINE_STIPPLE);
        gl.glLineStipple(1, (short) 0x5555);
        gl.glLineWidth(4);
        gl.glBegin(GL.GL_LINE_STRIP);
        float z = -50.0f;
        int i = 0;
        for (float angle = 0; angle <= 6 * Math.PI; angle += 0.1f) {
            final float x = 50.0f * (float) Math.sin(angle);
            final float y = 50.0f * (float) Math.cos(angle);
            if (i++ % 2 == 0) {
                gl.glColor3f(1, 0, 0);
            } else {
                gl.glColor3f(1, 1, 1);
            }
            gl.glVertex3f(x, y, z);
            z += 0.5f;
        }
        gl.glEnd();
    }

}
