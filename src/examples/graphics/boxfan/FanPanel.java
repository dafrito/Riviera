package examples.graphics.boxfan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import geom.Geometry;
import swing.Components;

public class FanPanel extends JPanel {

	private static final long serialVersionUID = -740545880409083777L;

	private static final int FPS = 50;

	@SuppressWarnings("unused")
	private final Timer timer = new Timer(1000 / FPS, new ActionListener() {
		@Override
		public void actionPerformed(final ActionEvent e) {
			FanPanel.this.repaint();
		}
	});

	private double degreeOffset = 0;

	private final Rotation model;

	public FanPanel(final Rotation model, final double percentageFilled, final int numArcs) {
		super();
		this.model = model;
		this.percentageFilled = percentageFilled;
		this.numArcs = numArcs;
		new Timer(1000 / 30, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		}).start();
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

		this.drawFan((Graphics2D) g.create());

		this.degreeOffset += this.getModel().getAngle();
	}

	private void drawFan(final Graphics2D g) {
		Components.setAntialias(g, true);

		final Dimension halfBounds = this.getSize();
		Geometry.Round.halfSize(halfBounds);
		g.translate(halfBounds.width, halfBounds.height);

		final Rectangle box = Components.getDrawableArea(this);
		Geometry.trimToSquare(box);
		Geometry.Round.multiply(box, this.percentageFilled);
		Geometry.alignCenter(box, new Point(0, 0));

		g.rotate(this.degreeOffset);
		g.setPaint(Color.white);
		for (int i = 0; i < this.numArcs; i++) {
			g.fillArc(box.x, box.y, box.width, box.height, 0, 360 / (this.numArcs * 2));
			g.rotate(2 * Math.PI / this.numArcs);
		}
	}
}
