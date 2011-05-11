package com.bluespot.logic;

import org.junit.Assert;
import org.junit.Test;

import com.bluespot.logic.adapters.Adapters;
import com.bluespot.logic.adapters.SafeFunctionAdapter;
import com.bluespot.logic.functions.AdapterFunction;
import com.bluespot.logic.functions.Curryable;
import com.bluespot.logic.functions.Function;
import com.bluespot.logic.functions.Functions;
import com.bluespot.logic.functions.NumericFunction;
import com.bluespot.logic.functions.NumericOperations;
import com.bluespot.logic.functions.SafeMetaCurryable;

public class FunctionTests {

	@Test
	public void testCurrying() {
		Curryable<Number, Function<Number, ? extends Number>> operation = NumericOperations.ADD;
		Assert.assertTrue(Functions.curry(NumericOperations.ADD, 2).apply(3).equals(5.0d));
		Assert.assertTrue(NumericOperations.ADD.curry(2).apply(3).equals(5.0d));
		operation.curry(4).apply(4);
	}

	@Test
	public void testComposeFunctions() {
		Functions.compose(Functions.multiply(2), Functions.add(3));
	}

	@Test
	public void testProtectFunction() {
		Function<? super Number, ? extends Number> addFunction = new NumericFunction(NumericOperations.ADD, 2);

		// Class
		Functions.protect(Number.class, addFunction).apply(3);

		// Adapter
		Functions.protect(Adapters.cast(Number.class), addFunction).apply(2);
	}

	/**
	 * I wrote these tests up since negotiating all the different types can be
	 * pretty daunting, so I'd like to catch broken-type bugs as soon as I cause
	 * them.
	 */
	@Test
	public void testProtectCurryable() {
		Curryable<Number, Function<Number, ? extends Number>> operation = NumericOperations.ADD;

		// Adapter	Adapter
		Functions.protect(Adapters.cast(Number.class), operation, Adapters.protectFunctions(Number.class)).curry(2).apply(3);

		// Class	Adapter
		Functions.protect(Number.class, operation, Adapters.protectFunctions(Number.class)).curry(2).apply(3);

		// Adapter	Class
		Functions.protect(Adapters.cast(Number.class), operation, Number.class).curry(2).apply(3);

		// Class	Class
		Functions.protect(Number.class, operation, Number.class).curry(2).apply(3);
	}

	/**
	 * Observe that {@link Functions#curry(Object)} doesn't have any knowledge
	 * of the produced function. This is okay since we have access to the
	 * {@link Curryable} object itself, obviating the need for this indirect
	 * currying process.
	 */
	@Test
	public void testIndirectCurrying() {
		Functions.curry(42).apply(NumericOperations.ADD);
		Functions.curry(42).apply(NumericOperations.ADD);
	}

	@Test
	public void testMetaCurryableChaos() throws Exception {
		SafeMetaCurryable.newInstance().curry(2).apply(Functions.protect(Number.class, NumericOperations.ADD, Number.class)).apply(4).equals(6.0d);
		SafeMetaCurryable.newInstance().apply(2).apply(Functions.protect(Number.class, NumericOperations.ADD, Number.class)).apply(4).equals(6.0d);
	}

	@Test
	public void testSafeFunctionAdapter() throws Exception {
		SafeFunctionAdapter<String, Integer> inputAdapter = new SafeFunctionAdapter<String, Integer>(Adapters.stringValue());
		Function<? super Object, ? extends Integer> fxn = inputAdapter.adapt(new AdapterFunction<String, Integer>(Adapters.stringLength()));
		Assert.assertTrue(fxn.apply(42) == 2);
	}

	@Test
	public void testSafeCurryables() throws Exception {
		Assert.assertTrue(Functions.protect(Number.class, NumericOperations.ADD, Number.class).curry(2).apply(3).equals(5.0d));
	}
}
