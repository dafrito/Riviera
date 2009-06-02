package com.bluespot.table.tests;

import com.bluespot.table.ArrayTable;
import com.bluespot.table.Table;

public class ArrayTableTest extends TableTest<Integer> {

	public ArrayTableTest() {
		super(0);
	}

	@Override
	public Table<Integer> newTable(int width, int height) {
		return new ArrayTable<Integer>(width, height, this.getDefaultValue());
	}

	@Override
	protected Integer getOtherValue() {
		return 2;
	}

	@Override
	protected Integer getValue() {
		return 1;
	}

	@Override
	protected Integer[] listOfValues() {
		return new Integer[] { 1, 2, 3, 4 };
	}

	@Override
	protected Integer[] otherListOfValues() {
		return new Integer[] { 10, 11, 12, 13 };
	}

}
