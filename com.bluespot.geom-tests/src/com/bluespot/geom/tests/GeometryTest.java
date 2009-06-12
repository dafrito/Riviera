package com.bluespot.geom.tests;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.bluespot.geom.Geometry;

public class GeometryTest {

    /**
     * @see Geometry#alignCenter(java.awt.Rectangle,java.awt.Point)
     */
    @Test
    public void testAlignCenter() {
        final Rectangle rect = new Rectangle(0, 0, 10, 10);

        Geometry.alignCenter(rect, new Point(0, 0));

        Assert.assertThat(Geometry.getCenter(rect), CoreMatchers.is(new Point(0, 0)));
        Assert.assertThat(rect, CoreMatchers.is(new Rectangle(-5, -5, 10, 10)));
    }

    /**
     * @see Geometry#bounds(java.awt.Point,java.awt.Point)
     */
    @Test
    public void testBounds() {

        final Point origin = new Point(0, 0);
        final Point a = new Point(1, 1);
        final Point b = new Point(-1, -1);

        Assert.assertThat("Simple", Geometry.bounds(origin, a), CoreMatchers.is(new Dimension(1, 1)));
        Assert.assertThat("Simple", Geometry.bounds(a, origin), CoreMatchers.is(new Dimension(-1, -1)));

        Assert.assertThat("Axis-crossing", Geometry.bounds(b, a), CoreMatchers.is(new Dimension(2, 2)));
        Assert.assertThat("Axis-crossing,reversed", Geometry.bounds(a, b), CoreMatchers.is(new Dimension(-2, -2)));

        Assert.assertThat("Opposing signs", Geometry.bounds(new Point(1, -1), new Point(-1, 1)),
                CoreMatchers.is(new Dimension(-2, 2)));
        Assert.assertThat("Opposing signs, reversed", Geometry.bounds(new Point(-1, 1), new Point(1, -1)),
                CoreMatchers.is(new Dimension(2, -2)));
    }

    /**
     * @see Geometry#containsExclusive(double,double,double)
     */
    @Test
    public void testContainsExclusive() {
        Assert.assertThat("Simple", Geometry.containsExclusive(0, 2, 1), CoreMatchers.is(true));
        Assert.assertThat("Simple, reversed", Geometry.containsExclusive(2, 0, 1), CoreMatchers.is(true));

        Assert.assertThat("Under", Geometry.containsExclusive(0, 2, -1), CoreMatchers.is(false));
        Assert.assertThat("Over", Geometry.containsExclusive(0, 2, 3), CoreMatchers.is(false));

        Assert.assertThat("Upper bound", Geometry.containsExclusive(0, 1, 1), CoreMatchers.is(false));
        Assert.assertThat("Lower bound", Geometry.containsExclusive(0, 1, 0), CoreMatchers.is(false));
    }

    /**
     * @see Geometry#containsInclusive(double,double,double)
     */
    @Test
    public void testContainsInclusive() {
        Assert.assertThat("Simple", Geometry.containsInclusive(0, 2, 1), CoreMatchers.is(true));
        Assert.assertThat("Simple, reversed", Geometry.containsInclusive(2, 0, 1), CoreMatchers.is(true));

        Assert.assertThat("Under", Geometry.containsInclusive(0, 2, -1), CoreMatchers.is(false));
        Assert.assertThat("Over", Geometry.containsInclusive(0, 2, 3), CoreMatchers.is(false));

        Assert.assertThat("Upper bound", Geometry.containsInclusive(0, 1, 1), CoreMatchers.is(true));
        Assert.assertThat("Lower bound", Geometry.containsInclusive(0, 1, 0), CoreMatchers.is(true));
    }

    /**
     * @see Geometry#half(java.awt.Dimension)
     */
    @Test
    public void testHalf() {
        Dimension dim = new Dimension(100, 100);
        Geometry.half(dim);
        Assert.assertThat(dim, CoreMatchers.is(new Dimension(100 / 2, 100 / 2)));

        dim = new Dimension(0, 0);
        Geometry.half(dim);
        Assert.assertThat("Empty dimension", dim, CoreMatchers.is(new Dimension(0, 0)));
    }

    /**
     * @see Geometry#interpolate(java.awt.Point,java.awt.Point,double)
     */
    @Test
    public void testInterpolate() {
        final Point source = new Point(0, 0);
        final Point dest = new Point(2, 2);

        final Point mid = Geometry.interpolate(source, dest, .5);
        Assert.assertThat(mid, CoreMatchers.is(new Point(1, 1)));

        Assert.assertThat(Geometry.interpolate(source, dest, 0), CoreMatchers.is(source));
        Assert.assertThat(Geometry.interpolate(source, dest, 1), CoreMatchers.is(dest));
    }

