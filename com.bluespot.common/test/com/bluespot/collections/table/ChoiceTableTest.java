package com.bluespot.collections.table;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ChoiceTableTest extends AbstractTableTest<TestEnum> {

	public ChoiceTableTest() {
		super(TestEnum.EMPTY);
	}

	@Override
	public Table<TestEnum> newTable(final int width, final int height, final TestEnum defaultValue) {
		return new ChoiceTable<TestEnum>(TestEnum.values(), width, height, defaultValue);
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
	protected boolean allowNullValues() {
		return false;
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
	protected List<TestEnum> listOfValues() {
		return new ArrayList<TestEnum>(Arrays.asList(TestEnum.FOO, TestEnum.BAZ, TestEnum.NO_TIME, TestEnum.WHEEL));
	}

	@Override
	protected List<TestEnum> otherListOfValues() {
		return new ArrayList<TestEnum>(Arrays.asList(TestEnum.BAR, TestEnum.BASE, TestEnum.CHEESE, TestEnum.NOTTINGHAM));
	}

}

enum TestEnum {
	BAR, BASE, BAZ, CHEESE, EMPTY, FOO, NO_TIME, NOTTINGHAM, WHEEL
}
