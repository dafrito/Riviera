package com.bluespot.table.tests;

import com.bluespot.table.StrategyTableIterator;
import com.bluespot.table.Table;
import com.bluespot.table.TableIterator;
import com.bluespot.table.iteration.NaturalTableIteration;

public class StrategyTableIteratorTest extends TableIteratorTest {

	@Override
	public TableIterator<Integer> newIterator(final Table<Integer> targetTable) {
		return new StrategyTableIterator<Integer>(targetTable, NaturalTableIteration.getInstance());
	}
}
