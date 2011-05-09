package com.bluespot.logic;

import org.junit.Assert;
import org.junit.Test;

import com.bluespot.logic.functions.Functions;
import com.bluespot.logic.functions.NumericOperations;

public class FunctionTests {

	@Test
	public void testCurrying() throws Exception {
		Assert.assertTrue(Functions.curry(NumericOperations.ADD, 2).apply(3).equals(5.0d));
	}
}
