package com.bluespot.swing.list.tests;

import org.junit.internal.runners.JUnit4ClassRunner;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllSwingListTests {
	public static Test suite() {
		final TestSuite suite = new TestSuite("Swing List Tests");
		suite.addTest(new JUnit4TestAdapter(ProxiedListModelTest.class));
		//suite.addTest(new JUnit4TestAdapter(DefaultMutableListModelTest.class));
		return suite;
	}
}
