package com.bluespot.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.bluespot.geom.Geometry;

public final class Components {

	public static enum Interpolation {
		BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC), BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR), NEAREST_NEIGHBOR(
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		private final Object interpolationValue;

		private Interpolation(final Object interpolationValue) {
			this.interpolationValue = interpolationValue;
		}

		public Object getValue() {
			return this.interpolationValue;
		}

		public void set(final Graphics2D g) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.getValue());
		}
	}

	private Components() {
		throw new AssertionError("This class cannot be instantiated.");
	}

	public static void center(final JFrame component) {
		component.setLocationRelativeTo(null);
	}

	/**
	 * Returns the center of the specified component's drawing area.
	 * 
	 * @param component
	 *            the {@link JComponent} used in this operation
	 * @return the center {@link Point}
	 * @see Components#getDrawableArea(JComponent)
	 * @see Geometry#getCenter(Rectangle)
	 */
	public static Point getCenter(final JComponent component) {
		return Geometry.getCenter(Components.getDrawableArea(component));
	}

	/**
	 * Returns the {@link Rectangle} object that is equal to the available
	 * drawing area of the specified {@link JComponent}, minus the insets of
	 * that component.
	 * 
	 * @param component
	 *            the {@code JComponent} used in this operation
	 * @return the area available for drawing
	 * @see JComponent#getInsets()
	 */
	public static Rectangle getDrawableArea(final JComponent component) {
		final Rectangle rectangle = new Rectangle(component.getSize());
		Geometry.subtractInsets(rectangle, component.getInsets());
		return rectangle;
	}

	/**
	 * Returns the {@link Dimension} object that is equal to the available
	 * drawing area of the specified {@link JComponent}, minus the insets of
	 * that component.
	 * 
	 * @param component
	 *            the {@code JComponent} used in this operation
	 * @return the dimensions available for drawing
	 * @see JComponent#getInsets()
	 */
	public static Dimension getDrawableSize(final JComponent component) {
		final Dimension dimension = component.getSize();
		Geometry.subtractInsets(dimension, component.getInsets());
		return dimension;
	}

	public static int getIndexOf(final Container parent, final Component component) {
		for (int i = 0; i < parent.getComponentCount(); i++) {
			if (parent.getComponent(i) == component) {
				return i;
			}
		}
		return -1;
	}

	public static void setAntialias(final Graphics2D g, final boolean isAntialiased) {
		final Object antialiased = isAntialiased ? RenderingHints.VALUE_ANTIALIAS_ON
				: RenderingHints.VALUE_ANTIALIAS_OFF;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiased);
	}

	public static void setInterpolation(final Graphics2D g, final Interpolation interpolation) {
		interpolation.set(g);
	}

}
