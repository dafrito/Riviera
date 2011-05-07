package com.bluespot.collections.table;

import java.util.Arrays;
import java.util.List;

/**
 * Collection of static methods that work on {@link Table} objects.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Tables {

	private Tables() {
		// Suppresses default constructor, ensuring non-instantiability.
		throw new AssertionError("This class cannot be instantiated");
	}

	/**
	 * Fills the table with values from the specified list. The table will be
	 * filled in order of the table's natural iterator (see
	 * {@link Table#tableIterator()}. The values provided in the list will be
	 * used in order, repeating as necessary to fill the entire table.
	 * 
	 * @param <T>
	 *            the type of elements in the table
	 * @param table
	 *            the table to fill
	 * @param values
	 *            the values used to fill the table
	 */
	public static <T> void fill(final Table<T> table, final List<T> values) {
		int i = 0;
		for (final TableIterator<T> iter = table.tableIterator(); iter.hasNext();) {
			iter.next();
			iter.put(values.get(i++));
			i %= values.size();
		}
	}

	/**
	 * Fills the table with the provided value. Note that default values should
	 * typically be used in preference to this method.
	 * <p>
	 * One use case that this method is well-suited for is for filling large
	 * areas of a subtable. For example, the code:
	 * 
	 * <pre>
	 * Tables.fill(table.subTable(point, size), Color.GREEN);
	 * </pre>
	 * 
	 * This allows you to fill areas quickly and cleanly. Without resorting to
	 * iteration.
	 * 
	 * @param <T>
	 *            the type of elements in the table
	 * @param table
	 *            the table to fill
	 * @param value
	 *            the value used to fill the table
	 */
	public static <T> void fill(final Table<T> table, final T value) {
		for (final TableIterator<T> iter = table.tableIterator(); iter.hasNext();) {
			iter.next();
			iter.put(value);
		}
	}

	/**
	 * Fills the table with values from the specified array. The array will be
	 * converted to a list and {@link #fill(Table, List)} will be called.
	 * 
	 * @param <T>
	 *            the type of elements in the table
	 * @param table
	 *            the table to fill
	 * @param values
	 *            the values used to fill the table
	 * 
	 * @see #fill(Table, List)
	 */
	public static <T> void fill(final Table<T> table, final T... values) {
		Tables.fill(table, Arrays.asList(values));
	}

}
