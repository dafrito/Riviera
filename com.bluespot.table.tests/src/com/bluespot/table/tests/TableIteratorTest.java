package com.bluespot.table.tests;

import java.awt.Point;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bluespot.table.ArrayTable;
import com.bluespot.table.Table;
import com.bluespot.table.TableIterator;
import com.bluespot.table.Tables;

public abstract class TableIteratorTest {

	protected TableIterator<Integer> iter;
	protected Table<Integer> table;

	public abstract TableIterator<Integer> newIterator(Table<Integer> targetTable);

	public Table<Integer> newTable(final int width, final int height) {
		return new ArrayTable<Integer>(width, height);
	}

	@Before
	public void setUp() {
		this.table = this.newTable(2, 2);
		this.iter = this.newIterator(this.table);
	}

	@Test
	public void testEmptyTable() {
		Assert.assertThat(this.newIterator(this.newTable(0, 0)).hasNext(), CoreMatchers.is(false));
		Assert.assertThat(this.newIterator(this.newTable(0, 0)).hasPrevious(), CoreMatchers.is(false));
	}

	@Test
	public void testHasNext() {
		Assert.assertThat(this.newIterator(this.newTable(0, 0)).hasNext(), CoreMatchers.is(false));
		this.iter = this.newIterator(this.newTable(1, 1));
		Assert.assertThat(this.iter.hasNext(), CoreMatchers.is(true));
		Assert.assertThat(this.iter.next(), CoreMatchers.is((Integer) null));
		Assert.assertThat(this.iter.hasNext(), CoreMatchers.is(false));
	}

	@Test
	public void testHasPrevious() {
		this.iter = this.newIterator(this.table = this.newTable(1, 1));
		Tables.fill(this.table, 1);
		Assert.assertThat(this.iter.hasPrevious(), CoreMatchers.is(false));
		Assert.assertThat(this.iter.next(), CoreMatchers.is(1));
		Assert.assertThat(this.iter.hasNext(), CoreMatchers.is(false));
		Assert.assertThat(this.iter.hasPrevious(), CoreMatchers.is(false));
	}

	@Test
	public void testImplicitGetLocation() {
		Assert.assertNotNull(this.iter.getLocation());
	}

	@Test
	public void testImplicitGetLocationWithProvidedPoint() {
		this.iter.getLocation(new Point());
	}

	@Test
	public void testImplicitPut() {
		Tables.fill(this.table, 1);
		Assert.assertThat(this.iter.put(2), CoreMatchers.is(1));
		Assert.assertThat(this.iter.get(), CoreMatchers.is(2));
	}

	@Test
	public void testImplicitRemove() {
		this.iter.remove();
		Assert.assertThat(this.iter.get(), CoreMatchers.is((Integer) null));
	}

	@Test
	public void testPrevious() {
		this.iter = this.newIterator(this.table = this.newTable(2, 1));
		Tables.fillCount(this.table, 1);
		this.iter.next();
		this.iter.next();
		Assert.assertThat(this.iter.hasNext(), CoreMatchers.is(false));
		Assert.assertThat(this.iter.hasPrevious(), CoreMatchers.is(true));
		Assert.assertThat(this.iter.previous(), CoreMatchers.is(1));
		Assert.assertThat(this.iter.hasPrevious(), CoreMatchers.is(false));
		Assert.assertThat(this.iter.hasNext(), CoreMatchers.is(true));
	}

	@Test
	public void testThrowOnGet() {
		Tables.fill(this.table, 1);
		Assert.assertThat(this.iter.get(), CoreMatchers.is(1));
	}
}