    /**
     * @see Geometry#multiply(java.awt.Dimension,double)
     */
    @Test
    public void testMultiply() {
        Dimension dim = new Dimension(100, 100);
        Geometry.multiply(dim, 1);
        Assert.assertThat("Identity", dim, CoreMatchers.is(new Dimension(100, 100)));

        dim = new Dimension(100, 100);
        Geometry.multiply(dim, 2);
        Assert.assertThat("Double", dim, CoreMatchers.is(new Dimension(100 * 2, 100 * 2)));

        dim = new Dimension(100, 100);
        Geometry.multiply(dim, 0);
        Assert.assertThat("Zero scale", dim, CoreMatchers.is(new Dimension(0, 0)));

        dim = new Dimension(0, 0);
        Geometry.multiply(dim, 2);
        Assert.assertThat("Double, empty dimension", dim, CoreMatchers.is(new Dimension(0, 0)));

        dim = new Dimension(100, 100);
        Geometry.half(dim);
        Assert.assertThat("half", dim, CoreMatchers.is(new Dimension(50, 50)));

        dim = new Dimension(100, 100);
        Geometry.flip(dim);
        Assert.assertThat("flip", dim, CoreMatchers.is(new Dimension(-100, -100)));

        dim = new Dimension(100, 100);
        Geometry.flipOverX(dim);
        Assert.assertThat("flipOverX", dim, CoreMatchers.is(new Dimension(100, -100)));

        dim = new Dimension(100, 100);
        Geometry.flipOverY(dim);
        Assert.assertThat("flipOverY", dim, CoreMatchers.is(new Dimension(-100, 100)));
    }

    /**
     * @see Geometry#growToSquare(java.awt.Dimension)
     */
    @Test
    public void testSquareByGrowth() {
        Dimension dim = new Dimension(100, 100);
        Geometry.growToSquare(dim);
        Assert.assertThat("Identity", dim, CoreMatchers.is(new Dimension(100, 100)));

        dim = new Dimension(200, 100);
        Geometry.growToSquare(dim);
        Assert.assertThat("Positive", dim, CoreMatchers.is(new Dimension(200, 200)));

        dim = new Dimension(100, 200);
        Geometry.growToSquare(dim);
        Assert.assertThat("Positive, reversed", dim, CoreMatchers.is(new Dimension(200, 200)));

        dim = new Dimension(-200, -100);
        Geometry.growToSquare(dim);
        Assert.assertThat("Both negative", dim, CoreMatchers.is(new Dimension(-200, -200)));

        dim = new Dimension(-100, -200);
        Geometry.growToSquare(dim);
        Assert.assertThat("Both negative, reversed", dim, CoreMatchers.is(new Dimension(-200, -200)));

        dim = new Dimension(100, -200);
        Geometry.growToSquare(dim);
        Assert.assertThat("Opposing signs (positive is smaller)", dim, CoreMatchers.is(new Dimension(200, -200)));

        dim = new Dimension(200, -100);
        Geometry.growToSquare(dim);
        Assert.assertThat("Opposing signs, reversed (positive is bigger)", dim,
                CoreMatchers.is(new Dimension(200, -200)));
    }

    /**
     * @see Geometry#trimToSquare(java.awt.Dimension)
     */
    @Test
    public void testSquareByTrim() {

        Dimension dim = new Dimension(100, 100);
        Geometry.trimToSquare(dim);
        Assert.assertThat("Identity", dim, CoreMatchers.is(new Dimension(100, 100)));

        dim = new Dimension(200, 100);
        Geometry.trimToSquare(dim);
        Assert.assertThat("Positive", dim, CoreMatchers.is(new Dimension(100, 100)));

        dim = new Dimension(100, 200);
        Geometry.trimToSquare(dim);
        Assert.assertThat("Positive, reversed", dim, CoreMatchers.is(new Dimension(100, 100)));

        dim = new Dimension(-200, -100);
        Geometry.trimToSquare(dim);
        Assert.assertThat("Both negative", dim, CoreMatchers.is(new Dimension(-100, -100)));

        dim = new Dimension(-100, -200);
        Geometry.trimToSquare(dim);
        Assert.assertThat("Both negative, reversed", dim, CoreMatchers.is(new Dimension(-100, -100)));

        dim = new Dimension(100, -200);
        Geometry.trimToSquare(dim);
        Assert.assertThat("Opposing signs (positive is smaller)", dim, CoreMatchers.is(new Dimension(100, -100)));

        dim = new Dimension(200, -100);
        Geometry.trimToSquare(dim);
        Assert.assertThat("Opposing signs, reversed (positive is bigger)", dim,
                CoreMatchers.is(new Dimension(100, -100)));
    }

