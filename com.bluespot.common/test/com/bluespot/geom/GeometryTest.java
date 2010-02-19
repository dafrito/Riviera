package com.bluespot.geom;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Test;

public class GeometryTest {

    /**
     * @see Geometry#alignCenter(java.awt.Rectangle,java.awt.Point)
     */
    @Test
    public void testAlignCenter() {
        final Rectangle rect = new Rectangle(0, 0, 10, 10);

        Geometry.alignCenter(rect, new Point(0, 0));

        assertThat(Geometry.getCenter(rect), is(new Point(0, 0)));
        assertThat(rect, is(new Rectangle(-5, -5, 10, 10)));
    }

    /**
     * @see Geometry#bounds(java.awt.Point,java.awt.Point)
     */
    @Test
    public void testBounds() {

        final Point origin = new Point(0, 0);
        final Point a = new Point(1, 1);
        final Point b = new Point(-1, -1);

        assertThat("Simple", Geometry.bounds(origin, a), is(new Dimension(1, 1)));
        assertThat("Simple", Geometry.bounds(a, origin), is(new Dimension(-1, -1)));

        assertThat("Axis-crossing", Geometry.bounds(b, a), is(new Dimension(2, 2)));
        assertThat("Axis-crossing,reversed", Geometry.bounds(a, b), is(new Dimension(-2, -2)));

        assertThat("Opposing signs", Geometry.bounds(new Point(1, -1), new Point(-1, 1)), is(new Dimension(-2, 2)));
        assertThat("Opposing signs, reversed", Geometry.bounds(new Point(-1, 1), new Point(1, -1)), is(new Dimension(2,
                -2)));
    }

    /**
     * @see Geometry#containsExclusive(double,double,double)
     */
    @Test
    public void testContainsExclusive() {
        assertThat("Simple", Geometry.containsExclusive(0, 2, 1), is(true));
        assertThat("Simple, reversed", Geometry.containsExclusive(2, 0, 1), is(true));

        assertThat("Under", Geometry.containsExclusive(0, 2, -1), is(false));
        assertThat("Over", Geometry.containsExclusive(0, 2, 3), is(false));

        assertThat("Upper bound", Geometry.containsExclusive(0, 1, 1), is(false));
        assertThat("Lower bound", Geometry.containsExclusive(0, 1, 0), is(false));
    }

    /**
     * @see Geometry#containsInclusive(double,double,double)
     */
    @Test
    public void testContainsInclusive() {
        assertThat("Simple", Geometry.containsInclusive(0, 2, 1), is(true));
        assertThat("Simple, reversed", Geometry.containsInclusive(2, 0, 1), is(true));

        assertThat("Under", Geometry.containsInclusive(0, 2, -1), is(false));
        assertThat("Over", Geometry.containsInclusive(0, 2, 3), is(false));

        assertThat("Upper bound", Geometry.containsInclusive(0, 1, 1), is(true));
        assertThat("Lower bound", Geometry.containsInclusive(0, 1, 0), is(true));
    }

    /**
     * @see Operations#halfSize(java.awt.Dimension)
     */
    @Test
    public void testHalf() {
        Dimension dim = new Dimension(100, 100);
        Geometry.Floor.halfSize(dim);
        assertThat(dim, is(new Dimension(100 / 2, 100 / 2)));

        dim = new Dimension(0, 0);
        Geometry.Floor.halfSize(dim);
        assertThat("Empty dimension", dim, is(new Dimension(0, 0)));
    }

    /**
     * @see Geometry#interpolate(java.awt.Point,java.awt.Point,double)
     */
    @Test
    public void testInterpolate() {
        final Point source = new Point(0, 0);
        final Point dest = new Point(2, 2);

        final Point mid = Geometry.interpolate(source, dest, .5);
        assertThat(mid, is(new Point(1, 1)));

        assertThat(Geometry.interpolate(source, dest, 0), is(source));
        assertThat(Geometry.interpolate(source, dest, 1), is(dest));
    }

    /**
     * @see Operations#multiply(java.awt.Dimension,double)
     */
    @Test
    public void testMultiply() {
        Dimension dim = new Dimension(100, 100);
        Geometry.Round.multiply(dim, 1);
        assertThat("Identity", dim, is(new Dimension(100, 100)));

        dim = new Dimension(100, 100);
        Geometry.Round.multiply(dim, 2);
        assertThat("Double", dim, is(new Dimension(100 * 2, 100 * 2)));

        dim = new Dimension(100, 100);
        Geometry.Round.multiply(dim, 0);
        assertThat("Zero scale", dim, is(new Dimension(0, 0)));

        dim = new Dimension(0, 0);
        Geometry.Round.multiply(dim, 2);
        assertThat("Double, empty dimension", dim, is(new Dimension(0, 0)));

    }

    @Test
    public void testFlip() {
        Dimension dim = new Dimension(100, 100);
        Geometry.flip(dim);
        assertThat("flip", dim, is(new Dimension(-100, -100)));

        dim = new Dimension(100, 100);
        Geometry.flipOverX(dim);
        assertThat("flipOverX", dim, is(new Dimension(100, -100)));

        dim = new Dimension(100, 100);
        Geometry.flipOverY(dim);
        assertThat("flipOverY", dim, is(new Dimension(-100, 100)));
    }

