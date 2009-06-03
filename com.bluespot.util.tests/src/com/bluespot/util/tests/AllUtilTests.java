package com.bluespot.util.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUtilTests {
	public static Test suite() {
		final TestSuite suite = new TestSuite("Util Tests");
		suite.addTest(new JUnit4TestAdapter(RangeTest.class));
		return suite;
	}
}
