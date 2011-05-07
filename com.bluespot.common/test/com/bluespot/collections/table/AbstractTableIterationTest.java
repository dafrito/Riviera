package com.bluespot.collections.table;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.collections.table.iteration.TableIteration;

public abstract class AbstractTableIterationTest<T> {

	protected TableIteration strategy;

	protected Table<T> table;

	@Before
	public void setUp() {
		this.table = this.newTable(2, 2);
		this.strategy = this.newTableIteration();
	}

	@Test
	public void testCompareEqual() {
		final Point point = this.getLastPoint();
		this.strategy.next(point);
		assertThat(this.strategy.comparePoints(this.table, this.getOrigin(), this.strategy.wrap(this.table, point)),
				is(0));
	}

	@Test
	public void testCompareEqualWrapped() {
		final Point point = this.getLastPoint();
		this.strategy.next(point);
		assertTrue(this.strategy.comparePoints(this.table, this.getOrigin(), point) == 0);
	}

	@Test
	public void testCompareGreaterThan() {
		final Point point = this.getOrigin();
		this.strategy.next(point);
		assertTrue(this.strategy.comparePoints(this.table, this.getLastPoint(), point) > 0);
	}

	@Test
	public void testCompareLessThan() {
		final Point point = this.getOrigin();
		this.strategy.next(point);
		assertTrue(this.strategy.comparePoints(this.table, this.getOrigin(), point) < 0);
	}

	@Test
	public void testExtraColumn() {
		assertThat(this.strategy.wrap(this.table, new Point(2, 0)), is(this.getExtraColumn()));
	}

	@Test
	public void testExtraRow() {
		assertThat(this.strategy.wrap(this.table, new Point(0, 2)), is(this.getExtraRow()));
	}

	@Test
	public void testNextPoint() {
		assertThat(this.strategy.wrap(this.table, this.getUnwrappedPoint()), is(this.getUnwrappedPoint()));
	}

	@Test
	public void testOneColumnBeforeOrigin() {
		assertThat(this.strategy.wrap(this.table, new Point(-1, 0)), is(this.getOneColumnBeforeOrigin()));
	}

	@Test
	public void testOneRowBeforeOrigin() {
		assertThat(this.strategy.wrap(this.table, new Point(0, -1)), is(this.getOneRowBeforeOrigin()));
	}

	@Test
	public void testOrigin() {
		assertThat(this.strategy.wrap(this.table, this.getOrigin()), is(this.getOrigin()));
	}

	@Test
	public void testSimpleIteration() {
		final Point point = this.getOrigin();
		this.strategy.previous(point);
		this.strategy.next(point);
		assertThat(point, is(this.getOrigin()));
	}

	@Test
	public void testThreeColumnsBeforeOrigin() {
		assertThat(this.strategy.wrap(this.table, new Point(-3, 0)), is(this.getThreeColumnsBeforeOrigin()));
	}

	@Test
	public void testThreeRowsBeforeOrigin() {
		assertThat(this.strategy.wrap(this.table, new Point(0, -3)), is(this.getThreeRowsBeforeOrigin()));
	}

	@Test
	public void testTwoColumnsAfterEnd() {
		assertThat(this.strategy.wrap(this.table, new Point(3, 0)), is(this.getTwoColumnsAfterEnd()));
	}

	@Test
	public void testTwoRowsAfterEnd() {
		assertThat(this.strategy.wrap(this.table, new Point(0, 3)), is(this.getTwoRowsAfterEnd()));
	}

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (2, 0)
	 */
	 protected abstract Point getExtraColumn();

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (0, 2)
	 */
	 protected abstract Point getExtraRow();

	 /**
	  * Returns the last point according to this iteration strategy.
	  * 
	  * @return the value of the last iterated point
	  */
	 protected Point getLastPoint() {
		 return new Point(1, 1);
	 }

	 /**
	  * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	  * 
	  * @return the value of the wrapped point that is equivalent to (-1, 0)
	  */
	 protected abstract Point getOneColumnBeforeOrigin();

	 /**
	  * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	  * 
	  * @return the value of the wrapped point that is equivalent to (0, -1)
	  */
	 protected abstract Point getOneRowBeforeOrigin();

	 /**
	  * Returns the origin according to this iteration strategy.
	  * 
	  * @return the value of the origin
	  */
	 protected Point getOrigin() {
		 return new Point(0, 0);
	 }

	 /**
	  * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	  * 
	  * @return the value of the wrapped point that is equivalent to (-3, 0)
	  */
	 protected abstract Point getThreeColumnsBeforeOrigin();

	 /**
	  * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	  * 
	  * @return the value of the wrapped point that is equivalent to (0, -3)
	  */
	 protected abstract Point getThreeRowsBeforeOrigin();

	 /**
	  * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	  * 
	  * @return the value of the wrapped point that is equivalent to (3, 0)
	  */
	 protected abstract Point getTwoColumnsAfterEnd();

	 /**
	  * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	  * 
	  * @return the value of the wrapped point that is equivalent to (0, 3)
	  */
	 protected abstract Point getTwoRowsAfterEnd();

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

	 protected Table<T> newTable(final int width, final int height) {
		 return new ArrayTable<T>(width, height);
	 }

	 protected abstract TableIteration newTableIteration();
}
