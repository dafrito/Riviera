package com.bluespot.table.tests;

import com.bluespot.table.ChoiceTable;
import com.bluespot.table.Table;

public class ChoiceTableTest extends TableTest<TestEnum> {

	public ChoiceTableTest() {
		super(TestEnum.EMPTY);
	}

	@Override
	public Table<TestEnum> newTable(final int width, final int height) {
		return new ChoiceTable<TestEnum>(TestEnum.values(), width, height, TestEnum.EMPTY);
	}

	@Override
	protected TestEnum getOtherValue() {
		return TestEnum.BAR;
	}

	@Override
	protected TestEnum getValue() {
		return TestEnum.FOO;
	}

	@Override
	protected TestEnum[] listOfValues() {
		return new TestEnum[] { TestEnum.FOO, TestEnum.BAZ, TestEnum.NO_TIME, TestEnum.WHEEL };
	}

	@Override
	protected TestEnum[] otherListOfValues() {
		return new TestEnum[] { TestEnum.BAR, TestEnum.BASE, TestEnum.CHEESE, TestEnum.NOTTINGHAM };
	}

}

enum TestEnum {
	BAR, BASE, BAZ, CHEESE, EMPTY, FOO, NO_TIME, NOTTINGHAM, WHEEL
}
