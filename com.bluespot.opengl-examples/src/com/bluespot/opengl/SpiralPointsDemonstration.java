package com.bluespot.opengl;

import javax.media.opengl.GL;

import com.bluespot.demonstration.Demonstration;

public class SpiralPointsDemonstration extends AbstractGLDemonstration {

    public static void main(final String[] args) {
        Demonstration.launchWrapped(SpiralPointsDemonstration.class);
    }

    @Override
    protected void render(final GL gl) {
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
