package com.bluespot.logic;

import org.junit.Assert;
import org.junit.Test;

import com.bluespot.logic.adapters.Adapters;
import com.bluespot.logic.adapters.FunctionInputAdapter;
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
	public void testCurryingTypes() throws Exception {
		Functions.curry(2).apply(NumericOperations.ADD);
	}

	@Test
	public void testFunctionInputAdapter() throws Exception {
		FunctionInputAdapter<Object, String, Integer> inputAdapter = new FunctionInputAdapter<Object, String, Integer>(Adapters.stringValue());
		Function<? super Object, ? extends Integer> fxn = inputAdapter.adapt(new AdapterFunction<String, Integer>(Adapters.stringLength()));
		Assert.assertTrue(fxn.apply(42) == 2);
	}
}
