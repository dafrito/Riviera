package com.bluespot.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.bluespot.geom.tests.AllGeomTests;
import com.bluespot.logging.tests.AllLoggingTests;
import com.bluespot.swing.list.tests.AllSwingListTests;
import com.bluespot.table.tests.AllTableTests;
import com.bluespot.tree.tests.AllTreeTests;
import com.bluespot.util.tests.AllUtilTests;

public class AllRivieraTests {

	public static Test suite() {
		final TestSuite suite = new TestSuite("Riviera Tests");
		suite.addTest(AllGeomTests.suite());
		suite.addTest(AllLoggingTests.suite());
		suite.addTest(AllSwingListTests.suite());
		suite.addTest(AllTableTests.suite());
		suite.addTest(AllTreeTests.suite());
		suite.addTest(AllUtilTests.suite());
		return suite;
	}
}
