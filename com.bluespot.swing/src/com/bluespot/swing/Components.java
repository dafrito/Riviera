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

/**
 * A collection of utility methods dealing with Swing components.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Components {

	/**
	 * Levels of texture interpolation
	 * 
	 * @author Aaron Faanes
	 * 
	 */
	public static enum Interpolation {
		/**
		 * Represents bicubic texture filtering.
		 * 
		 * @see RenderingHints#VALUE_INTERPOLATION_BICUBIC
		 */
		BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),

		/**
		 * Represents bilinear texture filtering.
		 * 
		 * @see RenderingHints#VALUE_INTERPOLATION_BILINEAR
		 */
		BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),

		/**
		 * Represents nearest-neighbor filtering.
		 * 
		 * @see RenderingHints#VALUE_INTERPOLATION_NEAREST_NEIGHBOR
		 * 
		 */
		NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		private final Object interpolationValue;

		private Interpolation(final Object interpolationValue) {
			this.interpolationValue = interpolationValue;
		}

		/**
		 * Returns the value of this filtering strategy, as it is known to
		 * Swing.
		 * 
		 * @return the value of this filtering strategy
		 * 
		 * @see Graphics2D#setRenderingHint(java.awt.RenderingHints.Key, Object)
		 */
		public Object getValue() {
			return this.interpolationValue;
		}

		/**
		 * Sets the graphics context to use this filtering strategy.
		 * 
		 * @param g
		 *            the context to modify
		 */
		public void set(final Graphics2D g) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.getValue());
		}
	}

	private Components() {
		throw new AssertionError("This class cannot be instantiated.");
	}

	/**
	 * Centers the specified frame on the screen.
	 * 
	 * @param component
	 *            the frame to center
	 * @see JFrame#setLocationRelativeTo(Component)
	 */
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

	/**
	 * Returns the index of the specified component in the specified parent.
	 * 
	 * @param parent
	 *            the parent of the specified component
	 * @param component
	 *            the component that is the target of this search
	 * @return the index of the specified component, otherwise {@code -1} is
	 *         returned
	 */
	public static int getIndexOf(final Container parent, final Component component) {
		for (int i = 0; i < parent.getComponentCount(); i++) {
			if (parent.getComponent(i) == component) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Sets whether the specified graphics context should use antialiasing.
	 * 
	 * @param g
	 *            the graphics context to modify
	 * @param isAntialiased
	 *            {@code true} if antialiasing should be enabled, otherwise
	 *            {@code false}
	 * @see RenderingHints#KEY_ANTIALIASING
	 */
	public static void setAntialias(final Graphics2D g, final boolean isAntialiased) {
		final Object antialiased = isAntialiased ? RenderingHints.VALUE_ANTIALIAS_ON
				: RenderingHints.VALUE_ANTIALIAS_OFF;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiased);
	}

	/**
	 * Sets the graphics context to use the specified level of texture
	 * interpolation.
	 * 
	 * @param g
	 *            the graphics context to modify
	 * @param interpolation
	 *            the level of texture interpolation
	 * @see RenderingHints#KEY_INTERPOLATION
	 */
	public static void setInterpolation(final Graphics2D g, final Interpolation interpolation) {
		interpolation.set(g);
	}

}
