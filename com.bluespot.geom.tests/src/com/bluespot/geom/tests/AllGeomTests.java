package com.bluespot.geom.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllGeomTests {
	public static Test suite() {
		final TestSuite suite = new TestSuite("Geometry Tests");
		suite.addTest(new JUnit4TestAdapter(GeometryTest.class));
		return suite;
	}
}