    /**
     * @see Geometry#subtractInsets(Dimension, java.awt.Insets)
     */
    @Test
    public void testSubtractInsetsWithDimension() {
        Dimension dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(10));
        Assert.assertThat("Simple", dim, CoreMatchers.is(new Dimension(80, 80)));

        dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(0));
        Assert.assertThat("Identity", dim, CoreMatchers.is(new Dimension(100, 100)));

        dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(10, 20));
        Assert.assertThat("Rectangular insets", dim, CoreMatchers.is(new Dimension(80, 60)));

        dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(20, 10));
        Assert.assertThat("Rectangular insets, reversed", dim, CoreMatchers.is(new Dimension(60, 80)));

        dim = new Dimension(-100, -100);
        Geometry.subtractInsets(dim, Geometry.insets(10, 10));
        Assert.assertThat("Both negative", dim, CoreMatchers.is(new Dimension(-80, -80)));

        dim = new Dimension(-100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(10, 10));
        Assert.assertThat("Opposing signs", dim, CoreMatchers.is(new Dimension(-80, 80)));

        dim = new Dimension(100, -100);
        Geometry.subtractInsets(dim, Geometry.insets(10, 10));
        Assert.assertThat("Opposing signs, reversed", dim, CoreMatchers.is(new Dimension(80, -80)));

        dim = new Dimension(10, 10);
        Geometry.subtractInsets(dim, Geometry.insets(20, 20));
        Assert.assertThat("Big insets cause zero-size dimensions", dim, CoreMatchers.is(new Dimension(0, 0)));
    }

    /**
     * @see Geometry#subtractInsets(Dimension, java.awt.Insets)
     */
    @Test
    public void testSubtractInsetsWithDimensionAndNegativeInsets() {
        Dimension dim = new Dimension(100, 100);
        Geometry.subtractInsets(dim, Geometry.insets(10, -10));
        Assert.assertThat("Negative inset height", dim, CoreMatchers.is(new Dimension(80, 120)));

        dim = new Dimension(5, 5);
        Geometry.subtractInsets(dim, Geometry.insets(-10, -10));
        Assert.assertThat("Negative inset values are never converted to zero", dim, CoreMatchers.is(new Dimension(25,
                25)));
    }

    /**
     * @see Geometry#subtractInsets(java.awt.Rectangle,java.awt.Insets)
     */
    @Test
    public void testSubtractInsetsWithRectangle() {
        Rectangle rect = new Rectangle(100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(10));
        Assert.assertThat("Simple", rect, CoreMatchers.is(new Rectangle(10, 10, 80, 80)));

        rect = new Rectangle(100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(0));
        Assert.assertThat("Identity", rect, CoreMatchers.is(new Rectangle(100, 100)));

        rect = new Rectangle(100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(10, 20));
        Assert.assertThat("Rectangular insets", rect, CoreMatchers.is(new Rectangle(10, 20, 80, 60)));

        rect = new Rectangle(100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(20, 10));
        Assert.assertThat("Rectangular insets, reversed", rect, CoreMatchers.is(new Rectangle(20, 10, 60, 80)));

        rect = new Rectangle(-100, -100);
        Geometry.subtractInsets(rect, Geometry.insets(10, 10));
        Assert.assertThat("Both negative", rect, CoreMatchers.is(new Rectangle(-10, -10, -80, -80)));

        rect = new Rectangle(-100, 100);
        Geometry.subtractInsets(rect, Geometry.insets(10, 10));
        Assert.assertThat("Opposing signs", rect, CoreMatchers.is(new Rectangle(-10, 10, -80, 80)));

        rect = new Rectangle(100, -100);
        Geometry.subtractInsets(rect, Geometry.insets(10, 10));
        Assert.assertThat("Opposing signs, reversed", rect, CoreMatchers.is(new Rectangle(10, -10, 80, -80)));

        rect = new Rectangle(10, 10);
        Geometry.subtractInsets(rect, Geometry.insets(20, 20));
        Assert.assertThat("Big insets cause zero-size rectangle", rect, CoreMatchers.is(new Rectangle(5, 5, 0, 0)));
    }
}
