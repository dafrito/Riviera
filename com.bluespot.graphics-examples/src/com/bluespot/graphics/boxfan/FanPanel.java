package com.bluespot.graphics.boxfan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.bluespot.geom.Geometry;
import com.bluespot.swing.Components;

public class FanPanel extends JPanel {

    private static final long serialVersionUID = -740545880409083777L;

    private static final int FPS = 50;

    private final Timer timer = new Timer(1000 / FPS, new ActionListener() {
        public void actionPerformed(final ActionEvent e) {
            FanPanel.this.repaint();
        }
    });

    private double degreeOffset = 0;
    protected BufferedImage fanImage;

    private final Rotation model;

    public FanPanel(final double percentageFilled, final int numArcs) {
        this(new Rotation(), percentageFilled, numArcs);
    }

    public FanPanel(final Rotation model, final double percentageFilled, final int numArcs) {
        super();
        this.model = model;
        this.percentageFilled = percentageFilled;
        this.numArcs = numArcs;
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(final ComponentEvent e) {
                FanPanel.this.fanImage = null;
            }

        });
    }

    public Rotation getModel() {
        return this.model;
    }

    /**
     * Percentage of the component's available area filled with the fan.
     */
    private final double percentageFilled;
    private final int numArcs;

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.blue);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.white);

        this.drawFan((Graphics2D) g.create());

        this.degreeOffset += this.getModel().getAngle();
        this.degreeOffset %= 360;
    }

    private void drawFan(final Graphics2D g) {

        // Antialiasing makes our fan look alot better.
        Components.setAntialias(g, true);

        final Dimension halfBounds = this.getSize();
        Geometry.Round.halfSize(halfBounds);
        g.translate(halfBounds.width, halfBounds.height);

        final Rectangle box = Components.getDrawableArea(this);
        Geometry.trimToSquare(box);
        Geometry.Round.multiply(box, this.percentageFilled);
        Geometry.alignCenter(box, new Point(0, 0));

        g.rotate(Math.toRadians(this.degreeOffset));
        g.setPaint(Color.white);
        g.rotate((-Math.PI / 2) + (Math.PI / this.numArcs / 2));

    }

    private void renderFan(final BufferedImage image, final int size) {
        final Graphics2D g = image.createGraphics();
        for (int i = 0; i < this.numArcs; i++) {
            g.fillArc(0, 0, size, size, 0, 360 / (this.numArcs * 2));
            g.rotate(2 * Math.PI / this.numArcs);
        }
        g.dispose();
    }
}
