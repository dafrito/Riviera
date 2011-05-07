package com.bluespot.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * A suite of utility methods dealing with painting and images.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Painting {

	private Painting() {
		// Suppress default construct to ensure non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * Creates and returns a {@link Timer} that calls
	 * {@link JComponent#repaint()} periodically. The timer is <em>not</em>
	 * started.
	 * <p>
	 * This is useful for animation but the consistency of calls is only as
	 * reliable as {@link Timer}. Therefore, any time-sensitive animation should
	 * keep track of elapsed time between calls to repaint.
	 * 
	 * @param component
	 *            the component that is repainted
	 * @param framesPerSecond
	 *            the ideal frames per second. The timer makes a best-effort to
	 *            meet this frame rate, but no guarantees or adjustments are
	 *            made.
	 * @return the created {@code Timer} object. It must be manually started.
	 */
	public static Timer repaintPeriodically(final JComponent component, final int framesPerSecond) {

		int delay;
		if (framesPerSecond <= 0) {
			delay = 0;
		} else {
			delay = 1000 / framesPerSecond;
		}
		final Timer timer = new Timer(delay, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				component.repaint();
			}
		});
		return timer;
	}

	/**
	 * Creates a compatible {@link BufferedImage} for use with this
	 * {@link Graphics2D} context.
	 * 
	 * @param g
	 *            {@code Graphics2D} object to use to create the {@code
	 *            BufferedImage}
	 * @param width
	 *            the width of the created image
	 * @param height
	 *            the height of the created image
	 * @return a new {@code BufferedImage}
	 * @see GraphicsConfiguration#createCompatibleImage(int, int)
	 * @see Graphics2D#getDeviceConfiguration()
	 */
	public static BufferedImage createImage(final Graphics2D g, final int width, final int height) {
		return g.getDeviceConfiguration().createCompatibleImage(width, height);
	}

	/**
	 * Creates a device-compatible buffered image of the specified dimensions.
	 * 
	 * @param width
	 *            the width of the created image
	 * @param height
	 *            the height of the create image
	 * @return a device-compatible buffered image
	 * @see GraphicsConfiguration#createCompatibleImage(int, int)
	 */
	public static BufferedImage createImage(final int width, final int height) {
		final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final GraphicsDevice device = environment.getDefaultScreenDevice();
		final GraphicsConfiguration configuration = device.getDefaultConfiguration();
		return configuration.createCompatibleImage(width, height);
	}

	/**
	 * Draws a rectangle using the specified {@link Graphics2D} object.
	 * 
	 * @param g
	 *            the {@code Graphics2D} object used in this operation
	 * @param dimension
	 *            the {@link Dimension} to draw
	 * @see Graphics2D#drawRect(int, int, int, int)
	 */
	public static void draw(final Graphics2D g, final Dimension dimension) {
		g.drawRect(0, 0, dimension.width, dimension.height);
	}

	/**
	 * Draws a rectangle using the specified {@link Graphics2D} object.
	 * 
	 * @param g
	 *            the {@code Graphics2D} object used in this operation
	 * @param rectangle
	 *            the {@link Rectangle} to draw
	 * @see Graphics2D#drawRect(int, int, int, int)
	 */
	public static void draw(final Graphics2D g, final Rectangle rectangle) {
		g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	/**
	 * Draws an arc using the specified {@link Rectangle} as its bounds.
	 * 
	 * @param g
	 *            the {@code Graphics2D} object used in this operation
	 * @param rectangle
	 *            the {@code Rectangle} that the arc will be centered on and
	 *            contained by
	 * @param startingAngle
	 *            the starting angle of the arc
	 * @param arcAngle
	 *            the length of the arc's angle
	 */
	public static void drawArc(final Graphics2D g, final Rectangle rectangle, final int startingAngle,
			final int arcAngle) {
		g.drawArc(rectangle.x, rectangle.y, rectangle.width, rectangle.height, startingAngle, arcAngle);
	}

	/**
	 * Fills a rectangle using the specified {@link Graphics2D} object.
	 * 
	 * @param g
	 *            the {@code Graphics2D} object used in this operation
	 * @param dimension
	 *            the {@link Dimension} to fill
	 * @see Graphics2D#fillRect(int, int, int, int)
	 */
	public static void fill(final Graphics2D g, final Dimension dimension) {
		g.fillRect(0, 0, dimension.width, dimension.height);
	}

	/**
	 * Fills an arc using the specified {@link Rectangle} as its bounds.
	 * 
	 * @param g
	 *            the {@code Graphics2D} object used in this operation
	 * @param rectangle
	 *            the {@code Rectangle} that the arc will be centered on and
	 *            contained by
	 * @param startingAngle
	 *            the starting angle of the arc
	 * @param arcAngle
	 *            the length of the arc's angle
	 */
	public static void fillArc(final Graphics2D g, final Rectangle rectangle, final int startingAngle,
			final int arcAngle) {
		g.fillArc(rectangle.x, rectangle.y, rectangle.width, rectangle.height, startingAngle, arcAngle);
	}

}
