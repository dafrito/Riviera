package com.bluespot.opengl;

import javax.media.opengl.GL;

import com.bluespot.demonstration.Demonstration;
import com.sun.opengl.util.GLUT;

/**
 * Demonstrates simple animation by using {@link GL#glPushMatrix()} and
 * {@link GL#glPopMatrix()}.
 * 
 * @author Aaron Faanes
 * 
 */
public class AtomDemonstration extends AbstractGLDemonstration {

    private static final long serialVersionUID = 1L;

    private float rotation = 0.0f;

    private final GLUT glut = new GLUT();

    /**
     * Launches this demonstration.
     * 
     * @param args
     *            unused
     */
    public static void main(final String[] args) {
        Demonstration.launchWrapped(AtomDemonstration.class);
    }

    @Override
    protected void render(final GL gl) {
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glTranslatef(0, 0, -100);
        gl.glColor3f(1, 0, 0);
        this.glut.glutSolidSphere(10, 15, 15);

        gl.glColor3f(1, 1, 0);

        gl.glPushMatrix();
        gl.glRotatef(this.rotation, 0, 1, 0);
        gl.glTranslatef(90, 0, 0);
        this.glut.glutSolidSphere(6, 15, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(45, 0, 0, 1);
        gl.glRotatef(this.rotation, 0, 1, 0);
        gl.glTranslatef(-70, 0, 0);
        this.glut.glutSolidSphere(6, 15, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(-45, 0, 0, 1);
        gl.glRotatef(this.rotation, 0, 1, 0);
        gl.glTranslatef(0, 0, 60);
        this.glut.glutSolidSphere(6, 15, 15);
        gl.glPopMatrix();

        this.rotation += 5.0f;
        this.rotation %= 360.0f;
    }
}
