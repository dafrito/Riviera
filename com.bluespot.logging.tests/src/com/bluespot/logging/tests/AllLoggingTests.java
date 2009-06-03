package com.bluespot.logging.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllLoggingTests {
	public static Test suite() {
		final TestSuite suite = new TestSuite("Logging Tests");
		suite.addTest(new JUnit4TestAdapter(CallStackHandlerTest.class));
		suite.addTest(new JUnit4TestAdapter(ListHandlerTest.class));
		suite.addTest(new JUnit4TestAdapter(LoggerTest.class));
		suite.addTest(new JUnit4TestAdapter(LoggingIntegrationTest.class));
		return suite;
	}
}
