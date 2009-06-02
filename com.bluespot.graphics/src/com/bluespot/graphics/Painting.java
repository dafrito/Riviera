package com.bluespot.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public final class Painting {

    /**
     * Draws a rectangle using the specified {@link Graphics2D} object.
     * 
     * @param g the {@code Graphics2D} object used in this operation
     * @param rectangle the {@link Rectangle} to draw
     * @see Graphics2D#drawRect(int, int, int, int)
     */
    public static void draw(Graphics2D g, Rectangle rectangle) {
        g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    /**
     * Draws a rectangle using the specified {@link Graphics2D} object.
     * 
     * @param g the {@code Graphics2D} object used in this operation
     * @param dimension the {@link Dimension} to draw
     * @see Graphics2D#drawRect(int, int, int, int)
     */
    public static void draw(Graphics2D g, Dimension dimension) {
        g.drawRect(0, 0, dimension.width, dimension.height);
    }

    /**
     * Fills a rectangle using the specified {@link Graphics2D} object.
     * 
     * @param g the {@code Graphics2D} object used in this operation
     * @param dimension the {@link Dimension} to fill
     * @see Graphics2D#fillRect(int, int, int, int)
     */
    public static void fill(Graphics2D g, Dimension dimension) {
        g.fillRect(0, 0, dimension.width, dimension.height);
    }

    /**
     * Fills an arc using the specified {@link Rectangle} as its bounds.
     * 
     * @param g the {@code Graphics2D} object used in this operation
     * @param rectangle the {@code Rectangle} that the arc will be centered on
     *        and contained by
     * @param startingAngle the starting angle of the arc
     * @param arcAngle the length of the arc's angle
     */
    public static void fillArc(Graphics2D g, Rectangle rectangle, int startingAngle, int arcAngle) {
        g.fillArc(rectangle.x, rectangle.y, rectangle.width, rectangle.height, startingAngle, arcAngle);
    }

    /**
     * Draws an arc using the specified {@link Rectangle} as its bounds.
     * 
     * @param g the {@code Graphics2D} object used in this operation
     * @param rectangle the {@code Rectangle} that the arc will be centered on
     *        and contained by
     * @param startingAngle the starting angle of the arc
     * @param arcAngle the length of the arc's angle
     */
    public static void drawArc(Graphics2D g, Rectangle rectangle, int startingAngle, int arcAngle) {
        g.drawArc(rectangle.x, rectangle.y, rectangle.width, rectangle.height, startingAngle, arcAngle);
    }

    /**
     * Creates a compatible {@link BufferedImage} for use with this
     * {@link Graphics2D} context.
     * 
     * @param g {@code Graphics2D} object to use to create the {@code BufferedImage}
     * @param width the width of the created image
     * @param height the height of the created image
     * @return a new {@code BufferedImage}
     * @see GraphicsConfiguration#createCompatibleImage(int, int)
     * @see Graphics2D#getDeviceConfiguration()
     */
    public static BufferedImage createImage(Graphics2D g, int width, int height) {
        return g.getDeviceConfiguration().createCompatibleImage(width, height);
    }
    
    public static BufferedImage createImage(int width, int height) {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environment.getDefaultScreenDevice();
        GraphicsConfiguration configuration = device.getDefaultConfiguration();
        return configuration.createCompatibleImage(width, height);
        
    }

    private Painting() {
        throw new AssertionError("This class cannot be instantiated");
    }

}
