package com.bluespot.collections.table;

import com.bluespot.collections.table.StrategyTableIterator;
import com.bluespot.collections.table.Table;
import com.bluespot.collections.table.TableIterator;
import com.bluespot.collections.table.iteration.NaturalTableIteration;

public class StrategyTableIteratorTest extends TableIteratorTest {

	@Override
	public TableIterator<Integer> newIterator(final Table<Integer> targetTable) {
		return new StrategyTableIterator<Integer>(targetTable, NaturalTableIteration.getInstance());
	}
}
