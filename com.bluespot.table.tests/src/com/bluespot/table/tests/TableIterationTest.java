package com.bluespot.table.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.table.ArrayTable;
import com.bluespot.table.Table;
import com.bluespot.table.iteration.TableIteration;

public abstract class TableIterationTest<T> {

    protected TableIteration strategy;

    protected Table<T> table;

    protected Table<T> newTable(int width, int height) {
        return new ArrayTable<T>(width, height);
    }

    protected abstract TableIteration newTableIteration();

    @Before
    public void setUp() {
        this.table = this.newTable(2, 2);
        this.strategy = this.newTableIteration();
    }

    /**
     * Returns the origin according to this iteration strategy.
     * 
     * @return the value of the origin
     */
    protected Point getOrigin() {
        return new Point(0, 0);
    }

    /**
     * Returns the last point according to this iteration strategy.
     * 
     * @return the value of the last iterated point
     */
    protected Point getLastPoint() {
        return new Point(1, 1);
    }

    /**
     * Returns a wrapped point that should never be wrapped.
     * <p>
     * The point will be wrapped using a 2x2 table.
     * 
     * @return the value of a point that is never wrapped
     */
    protected Point getUnwrappedPoint() {
        return new Point(0, 1);
    }

    @Test
    public void testOrigin() {
        assertThat(this.strategy.wrap(this.table, this.getOrigin()), is(this.getOrigin()));
    }

    @Test
    public void testNextPoint() {
        assertThat(this.strategy.wrap(this.table, this.getUnwrappedPoint()), is(this.getUnwrappedPoint()));
    }

    /**
     * Returns a wrapped point. The point will be wrapped on a 2x2 table.
     * 
     * @return the value of the wrapped point that is equivalent to (3, 0)
     */
    protected abstract Point getTwoColumnsAfterEnd();

    @Test
    public void testTwoColumnsAfterEnd() {
        assertThat(this.strategy.wrap(this.table, new Point(3, 0)), is(this.getTwoColumnsAfterEnd()));
    }

    /**
     * Returns a wrapped point. The point will be wrapped on a 2x2 table.
     * 
     * @return the value of the wrapped point that is equivalent to (0, 3)
     */
    protected abstract Point getTwoRowsAfterEnd();

    @Test
    public void testTwoRowsAfterEnd() {
        assertThat(this.strategy.wrap(this.table, new Point(0, 3)), is(this.getTwoRowsAfterEnd()));
    }

    /**
     * Returns a wrapped point. The point will be wrapped on a 2x2 table.
     * 
     * @return the value of the wrapped point that is equivalent to (-3, 0)
     */
    protected abstract Point getThreeColumnsBeforeOrigin();

    @Test
    public void testThreeColumnsBeforeOrigin() {
        assertThat(this.strategy.wrap(this.table, new Point(-3, 0)), is(getThreeColumnsBeforeOrigin()));
    }

    /**
     * Returns a wrapped point. The point will be wrapped on a 2x2 table.
     * 
     * @return the value of the wrapped point that is equivalent to (0, -3)
     */
    protected abstract Point getThreeRowsBeforeOrigin();

    @Test
    public void testThreeRowsBeforeOrigin() {
        assertThat(this.strategy.wrap(this.table, new Point(0, -3)), is(getThreeRowsBeforeOrigin()));
    }

    /**
     * Returns a wrapped point. The point will be wrapped on a 2x2 table.
     * 
     * @return the value of the wrapped point that is equivalent to (-1, 0)
     */
    protected abstract Point getOneColumnBeforeOrigin();

    @Test
    public void testOneColumnBeforeOrigin() {
        assertThat(this.strategy.wrap(this.table, new Point(-1, 0)), is(getOneColumnBeforeOrigin()));
    }

    /**
     * Returns a wrapped point. The point will be wrapped on a 2x2 table.
     * 
     * @return the value of the wrapped point that is equivalent to (0, -1)
     */
    protected abstract Point getOneRowBeforeOrigin();

    @Test
    public void testOneRowBeforeOrigin() {
        assertThat(this.strategy.wrap(this.table, new Point(0, -1)), is(getOneRowBeforeOrigin()));
    }

    /**
     * Returns a wrapped point. The point will be wrapped on a 2x2 table.
     * 
     * @return the value of the wrapped point that is equivalent to (2, 0)
     */
    protected abstract Point getExtraColumn();

    @Test
    public void testExtraColumn() {
        assertThat(this.strategy.wrap(this.table, new Point(2, 0)), is(getExtraColumn()));
    }

    /**
     * Returns a wrapped point. The point will be wrapped on a 2x2 table.
     * 
     * @return the value of the wrapped point that is equivalent to (0, 2)
     */
    protected abstract Point getExtraRow();

    @Test
    public void testExtraRow() {
        assertThat(this.strategy.wrap(this.table, new Point(0, 2)), is(getExtraRow()));
    }

    @Test
    public void testCompareLessThan() {
        Point point = this.getOrigin();
        this.strategy.next(point);
        assertTrue(this.strategy.comparePoints(this.table, getOrigin(), point) < 0);
    }

    @Test
    public void testCompareGreaterThan() {
        Point point = this.getOrigin();
        this.strategy.next(point);
        assertTrue(this.strategy.comparePoints(this.table, getLastPoint(), point) > 0);
    }

    @Test
    public void testCompareEqual() {
        Point point = this.getLastPoint();
        this.strategy.next(point);
        assertThat(this.strategy.comparePoints(this.table, getOrigin(), this.strategy.wrap(this.table, point)), is(0));
    }

    @Test
    public void testCompareEqualWrapped() {
        Point point = this.getLastPoint();
        this.strategy.next(point);
        assertTrue(this.strategy.comparePoints(this.table, getOrigin(), point) == 0);
    }
}
