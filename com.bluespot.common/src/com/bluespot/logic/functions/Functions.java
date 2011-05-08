package com.bluespot.logic.functions;

import com.bluespot.logic.adapters.Adapters;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.values.Value;
import com.bluespot.logic.values.Values;

/**
 * A collection of factory methods for common {@link Value} idioms.
 * 
 * @author Aaron Faanes
 * 
 */
public class Functions {

	private Functions() {
		// Suppress default constructor to ensure non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * A {@link Function} that safely casts to a specified value before
	 * proceeding. Casts that fail will be passed to the underlying function as
	 * {@code null}.
	 * 
	 * @param <I>
	 *            the type of input value expected by the function
	 * @param <V>
	 *            the type of the function's return value
	 * @param guardType
	 *            the type of value that is passed to the underlying function
	 * @param function
	 *            the underlying function
	 * @return a new {@link Function} object
	 */
	public static <I, V> SafeFunction<? extends V> cast(Class<I> guardType, Function<? super I, ? extends V> function) {
		return new AdaptingSafeFunction<I, V>(Adapters.cast(guardType), function);
	}

	public static <I, V> Function<? super I, ? extends V> guard(Predicate<? super I> guard, Function<? super I, ? extends V> function) {
		return new GuardedFunction<I, V>(guard, function);
	}

	public static <V> SafeFunction<? extends V> value(V constant) {
		return new ValueFunction<V>(Values.constant(constant));
	}

	public static <V> SafeFunction<? extends V> value(Value<? extends V> constant) {
		return new ValueFunction<V>(constant);
	}

	public static <T extends Number> Function<Number, ? extends Number> add(final T constant) {
		return new NumericFunction(NumericOperations.ADD, constant);
	}

	public static <T extends Number> Function<Number, ? extends Number> subtract(final T constant) {
		return new NumericFunction(NumericOperations.SUBTRACT, constant);
	}

	public static <T extends Number> Function<Number, ? extends Number> multiply(final T constant) {
		return new NumericFunction(NumericOperations.MULTIPLY, constant);
	}

	public static <T extends Number> Function<Number, ? extends Number> divide(final T constant) {
		return new NumericFunction(NumericOperations.DIVIDE, constant);
	}

	public static <T extends Number> Function<Number, ? extends Number> modulus(final T constant) {
		return new NumericFunction(NumericOperations.MODULUS, constant);
	}

}
