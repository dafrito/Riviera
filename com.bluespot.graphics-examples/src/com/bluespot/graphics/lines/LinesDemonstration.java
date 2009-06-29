package com.bluespot.graphics.lines;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import com.bluespot.demonstration.Demonstration;

public class LinesDemonstration extends JComponent {

    private static final int ITERATIONS = 40;
    private static float OFFSET = .1f;

    public LinesDemonstration() {
        this.setPreferredSize(new Dimension(300, 300));
        new Timer(1000 / 30, new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                OFFSET += .01f;
                LinesDemonstration.this.repaint();
            }
        }).start();
    }

    private void simpleLine(final Graphics g) {
        final int width = this.getWidth() - 1;
        final int height = this.getHeight() - 1;
        int j = 0;
        int k = 0;
        for (int i = 0; i < LinesDemonstration.ITERATIONS; i++) {
            final float colorVariant = (float) i / (float) LinesDemonstration.ITERATIONS;
            g.setColor(new Color(Color.HSBtoRGB((OFFSET + colorVariant) % 1.0f, 1.0f - colorVariant, 1.0f)));
            g.drawLine(width, height, width - j, k);
            g.drawLine(width, 0, width - j, height - k);
            g.drawLine(0, height, j, k);
            g.drawLine(0, 0, j, height - k);
            j += width / LinesDemonstration.ITERATIONS;
            k += height / LinesDemonstration.ITERATIONS;
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int width = this.getWidth() - 1;
        final int height = this.getHeight() - 1;
        for (int i = 0; i < LinesDemonstration.ITERATIONS; i++) {
            final float colorVariant = (float) i / (float) LinesDemonstration.ITERATIONS;
            g.setColor(new Color(Color.HSBtoRGB((OFFSET + colorVariant) % 1.0f, 1.0f - colorVariant, 1.0f)));
            final int widthDifference = width * i / LinesDemonstration.ITERATIONS;
            final int heightDifference = height * i / LinesDemonstration.ITERATIONS;
            g.drawLine(widthDifference, 0, width, heightDifference);
            g.drawLine(0, heightDifference, widthDifference, height);
            g.drawLine(widthDifference, height, width, height - heightDifference);
            g.drawLine(0, height - heightDifference, widthDifference, 0);
        }
        this.simpleLine(g);

    }

    public static void main(final String args[]) {
        Demonstration.launchWrapped(LinesDemonstration.class);
    }

}