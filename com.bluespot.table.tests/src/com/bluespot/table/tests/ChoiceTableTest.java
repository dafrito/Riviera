package com.bluespot.table.tests;

import java.awt.Point;

import org.junit.Test;

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

	@Test(expected = IllegalArgumentException.class)
	public void testBadDefaultValue() {
		final TestEnum[] choices = new TestEnum[] { TestEnum.BAR, TestEnum.BASE };
		new ChoiceTable<TestEnum>(choices, 10, 10, TestEnum.NOTTINGHAM);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidChoiceForPut() {
		final TestEnum[] choices = new TestEnum[] { TestEnum.BAR, TestEnum.BASE };
		final ChoiceTable<TestEnum> table = new ChoiceTable<TestEnum>(choices, 10, 10, TestEnum.BAR);
		table.put(new Point(0, 0), TestEnum.NOTTINGHAM);
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
