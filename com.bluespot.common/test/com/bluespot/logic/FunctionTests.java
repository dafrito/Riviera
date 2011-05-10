package com.bluespot.logic;

import org.junit.Assert;
import org.junit.Test;

import com.bluespot.logic.adapters.Adapters;
import com.bluespot.logic.adapters.SafeFunctionAdapter;
import com.bluespot.logic.functions.AdapterFunction;
import com.bluespot.logic.functions.Function;
import com.bluespot.logic.functions.Functions;
import com.bluespot.logic.functions.NumericOperations;

public class FunctionTests {

	@Test
	public void testCurrying() throws Exception {
		Assert.assertTrue(Functions.curry(NumericOperations.ADD, 2).apply(3).equals(5.0d));
	}

	@Test
	public void testSafeFunctionAdapter() throws Exception {
		SafeFunctionAdapter<String, Integer> inputAdapter = new SafeFunctionAdapter<String, Integer>(Adapters.stringValue());
		Function<? super Object, ? extends Integer> fxn = inputAdapter.adapt(new AdapterFunction<String, Integer>(Adapters.stringLength()));
		Assert.assertTrue(fxn.apply(42) == 2);
	}
}
