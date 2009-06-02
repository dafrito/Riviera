package com.bluespot.table.tests;

import com.bluespot.table.EnumTable;
import com.bluespot.table.Table;

enum TestEnum {
    EMPTY,
    FOO,
    BAR,
    BAZ,
    BASE,
    NO_TIME,
    CHEESE,
    WHEEL,
    NOTTINGHAM
}

public class EnumTableTest extends TableTest<TestEnum> {
    
    public EnumTableTest() {
        super(TestEnum.EMPTY);
    }

    @Override
    public Table<TestEnum> newTable(int width, int height) {
        return new EnumTable<TestEnum>(TestEnum.class, TestEnum.values(), width, height, TestEnum.EMPTY);
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
