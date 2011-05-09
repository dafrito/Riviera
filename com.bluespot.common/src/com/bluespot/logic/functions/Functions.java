package com.bluespot.logic.functions;

import com.bluespot.logic.adapters.Adapters;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.values.FunctionValue;
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

	private static final SafeFunction<Object> FUNCTION_IDENTITY = new SafeFunction<Object>() {
		@Override
		public Object apply(Object input) {
			return input;
		}
	};

	/**
	 * @return a {@link Function} implementation that returns any value it is
	 *         given.
	 */
	public static SafeFunction<Object> identity() {
		return FUNCTION_IDENTITY;
	}

	/**
	 * Return {@link CurryFunction} objects that use provided values.
	 * <p>
	 * This singleton would be an anonymous class, but I couldn't get the
	 * casting to work with the generic type. A singleton was the next best
	 * thing.
	 * 
	 * @author Aaron Faanes
	 * 
	 * @param <I>
	 *            the type of the provided value
	 */
	private static class CreateCurryFunction<I> implements Function<I, CurryFunction<I, Function<?, ?>>> {

		private CreateCurryFunction() {
			// Hide this constructor, since we're a singleton
		}

		@Override
		public CurryFunction<I, Function<?, ?>> apply(I input) {
			return new CurryFunction<I, Function<?, ?>>(input);
		}

		public static final CreateCurryFunction<?> INSTANCE = new CreateCurryFunction<Object>();
	}

	@SuppressWarnings("unchecked")
	public static <I> Function<I, CurryFunction<I, Function<?, ?>>> curry() {
		// This unchecked cast is always safe, since we're only restricting the allowable values here.
		// Uses of this function would be restricted by the returned type anyway, and thus fail at
		// compile-time.
		return (CreateCurryFunction<I>) CreateCurryFunction.INSTANCE;
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

	public static <C, R> R curry(Curryable<? super C, ? extends R> curryable, C value) {
		return curryable.curry(value);
	}

	public static <C, R> SafeFunction<? extends R> curry(Function<? super C, ? extends R> function, C value) {
		return new FunctionValue<C, R>(function, value);
	}

	public static <C, R extends Function<?, ?>> CurryFunction<C, R> curry(C value) {
		return new CurryFunction<C, R>(value);
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
