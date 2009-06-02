package com.bluespot.table.tests;

import java.awt.Dimension;
import java.awt.Point;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bluespot.table.StrategyTableIterator;
import com.bluespot.table.Table;
import com.bluespot.table.TableIterator;
import com.bluespot.table.Tables;
import com.bluespot.table.iteration.NaturalTableIteration;

public abstract class TableTest<T> {

	private final T defaultValue;
	private Table<T> table;

	public TableTest(final T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public abstract Table<T> newTable(int width, int height);

	@Before
	public void setUp() {
		this.table = this.newTable(2, 2);
	}

	@Test
	public void testClear() {
		final Point point = new Point(0, 0);
		this.table.put(point, this.getValue());
		this.table.put(new Point(1, 0), this.getOtherValue());
		this.table.clear();
		Assert.assertThat(this.table.get(point), CoreMatchers.is(this.getDefaultValue()));
		Assert.assertThat(this.table.get(new Point(1, 0)), CoreMatchers.is(this.getDefaultValue()));
	}

	@Test
	public void testConvenienceSubTable() {
		this.table = this.newTable(2, 1);
		final Table<T> subTable = this.table.subTable(new Point(1, 0));
		Assert.assertThat(subTable.size(), CoreMatchers.is(1));
	}

	@Test
	public void testEmptyClear() {
		this.newTable(0, 0).clear();
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testExcessiveHeightSubTable() {
		this.table.subTable(new Point(0, 0), new Dimension(0, 3));
	}

	// Get

	@Test(expected = IndexOutOfBoundsException.class)
	public void testExcessiveWidthSubTable() {
		this.table.subTable(new Point(0, 0), new Dimension(3, 0));
	}

	@Test
	public void testFill() {
		this.table = this.newTable(2, 2);
		Tables.fill(this.table, this.getValue());
		for (final T num : this.table) {
			Assert.assertThat(num, CoreMatchers.is(this.getValue()));
		}
	}

	@Test
	public void testGetPutAndRemove() {
		final Point point = new Point(0, 0);
		Assert.assertThat(this.table.get(point), CoreMatchers.is(this.getDefaultValue()));
		Assert.assertThat(this.table.put(point, this.getValue()), CoreMatchers.is(this.getDefaultValue()));
		Assert.assertThat(this.table.get(point), CoreMatchers.is(this.getValue()));
		Assert.assertThat(this.table.put(point, this.getOtherValue()), CoreMatchers.is(this.getValue()));
		Assert.assertThat(this.table.remove(point), CoreMatchers.is(this.getOtherValue()));
		Assert.assertThat(this.table.get(point), CoreMatchers.is(this.getDefaultValue()));
	}

	@Test
	public void testHeight() {
		Assert.assertThat(this.newTable(1, 1).getHeight(), CoreMatchers.is(1));
		Assert.assertThat(this.newTable(2, 4).getHeight(), CoreMatchers.is(4));
	}

	@Test
	public void testNaturalTableIteration() {
		this.table = this.newTable(2, 2);
		Tables.fill(this.table, this.listOfValues());
		final TableIterator<T> iterator = new StrategyTableIterator<T>(this.table, NaturalTableIteration.getInstance());

		Assert.assertThat(iterator.next(), CoreMatchers.is(this.listOfValues()[0]));
		Assert.assertThat(iterator.getLocation(), CoreMatchers.is(new Point(0, 0)));
		Assert.assertThat(iterator.hasNext(), CoreMatchers.is(true));

		Assert.assertThat(iterator.next(), CoreMatchers.is(this.listOfValues()[1]));
		Assert.assertThat(iterator.getLocation(), CoreMatchers.is(new Point(1, 0)));
		Assert.assertThat(iterator.hasNext(), CoreMatchers.is(true));

		Assert.assertThat(iterator.next(), CoreMatchers.is(this.listOfValues()[2]));
		Assert.assertThat(iterator.getLocation(), CoreMatchers.is(new Point(0, 1)));
		Assert.assertThat(iterator.hasNext(), CoreMatchers.is(true));

		Assert.assertThat(iterator.next(), CoreMatchers.is(this.listOfValues()[3]));
		Assert.assertThat(iterator.getLocation(), CoreMatchers.is(new Point(1, 1)));
		Assert.assertThat(iterator.hasNext(), CoreMatchers.is(false));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testNegativeHeightSubTable() {
		this.table.subTable(new Point(0, -1), new Dimension(1, 1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testNegativeWidthSubTable() {
		this.table.subTable(new Point(-1, 0), new Dimension(1, 1));
	}

	// Put

	@Test
	public void testSize() {
		Assert.assertThat(this.newTable(1, 1).size(), CoreMatchers.is(1));
		Assert.assertThat(this.newTable(2, 2).size(), CoreMatchers.is(4));
		Assert.assertThat(this.newTable(2, 4).size(), CoreMatchers.is(8));
	}

	@Test
	public void testSubTable() {
		this.table = this.newTable(2, 1);
		final Table<T> subTable = this.table.subTable(new Point(1, 0), new Dimension(1, 1));
		Assert.assertThat(subTable.size(), CoreMatchers.is(1));
	}

	@Test
	public void testSubTableClear() {
		this.table = this.newTable(4, 1);
		Tables.fill(this.table, this.listOfValues());
		final Table<T> subTable = this.table.subTable(new Point(2, 0));
		subTable.clear();
		Assert.assertThat(this.table.get(new Point(0, 0)), CoreMatchers.is(this.listOfValues()[0]));
		Assert.assertThat(this.table.get(new Point(1, 0)), CoreMatchers.is(this.listOfValues()[1]));
		Assert.assertThat(this.table.get(new Point(2, 0)), CoreMatchers.is(this.getDefaultValue()));
		Assert.assertThat(this.table.get(new Point(3, 0)), CoreMatchers.is(this.getDefaultValue()));
	}

	@Test
	public void testSubTableGet() {
		this.table = this.newTable(4, 1);
		Tables.fill(this.table, this.listOfValues());
		final Table<T> subTable = this.table.subTable(new Point(2, 0));
		Assert.assertThat(subTable.size(), CoreMatchers.is(2));
		Assert.assertThat(subTable.get(new Point(0, 0)), CoreMatchers.is(this.listOfValues()[2]));
		Assert.assertThat(subTable.get(new Point(1, 0)), CoreMatchers.is(this.listOfValues()[3]));
	}

	// Remove

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubTableInsidiousGet() {
		this.table = this.newTable(4, 1);
		final Table<T> subTable = this.table.subTable(new Point(2, 0));
		subTable.remove(new Point(-1, 0));
	}

	@Test
	public void testSubTablePut() {
		this.table = this.newTable(4, 1);
		Tables.fill(this.table, this.listOfValues());
		final Table<T> subTable = this.table.subTable(new Point(2, 0));
		Tables.fill(subTable, this.otherListOfValues());
		Assert.assertThat(this.table.get(new Point(0, 0)), CoreMatchers.is(this.listOfValues()[0]));
		Assert.assertThat(this.table.get(new Point(1, 0)), CoreMatchers.is(this.listOfValues()[1]));
		Assert.assertThat(this.table.get(new Point(2, 0)), CoreMatchers.is(this.otherListOfValues()[0]));
		Assert.assertThat(this.table.get(new Point(3, 0)), CoreMatchers.is(this.otherListOfValues()[1]));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnGetWithExcessiveX() {
		this.table.get(new Point(2, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnGetWithExcessiveY() {
		this.table.get(new Point(0, 2));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnGetWithNegativeX() {
		this.table.get(new Point(-1, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnGetWithNegativeY() {
		this.table.get(new Point(0, -1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnPutWithExcessiveX() {
		this.table.put(new Point(2, 0), this.getValue());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnPutWithExcessiveY() {
		this.table.put(new Point(0, 2), this.getValue());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnPutWithNegativeX() {
		this.table.put(new Point(-1, 0), this.getValue());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnPutWithNegativeY() {
		this.table.put(new Point(0, -1), this.getValue());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnRemoveWithExcessiveX() {
		this.table.remove(new Point(2, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnRemoveWithExcessiveY() {
		this.table.remove(new Point(0, 2));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnRemoveWithNegativeX() {
		this.table.remove(new Point(-1, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnRemoveWithNegativeY() {
		this.table.remove(new Point(0, -1));
	}

	@Test
	public void testWidth() {
		Assert.assertThat(this.newTable(1, 1).getWidth(), CoreMatchers.is(1));
		Assert.assertThat(this.newTable(2, 4).getWidth(), CoreMatchers.is(2));
	}

	protected T getDefaultValue() {
		return this.defaultValue;
	}

	protected abstract T getOtherValue();

	protected abstract T getValue();

	protected abstract T[] listOfValues();

	protected abstract T[] otherListOfValues();
}
