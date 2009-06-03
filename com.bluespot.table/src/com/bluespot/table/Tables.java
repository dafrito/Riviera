package com.bluespot.table;

import java.util.Arrays;
import java.util.List;

public final class Tables {

	private Tables() {
		// Suppresses default constructor, ensuring non-instantiability.
		throw new AssertionError("This class cannot be instantiated");
	}

	public static <T> void fill(final Table<T> table, final List<T> values) {
		int i = 0;
		for (final TableIterator<T> iter = table.tableIterator(); iter.hasNext();) {
			iter.next();
			iter.put(values.get(i++));
			i %= values.size();
		}
	}

	public static <T> void fill(final Table<T> table, final T value) {
		for (final TableIterator<T> iter = table.tableIterator(); iter.hasNext();) {
			iter.next();
			iter.put(value);
		}
	}

	public static <T> void fill(final Table<T> table, final T... values) {
		Tables.fill(table, Arrays.asList(values));
	}

	public static void fillCount(final Table<Integer> table, final int start) {
		int i = start;
		for (final TableIterator<Integer> iter = table.tableIterator(); iter.hasNext();) {
			iter.next();
			iter.put(i++);
		}
	}

}