    /**
     * @see Geometry#growToSquare(java.awt.Dimension)
     */
    @Test
    public void testSquareByGrowth() {
        Dimension dim = new Dimension(100, 100);
        Geometry.growToSquare(dim);
        assertThat("Identity", dim, is(new Dimension(100, 100)));

        dim = new Dimension(200, 100);
        Geometry.growToSquare(dim);
        assertThat("Positive", dim, is(new Dimension(200, 200)));

        dim = new Dimension(100, 200);
        Geometry.growToSquare(dim);
        assertThat("Positive, reversed", dim, is(new Dimension(200, 200)));

        dim = new Dimension(-200, -100);
        Geometry.growToSquare(dim);
        assertThat("Both negative", dim, is(new Dimension(-200, -200)));

        dim = new Dimension(-100, -200);
        Geometry.growToSquare(dim);
        assertThat("Both negative, reversed", dim, is(new Dimension(-200, -200)));

        dim = new Dimension(100, -200);
        Geometry.growToSquare(dim);
        assertThat("Opposing signs (positive is smaller)", dim, is(new Dimension(200, -200)));

        dim = new Dimension(200, -100);
        Geometry.growToSquare(dim);
        assertThat("Opposing signs, reversed (positive is bigger)", dim, is(new Dimension(200, -200)));
    }

    /**
     * @see Geometry#trimToSquare(java.awt.Dimension)
     */
    @Test
    public void testSquareByTrim() {

        Dimension dim = new Dimension(100, 100);
        Geometry.trimToSquare(dim);
        assertThat("Identity", dim, is(new Dimension(100, 100)));

        dim = new Dimension(200, 100);
        Geometry.trimToSquare(dim);
        assertThat("Positive", dim, is(new Dimension(100, 100)));

        dim = new Dimension(100, 200);
        Geometry.trimToSquare(dim);
        assertThat("Positive, reversed", dim, is(new Dimension(100, 100)));

        dim = new Dimension(-200, -100);
        Geometry.trimToSquare(dim);
        assertThat("Both negative", dim, is(new Dimension(-100, -100)));

        dim = new Dimension(-100, -200);
        Geometry.trimToSquare(dim);
        assertThat("Both negative, reversed", dim, is(new Dimension(-100, -100)));

        dim = new Dimension(100, -200);
        Geometry.trimToSquare(dim);
        assertThat("Opposing signs (positive is smaller)", dim, is(new Dimension(100, -100)));

        dim = new Dimension(200, -100);
        Geometry.trimToSquare(dim);
        assertThat("Opposing signs, reversed (positive is bigger)", dim, is(new Dimension(100, -100)));
    }

    /**
     * @see Geometry#subtractInsets(Dimension, java.awt.Insets)
     */
    @Test
    public void testSubtractInsetsWithDimension() {
        Dimension dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(10));
        assertThat("Simple", dim, is(new Dimension(80, 80)));

        dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(0));
        assertThat("Identity", dim, is(new Dimension(100, 100)));

        dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(10, 20));
        assertThat("Rectangular insets", dim, is(new Dimension(80, 60)));

        dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(20, 10));
        assertThat("Rectangular insets, reversed", dim, is(new Dimension(60, 80)));

        dim = new Dimension(-100, -100);
        Geometry.subtractInsets(dim, Geometry.insets(10, 10));
        assertThat("Both negative", dim, is(new Dimension(-80, -80)));

        dim = new Dimension(-100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(10, 10));
        assertThat("Opposing signs", dim, is(new Dimension(-80, 80)));

        dim = new Dimension(100, -100);
        Geometry.subtractInsets(dim, Geometry.insets(10, 10));
        assertThat("Opposing signs, reversed", dim, is(new Dimension(80, -80)));

        dim = new Dimension(10, 10);
        Geometry.subtractInsets(dim, Geometry.insets(20, 20));
        assertThat("Big insets cause zero-size dimensions", dim, is(new Dimension(0, 0)));
    }

    /**
     * @see Geometry#subtractInsets(Dimension, java.awt.Insets)
     */
    @Test
    public void testSubtractInsetsWithDimensionAndNegativeInsets() {
        Dimension dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(10, -10));
        assertThat("Negative inset height", dim, is(new Dimension(80, 120)));

        dim = new Dimension(5, 5);
        Geometry.subtractInsets(dim, Geometry.insets(-10, -10));
        assertThat("Negative inset values are never converted to zero", dim, is(new Dimension(25, 25)));
    }

    /**
     * @see Geometry#subtractInsets(java.awt.Rectangle,java.awt.Insets)
     */
    @Test
    public void testSubtractInsetsWithRectangle() {
        Rectangle rect = new Rectangle(100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(10));
        assertThat("Simple", rect, is(new Rectangle(10, 10, 80, 80)));

        rect = new Rectangle(100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(0));
        assertThat("Identity", rect, is(new Rectangle(100, 100)));

        rect = new Rectangle(100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(10, 20));
        assertThat("Rectangular insets", rect, is(new Rectangle(10, 20, 80, 60)));

        rect = new Rectangle(100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(20, 10));
        assertThat("Rectangular insets, reversed", rect, is(new Rectangle(20, 10, 60, 80)));

        rect = new Rectangle(-100, -100);
        Geometry.subtractInsets(rect, Geometry.insets(10, 10));
        assertThat("Both negative", rect, is(new Rectangle(-10, -10, -80, -80)));

        rect = new Rectangle(-100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(10, 10));
        assertThat("Opposing signs", rect, is(new Rectangle(-10, 10, -80, 80)));

        rect = new Rectangle(100, -100);
        Geometry.subtractInsets(rect, Geometry.insets(10, 10));
        assertThat("Opposing signs, reversed", rect, is(new Rectangle(10, -10, 80, -80)));

        rect = new Rectangle(10, 10);
        Geometry.subtractInsets(rect, Geometry.insets(20, 20));
        assertThat("Big insets cause zero-size rectangle", rect, is(new Rectangle(5, 5, 0, 0)));
    }
}
