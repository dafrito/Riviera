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

	/**
	 * I struggled a bit regarding the order of functions in Functions.compose.
	 * I went with (inner, outer) but the reverse may have been just as well. I
	 * think staying consistent matters more than finding the "perfect reason"
	 * for some syntax.
	 * <p>
	 * Similarly, composing curryables add outer, rather than inner, functions.
	 * I think the code would not be that much different for the unchosen case,
	 * but I prefer inner-to-outer since it meshes with other decisions (and my
	 * own line of thinking).
	 * <p>
	 * Functions chain inner to outer: {@code (((x + 3) * 2) / 3)}
	 * <p>
	 * I use the numeric utility functions below. Remember that they always
	 * specify the right argument, even the cases of division and subtraction.
	 * {@code Functions.divide(3)} means {@code x / 3} rather than {@code 3 / x}.
	 */
	@Test
	public void testComposeFunctions() {
		Assert.assertEquals(
				Double.valueOf(((4.0d + 3) * 2) / 3),
				Functions.compose(Functions.compose(Functions.add(3), Functions.multiply(2)), Functions.divide(3)).apply(4)
				);
	}

	@Test
	public void testComposingCurryable() {
		Assert.assertEquals(
				Double.valueOf((2 + 3) * 2),
				Functions.composing(Functions.add(3)).curry(Functions.multiply(2)).apply(2));
		Assert.assertEquals(
				Double.valueOf(((4.0d + 3) * 2) / 3),
				Functions.composing(Functions.add(3)).curry(Functions.multiply(2)).curry(Functions.divide(3)).apply(4)
				);
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
