package com.bluespot.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.bluespot.demonstration.Demonstration;

/**
 * Draws a trapezoid on screen using a triangle strip. Strips can be thought of
 * lists of vertices, where each triangle in the list is composed of some
 * adjacent group of three vertices. The vertices are colored for easy viewing.
 * 
 * @author Aaron Faanes
 * 
 */
public class TriangleStripDemonstration extends AbstractGLDemonstration {

    private static final long serialVersionUID = 1L;

    /**
     * Launches this demonstration.
     * 
     * @param args
     *            unused
     */
    public static void main(final String[] args) {
        Demonstration.launchWrapped(TriangleStripDemonstration.class);
    }

    @Override
    public void init(final GLAutoDrawable drawable) {
        super.init(drawable);
    }

    @Override
    protected void render(final GL gl) {
        gl.glBegin(GL.GL_TRIANGLE_STRIP);

        gl.glVertex2f(0, 0);

        gl.glVertex2f(50, 0);

        gl.glColor3f(0, 1, 0);
        gl.glVertex2f(25, 50);

        gl.glColor3f(0, 0, 1);
        gl.glVertex2f(75, 50);

        gl.glColor3f(1, 0, 0);
        gl.glVertex2f(50, 100);

        gl.glEnd();
    }

}
