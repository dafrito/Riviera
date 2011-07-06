package com.bluespot.collections.table;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.collections.table.iteration.NaturalTableIteration;
import com.bluespot.geom.vectors.Vector3i;

public abstract class AbstractTableTest<T> {

	/**
	 * Constructs a new test suite using the specified value as the default
	 * default-value.
	 * 
	 * @param defaultDefaultValue
	 *            the default default value used in testing. This is used
	 *            whenever a default value is not explicitly supplied.
	 * @see #newTable(int, int, Object)
	 */
	public AbstractTableTest(final T defaultDefaultValue) {
		this.defaultDefaultValue = defaultDefaultValue;
	}

	private final T defaultDefaultValue;

	private Table<T> table;

	@Test
	public void ctorAllowsNullDefaultValue() {
		if (this.allowNullValues()) {
			this.newTable(2, 2, null);
		} else {
			try {
				this.newTable(2, 2, null);
				fail("Expected IAE on disallowed null default value");
			} catch (final IllegalArgumentException e) {
				// Expected, so continue.
			}
		}
	}

	public Table<T> newTable(final int width, final int height) {
		return this.newTable(width, height, this.getDefaultDefaultValue());
	}

	/**
	 * Creates a new table using the specified size and default value.
	 * 
	 * @param width
	 *            the width of the table
	 * @param height
	 *            the height of the table
	 * @param defaultValue
	 *            the default value used in the table
	 * @return a new {@code Table} object
	 */
	public abstract Table<T> newTable(int width, int height, T defaultValue);

	public void putThrowsIAEOnUnallowedNullValue() {
		if (this.allowNullValues()) {
			this.table.put(Vector3i.origin(), null);
		} else {
			try {
				this.table.put(Vector3i.origin(), null);
				fail("Table#put: Table should throw IAE if it does not allow default values");
			} catch (final IllegalArgumentException ex) {
				// We expected this, so continue
			}
		}
	}

	@Before
	public void setUp() {
		this.table = this.newTable(2, 2);
	}

	@Test
	public void testClear() {
		this.table.put(Vector3i.origin(), this.getValue());
		this.table.put(Vector3i.frozen(1, 0), this.getOtherValue());
		this.table.clear();
		assertThat(this.table.get(Vector3i.origin()), is(this.getDefaultDefaultValue()));
		assertThat(this.table.get(Vector3i.frozen(1, 0)), is(this.getDefaultDefaultValue()));
	}

	@Test
	public void testConvenienceSubTable() {
		this.table = this.newTable(2, 1);
		final Table<T> subTable = this.table.subTable(Vector3i.frozen(1, 0));
		assertThat(subTable.size(), is(1));
	}

