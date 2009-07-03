package com.bluespot.opengl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;

import com.bluespot.graphics.Painting;

/**
 * A skeletal OpenGL application. This sets up an orthogonal view that
 * automatically manages rotation by mouse. I imagine the current way of doing
 * rotation is sub-optimal, but I haven't figured out a cleaner way of handling
 * camera movement.
 * 
 * @author Aaron Faanes
 * 
 */
public abstract class AbstractGLDemonstration extends GLJPanel implements GLEventListener {

    private static final long serialVersionUID = 107706882711513973L;

    private Point lastPoint = null;

    /**
     * Constructs a new {@link AbstractGLDemonstration}.
     */
    public AbstractGLDemonstration() {
        this.setPreferredSize(new Dimension(800, 600));
        this.addGLEventListener(this);

        Painting.repaintPeriodically(this, 50).start();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                AbstractGLDemonstration.this.lastPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                AbstractGLDemonstration.this.lastPoint = null;
            }

        });
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(final MouseEvent e) {
                final Point last = AbstractGLDemonstration.this.lastPoint;
                if (last == null) {
                    return;
                }
                AbstractGLDemonstration.this.yRot += e.getX() - last.x;
                AbstractGLDemonstration.this.xRot += e.getY() - last.y;
                AbstractGLDemonstration.this.lastPoint = e.getPoint();
            }
        });
    }

    private float xRot = 0;
    private float yRot = 0;

    @Override
    public void display(final GLAutoDrawable drawable) {
        final GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glPushMatrix();
        gl.glRotatef(this.xRot, 1, 0, 0);
        gl.glRotatef(this.yRot, 0, 1, 0);

        this.render(gl);

        gl.glPopMatrix();
    }

    /**
     * Performs any rendering necessary by this demonstration.
     * 
     * @param gl
     *            the rendering context
     */
    protected abstract void render(GL gl);

    @Override
    public void displayChanged(final GLAutoDrawable drawable, final boolean modeChanged, final boolean deviceChanged) {
        // XXX Do nothing; I'm not currently sure what to do here.
    }

    @Override
    public void init(final GLAutoDrawable drawable) {
        final GL gl = drawable.getGL();
        gl.glClearColor(0, 0, 0, .5f);
    }

    @Override
    public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, final int height) {
        final GL gl = drawable.getGL();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        final double aspectRatio = (double) height / (double) width;

        final double range = 100.0;
        if (width <= height) {
            gl.glOrtho(-range, range, -range * aspectRatio, range * aspectRatio, 2 * range, 2 * -range);
        } else {
            gl.glOrtho(-range / aspectRatio, range / aspectRatio, -range, range, 2 * range, 2 * -range);
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

}
