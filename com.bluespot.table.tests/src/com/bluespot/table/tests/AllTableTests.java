package com.bluespot.table.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTableTests {
	public static Test suite() {
		final TestSuite suite = new TestSuite("Table Tests");
		suite.addTest(new JUnit4TestAdapter(ArrayTableTest.class));
		suite.addTest(new JUnit4TestAdapter(ChoiceTableTest.class));
		suite.addTest(new JUnit4TestAdapter(ColumnarTableIterationTest.class));
		suite.addTest(new JUnit4TestAdapter(NaturalTableIterationTest.class));
		return suite;
	}
}