	@Test
	public void testEmptyClear() {
		this.newTable(0, 0).clear();
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testExcessiveHeightSubTable() {
		this.table.subTable(Vector3i.origin(), Vector3i.frozen(0, 3));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testExcessiveWidthSubTable() {
		this.table.subTable(Vector3i.origin(), Vector3i.frozen(3, 0));
	}

	@Test
	public void testFill() {
		this.table = this.newTable(2, 2);
		Tables.fill(this.table, this.getValue());
		for (final T num : this.table) {
			assertThat(num, is(this.getValue()));
		}
	}

	@Test
	public void testGetPutAndRemove() {
		assertThat(this.table.get(Vector3i.origin()), is(this.getDefaultDefaultValue()));
		assertThat(this.table.put(Vector3i.origin(), this.getValue()), is(this.getDefaultDefaultValue()));
		assertThat(this.table.get(Vector3i.origin()), is(this.getValue()));
		assertThat(this.table.put(Vector3i.origin(), this.getOtherValue()), is(this.getValue()));
		assertThat(this.table.remove(Vector3i.origin()), is(this.getOtherValue()));
		assertThat(this.table.get(Vector3i.origin()), is(this.getDefaultDefaultValue()));
	}

	@Test
	public void testHeight() {
		assertThat(this.newTable(1, 1).height(), is(1));
		assertThat(this.newTable(2, 4).height(), is(4));
	}

	@Test
	public void testListsAreEqualButNotUnique() {
		assertThat(this.listOfValues(), is(this.listOfValues()));
		assertTrue(this.listOfValues() != this.listOfValues());
		assertThat(this.otherListOfValues(), is(this.otherListOfValues()));
		assertTrue(this.otherListOfValues() != this.otherListOfValues());
	}

	// Get

	@Test
	public void testListsOfValuesAreNonIntersecting() {
		final List<T> values = this.listOfValues();
		values.retainAll(this.otherListOfValues());
		assertTrue(values.isEmpty());
	}

	@Test
	public void testNaturalTableIteration() {
		this.table = this.newTable(2, 2);
		Tables.fill(this.table, this.listOfValues());
		final TableIterator<T> iterator = new StrategyTableIterator<T>(this.table, NaturalTableIteration.getInstance());

		assertThat(iterator.next(), is(this.listOfValues().get(0)));
		assertThat(iterator.getLocation(), is(Vector3i.origin()));
		assertThat(iterator.hasNext(), is(true));

		assertThat(iterator.next(), is(this.listOfValues().get(1)));
		assertTrue(iterator.getLocation().at(1, 0, 0));
		assertThat(iterator.hasNext(), is(true));

		assertThat(iterator.next(), is(this.listOfValues().get(2)));
		assertTrue(iterator.getLocation().at(0, 1, 0));
		assertThat(iterator.hasNext(), is(true));

		assertThat(iterator.next(), is(this.listOfValues().get(3)));
		assertTrue(iterator.getLocation().at(1, 1, 0));
		assertThat(iterator.hasNext(), is(false));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testNegativeHeightSubTable() {
		this.table.subTable(Vector3i.frozen(0, -1, 0), Vector3i.frozen(1, 1, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testNegativeWidthSubTable() {
		this.table.subTable(Vector3i.frozen(-1, 0, 0), Vector3i.frozen(1, 1, 0));
	}

	@Test
	public void testPutReturnsDefaultValue() {
		this.table = this.newTable(2, 2, this.getValue());
		assertThat(this.table.put(Vector3i.origin(), this.getValue()), is(this.getValue()));
	}

	@Test
	public void testRemoveReturnsDefaultValue() {
		this.table = this.newTable(2, 2, this.getValue());
		assertThat(this.table.remove(Vector3i.origin()), is(this.getValue()));
	}

	@Test
	public void testSize() {
		assertThat(this.newTable(1, 1).size(), is(1));
		assertThat(this.newTable(2, 2).size(), is(4));
		assertThat(this.newTable(2, 4).size(), is(8));
	}

	// Put

	@Test
	public void testSubTable() {
		this.table = this.newTable(2, 1);
		final Table<T> subTable = this.table.subTable(Vector3i.frozen(1, 0), Vector3i.frozen(1, 1));
		assertThat(subTable.size(), is(1));
	}

	@Test
	public void testSubTableClear() {
		this.table = this.newTable(4, 1);
		Tables.fill(this.table, this.listOfValues());
		final Table<T> subTable = this.table.subTable(Vector3i.frozen(2, 0));
		subTable.clear();
		assertThat(this.table.get(Vector3i.origin()), is(this.listOfValues().get(0)));
		assertThat(this.table.get(Vector3i.frozen(1, 0)), is(this.listOfValues().get(1)));
		assertThat(this.table.get(Vector3i.frozen(2, 0)), is(this.getDefaultDefaultValue()));
		assertThat(this.table.get(Vector3i.frozen(3, 0)), is(this.getDefaultDefaultValue()));
	}

	@Test
	public void testSubTableGet() {
		this.table = this.newTable(4, 1);
		Tables.fill(this.table, this.listOfValues());
		final Table<T> subTable = this.table.subTable(Vector3i.frozen(2, 0));
		assertThat(subTable.size(), is(2));
		assertThat(subTable.get(Vector3i.origin()), is(this.listOfValues().get(2)));
		assertThat(subTable.get(Vector3i.frozen(1, 0)), is(this.listOfValues().get(3)));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubTableInsidiousGet() {
		this.table = this.newTable(4, 1);
		final Table<T> subTable = this.table.subTable(Vector3i.frozen(2, 0));
		subTable.remove(Vector3i.frozen(-1, 0));
	}

	// Remove

	@Test
	public void testSubTablePut() {
		this.table = this.newTable(4, 1);
		Tables.fill(this.table, this.listOfValues());
		final Table<T> subTable = this.table.subTable(Vector3i.frozen(2, 0));
		Tables.fill(subTable, this.otherListOfValues());
		assertThat(this.table.get(Vector3i.frozen(0, 0)), is(this.listOfValues().get(0)));
		assertThat(this.table.get(Vector3i.frozen(1, 0)), is(this.listOfValues().get(1)));
		assertThat(this.table.get(Vector3i.frozen(2, 0)), is(this.otherListOfValues().get(0)));
		assertThat(this.table.get(Vector3i.frozen(3, 0)), is(this.otherListOfValues().get(1)));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnGetWithExcessiveX() {
		this.table.get(Vector3i.frozen(2, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnGetWithExcessiveY() {
		this.table.get(Vector3i.frozen(0, 2));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnGetWithNegativeX() {
		this.table.get(Vector3i.frozen(-1, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnGetWithNegativeY() {
		this.table.get(Vector3i.frozen(0, -1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnPutWithExcessiveX() {
		this.table.put(Vector3i.frozen(2, 0), this.getValue());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnPutWithExcessiveY() {
		this.table.put(Vector3i.frozen(0, 2), this.getValue());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnPutWithNegativeX() {
		this.table.put(Vector3i.frozen(-1, 0), this.getValue());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnPutWithNegativeY() {
		this.table.put(Vector3i.frozen(0, -1), this.getValue());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnRemoveWithExcessiveX() {
		this.table.remove(Vector3i.frozen(2, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnRemoveWithExcessiveY() {
		this.table.remove(Vector3i.frozen(0, 2));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnRemoveWithNegativeX() {
		this.table.remove(Vector3i.frozen(-1, 0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testThrowOnRemoveWithNegativeY() {
		this.table.remove(Vector3i.frozen(0, -1));
	}

	@Test
	public void testValuesNotNull() {
		assertThat(this.getValue(), is(notNullValue()));
		assertThat(this.getOtherValue(), is(notNullValue()));
		assertThat(this.getDefaultDefaultValue(), is(notNullValue()));
		assertTrue("list of values has no null values", !this.listOfValues().contains(null));
		assertTrue("otherListOfValues has null values", !this.otherListOfValues().contains(null));
	}

	@Test
	public void testWidth() {
		assertThat(this.newTable(1, 1).width(), is(1));
		assertThat(this.newTable(2, 4).width(), is(2));
	}

	/**
	 * Returns whether this type of table allows null values.
	 * 
	 * @return {@code true} if this type of table allows null values.
	 */
	protected abstract boolean allowNullValues();

	/**
	 * Returns a default non-null default default-value used for the tables
	 * created in this test. This is the default value that is returned with a
	 * value is missing or unset in a table.
	 * <p>
	 * Tables may be created using a default value other than the one specified
	 * here; this is merely the value that will be used if no other one is
	 * explicitly given.
	 * <p>
	 * If applicable, a new value should be created every time this method is
	 * called.
	 * <p>
	 * This method's name is fairly ugly, but I figure it's better to make it
	 * obvious that the default value used here may not necessarily be the
	 * default value used in any given table.
	 * 
	 * @return the default value of tables used in this test
	 */
	protected T getDefaultDefaultValue() {
		return this.defaultDefaultValue;
	}

	/**
	 * Returns some value, used in testing. The value of this value is
	 * irrelevant, as long as it is not {@code null} and not equal to the value
	 * returned by {@link #getValue()} or {@link #getDefaultDefaultValue()}.
	 * <p>
	 * If applicable, a new value should be created every time this method is
	 * called.
	 * 
	 * @return some other value
	 */
	protected abstract T getOtherValue();

	/**
	 * Returns some value, used in testing. The value of this value is
	 * irrelevant, as long as it is not {@code null} and not equal to the value
	 * returned by {@link #getOtherValue()} or {@link #getDefaultDefaultValue()}
	 * .
	 * <p>
	 * If applicable, a new value should be created every time this method is
	 * called.
	 * 
	 * @return some value
	 */
	protected abstract T getValue();

	/**
	 * Returns an arbitrary list of at least two values, used in testing. This
	 * must not contain any values that are contained in the list returned by
	 * {@link #otherListOfValues()}.
	 * <p>
	 * A new array should be created every time this method is called.
	 * 
	 * @return an arbitrary list of values
	 */
	protected abstract List<T> listOfValues();

	/**
	 * Returns an arbitrary list of at least two values, used in testing. This
	 * must not contain any values that are contained in the list returned by
	 * {@link #listOfValues()}.
	 * <p>
	 * A new array should be created every time this method is called.
	 * 
	 * @return an arbitrary list of values
	 */
	protected abstract List<T> otherListOfValues();
}
