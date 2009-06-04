package com.bluespot.reflection.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllReflectionTests {
	public static Test suite() {
		final TestSuite suite = new TestSuite("Reflection Tests");
		suite.addTest(new JUnit4TestAdapter(ReflectionTest.class));
		suite.addTest(new JUnit4TestAdapter(CallStackTests.class));
		return suite;
	}
}
