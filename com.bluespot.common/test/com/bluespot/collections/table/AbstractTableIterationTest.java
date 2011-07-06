package com.bluespot.collections.table;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.collections.table.iteration.TableIteration;
import com.bluespot.geom.vectors.Vector3i;

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
		final Vector3i point = this.getLastPoint().toMutable();
		this.strategy.next(point);
		assertThat(this.strategy.comparePoints(this.table, this.getOrigin(), this.strategy.wrap(this.table, point)),
				is(0));
	}

	@Test
	public void testCompareEqualWrapped() {
		final Vector3i point = this.getLastPoint().toMutable();
		this.strategy.next(point);
		assertTrue(this.strategy.comparePoints(this.table, this.getOrigin(), point) == 0);
	}

	@Test
	public void testCompareGreaterThan() {
		final Vector3i point = this.getOrigin().toMutable();
		this.strategy.next(point);
		assertTrue(this.strategy.comparePoints(this.table, this.getLastPoint(), point) > 0);
	}

	@Test
	public void testCompareLessThan() {
		final Vector3i point = this.getOrigin().toMutable();
		this.strategy.next(point);
		assertTrue(this.strategy.comparePoints(this.table, this.getOrigin(), point) < 0);
	}

	@Test
	public void testExtraColumn() {
		assertTrue(this.strategy.wrap(this.table, Vector3i.frozen(2, 0)).at(this.getExtraColumn()));
	}

	@Test
	public void testExtraRow() {
		assertTrue(this.strategy.wrap(this.table, Vector3i.frozen(0, 2)).at(this.getExtraRow()));
	}

	@Test
	public void testNextPoint() {
		assertTrue(this.strategy.wrap(this.table, this.getUnwrappedPoint()).at(this.getUnwrappedPoint()));
	}

	@Test
	public void testOneColumnBeforeOrigin() {
		assertTrue(this.strategy.wrap(this.table, Vector3i.frozen(-1, 0)).at(this.getOneColumnBeforeOrigin()));
	}

	@Test
	public void testOneRowBeforeOrigin() {
		assertTrue(this.strategy.wrap(this.table, Vector3i.frozen(0, -1)).at(this.getOneRowBeforeOrigin()));
	}

	@Test
	public void testOrigin() {
		assertTrue(this.strategy.wrap(this.table, this.getOrigin()).at(this.getOrigin()));
	}

	@Test
	public void testSimpleIteration() {
		final Vector3i point = this.getOrigin().toMutable();
		this.strategy.previous(point);
		this.strategy.next(point);
		assertTrue(point.at(this.getOrigin()));
	}

	@Test
	public void testThreeColumnsBeforeOrigin() {
		assertTrue(this.strategy.wrap(this.table, Vector3i.frozen(-3, 0)).at(this.getThreeColumnsBeforeOrigin()));
	}

	@Test
	public void testThreeRowsBeforeOrigin() {
		assertTrue(this.strategy.wrap(this.table, Vector3i.frozen(0, -3)).at(this.getThreeRowsBeforeOrigin()));
	}

	@Test
	public void testTwoColumnsAfterEnd() {
		assertTrue(this.strategy.wrap(this.table, Vector3i.frozen(3, 0)).at(this.getTwoColumnsAfterEnd()));
	}

	@Test
	public void testTwoRowsAfterEnd() {
		assertTrue(this.strategy.wrap(this.table, Vector3i.frozen(0, 3)).at(this.getTwoRowsAfterEnd()));
	}

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (2, 0)
	 */
	protected abstract Vector3i getExtraColumn();

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (0, 2)
	 */
	protected abstract Vector3i getExtraRow();

	/**
	 * Returns the last point according to this iteration strategy.
	 * 
	 * @return the value of the last iterated point
	 */
	protected Vector3i getLastPoint() {
		return Vector3i.frozen(1, 1);
	}

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (-1, 0)
	 */
	protected abstract Vector3i getOneColumnBeforeOrigin();

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (0, -1)
	 */
	protected abstract Vector3i getOneRowBeforeOrigin();

	/**
	 * Returns the origin according to this iteration strategy.
	 * 
	 * @return the value of the origin
	 */
	protected Vector3i getOrigin() {
		return Vector3i.origin();
	}

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (-3, 0)
	 */
	protected abstract Vector3i getThreeColumnsBeforeOrigin();

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (0, -3)
	 */
	protected abstract Vector3i getThreeRowsBeforeOrigin();

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (3, 0)
	 */
	protected abstract Vector3i getTwoColumnsAfterEnd();

	/**
	 * Returns a wrapped point. The point will be wrapped on a 2x2 table.
	 * 
	 * @return the value of the wrapped point that is equivalent to (0, 3)
	 */
	protected abstract Vector3i getTwoRowsAfterEnd();

	/**
	 * Returns a wrapped point that should never be wrapped.
	 * <p>
	 * The point will be wrapped using a 2x2 table.
	 * 
	 * @return the value of a point that is never wrapped
	 */
	protected Vector3i getUnwrappedPoint() {
		return Vector3i.frozen(0, 1);
	}

	protected Table<T> newTable(final int width, final int height) {
		return new ArrayTable<T>(width, height);
	}

	protected abstract TableIteration newTableIteration();
}
