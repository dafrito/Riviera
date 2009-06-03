package com.bluespot.tree.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTreeTests {
	public static Test suite() {
		final TestSuite suite = new TestSuite("Tree Tests");
		suite.addTest(new JUnit4TestAdapter(TreeTest.class));
		return suite;
	}
}
