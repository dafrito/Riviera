package com.bluespot.opengl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.Timer;

import com.bluespot.swing.Components;

public class SimpleDemo extends GLJPanel implements GLEventListener {

    public static void main(final String[] args) {

        Components.LookAndFeel.NIMBUS.activate();

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JSplitPane pane = new JSplitPane();
        pane.setLeftComponent(new SimpleDemo());
        pane.setRightComponent(new JButton("LOL"));

        frame.setContentPane(pane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private Point lastPoint = null;

    public SimpleDemo() {
        super();
        this.setPreferredSize(new Dimension(800, 600));
        this.addGLEventListener(this);

        new Timer(1000 / 50, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimpleDemo.this.repaint();
            }
        }).start();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                SimpleDemo.this.lastPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                SimpleDemo.this.lastPoint = null;
            }

        });
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(final MouseEvent e) {
                final Point last = SimpleDemo.this.lastPoint;
                if (last == null) {
                    return;
                }
                SimpleDemo.this.yRot += e.getX() - last.x;
                SimpleDemo.this.xRot += e.getY() - last.y;
                SimpleDemo.this.lastPoint = e.getPoint();
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
        final float[] temp = new float[2];
        gl.glGetFloatv(GL.GL_LINE_WIDTH_RANGE, temp, 0);
        gl.glLineWidth(4);
        gl.glBegin(GL.GL_LINE_STRIP);

        float z = -50.0f;
        for (float angle = 0; angle <= 6 * Math.PI; angle += 0.1f) {
            final float x = 50.0f * (float) Math.sin(angle);
            final float y = 50.0f * (float) Math.cos(angle);

            gl.glVertex3f(x, y, z);
            z += 0.5f;
        }
        gl.glEnd();

        gl.glPopMatrix();
    }

    @Override
    public void displayChanged(final GLAutoDrawable drawable, final boolean modeChanged, final boolean deviceChanged) {
        // TODO Auto-generated method stub

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
            gl.glOrtho(-range, range, -range * aspectRatio, range * aspectRatio, range, -range);
        } else {
            gl.glOrtho(-range / aspectRatio, range / aspectRatio, -range, range, range, -range);
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

}
