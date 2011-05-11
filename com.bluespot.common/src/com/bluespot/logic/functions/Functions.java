package com.bluespot.logic.functions;

import com.bluespot.logic.adapters.Adapter;
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
	 * Returns the {@link SafeCurryable} class, cast as the parent
	 * {@link Curryable} type. This type is useful when working with complicated
	 * adapters that require a {@link Curryable} type, but you are only using
	 * {@link SafeCurryable} objects.
	 * <p>
	 * This is the authoritative cast, so any {@link SafeCurryable} object is
	 * guaranteed to be accepted by this type.
	 * 
	 * @param <R>
	 *            the type of value returned by the underlying function.
	 * @return the {@link SafeCurryable} class, cast as a {@link Curryable}
	 * @see #safeFunctionType()
	 */
	@SuppressWarnings("unchecked")
	public static <R> Class<? extends Curryable<Object, SafeFunction<R>>> safeCurryableType() {
		// This cast is safe since it matches SafeCurryable's declared type. 
		return (Class<? extends Curryable<Object, SafeFunction<R>>>) SafeCurryable.class;
	}

	/**
	 * Returns the {@link SafeFunction} class, cast as the parent
	 * {@link Function} type. This type is useful when working with complicated
	 * adapters that require a {@link Function} type, and you are only using
	 * {@link SafeFunction} objects.
	 * <p>
	 * This is the authoritative cast, so any {@link SafeFunction} object is
	 * guaranteed to be accepted by this type.
	 * 
	 * @param <R>
	 *            the type of value returned by the underlying function.
	 * @return the {@link SafeFunction} type, cast as a {@link Function}
	 * @see #safeCurryableType()
	 */
	@SuppressWarnings("unchecked")
	public static <R> Class<? extends Function<Object, R>> safeFunctionType() {
		// This cast is safe since it matches SafeFunction's declared type. 
		return (Class<? extends Function<Object, R>>) SafeFunction.class;
	}

	/**
	 * @return a {@link Function} implementation that returns any value it is
	 *         given.
	 */
	public static SafeFunction<Object> identity() {
		return FUNCTION_IDENTITY;
	}

	public static <I, R> SafeFunction<? extends R> protect(Adapter<? super Object, ? extends I> adapter, Function<? super I, ? extends R> function) {
		return new AdaptingSafeFunction<I, R>(adapter, function);
	}

	/**
	 * A {@link Function} that safely casts to a specified value before
	 * proceeding. Casts that fail will be passed to the underlying function as
	 * {@code null}.
	 * 
	 * @param <I>
	 *            the type of input value expected by the function
	 * @param <R>
	 *            the type of the function's return value
	 * @param underlyingType
	 *            the type of value that is passed to the underlying function
	 * @param function
	 *            the underlying function
	 * @return a new {@link Function} object
	 */
	public static <I, R> SafeFunction<? extends R> protect(Class<? extends I> underlyingType, Function<? super I, ? extends R> function) {
		return Functions.<I, R> protect(Adapters.cast(underlyingType), function);
	}

	public static <C, F> F curry(Curryable<? super C, ? extends F> curryable, C value) {
		return curryable.curry(value);
	}

	public static <C, R> SafeFunction<? extends R> curry(Function<? super C, ? extends R> function, C value) {
		return new FunctionValue<C, R>(function, value);
	}

	private static final Function<?, ?> FUNCTION_CURRY_NULL = new Function<Curryable<?, ?>, Function<?, ?>>() {

		@Override
		public Function<?, ?> apply(Curryable<?, ?> input) {
			return input.curry(null);
		}

	};

	@SuppressWarnings("unchecked")
	public static <C, F extends Function<?, ?>> Function<Curryable<? super C, ? extends F>, F> curryNull() {
		// This cast is safe since FUNCTION_CURRY_NULL does not use the underlying curryable (beyond currying it, of course)
		return (Function<Curryable<? super C, ? extends F>, F>) FUNCTION_CURRY_NULL;
	}

	/**
	 * Create a function that curries the specified value. This method is
	 * normally not necessary, since {@link Curryable} objects can curry
	 * themselves.
	 * 
	 * @param <C>
	 *            the type of the curried value
	 * @param <F>
	 *            the type of function produced by {@link Curryable} objects
	 * @param value
	 *            the value that will be curried
	 * @return a {@link Function} that curries values.
	 */
	public static <C, F extends Function<?, ?>> Function<Curryable<? super C, ? extends F>, F> curry(C value) {
		if (value == null) {
			return Functions.curryNull();
		}
		return new CurryFunction<C, F>(value);
	}

	/**
	 * Protect the specified {@link Curryable} through adapters. The curried
	 * value is adapted to the expected type {@code C} for the underlying
	 * curryable. The returned function is similarly protected by adapted
	 * received values to the function's input type {@code I}.
	 * 
	 * @param <C>
	 *            the type of value that is curried
	 * @param <I>
	 *            the type of input value received by underlying functions.
	 * @param <R>
	 *            the type of value that is returned by underlying functions.
	 *            The return type is not modified by this object.
	 * @param curryAdapter
	 *            an {@link Adapter} that converts any object to a type accepted
	 *            by the underlying curryable
	 * @param curryable
	 *            the {@link Curryable} that is protected by this invocation
	 * @param functionAdapter
	 *            an {@link Adapter} that converts functions returned by the
	 *            underlying {@link Curryable} to {@link SafeFunction} objects
	 * @return a {@link SafeCurryable} that accepts any value, and produces
	 *         {@link SafeFunction} objects that also accept any value
	 * @see AdaptingSafeCurryable
	 * @see SafeFunction
	 */
	public static <C, I, R> SafeCurryable<? extends R> protect(
			Adapter<? super Object, ? extends C> curryAdapter,
			Curryable<? super C, ? extends Function<? super I, ? extends R>> curryable,
			Adapter<? super Function<? super I, ? extends R>, ? extends SafeFunction<? extends R>> functionAdapter) {
		return new AdaptingSafeCurryable<C, I, R>(curryAdapter, curryable, functionAdapter);
	}

	/**
	 * Protect the specified {@link Curryable} through adapters.
	 * <p>
	 * Curried values are adapted using {@link Adapters#cast(Class)}. Any value
	 * that is not an instance of {@code curryType} is converted to {@code null}.
	 * 
	 * @param <C>
	 *            the type of value that is curried
	 * @param <I>
	 *            the type of input value received by underlying functions.
	 * @param <R>
	 *            the type of value that is returned by underlying functions.
	 *            The return type is not modified by this object.
	 * @param curryType
	 *            the type that is used to dynamically cast objects to a class
	 *            expected by the underlying {@link Curryable}.
	 * @param curryable
	 *            the {@link Curryable} that is protected by this invocation
	 * @param functionAdapter
	 *            an {@link Adapter} that converts functions returned by the
	 *            underlying {@link Curryable} to {@link SafeFunction} objects
	 * @return a {@link SafeCurryable} that dynamically casts curry values, and
	 *         produces {@link SafeFunction} objects that also accept any value
	 * @see Functions#protect(Adapter, Curryable, Adapter)
	 */
	public static <C, I, R> SafeCurryable<? extends R> protect(
			Class<? extends C> curryType,
			Curryable<? super C, ? extends Function<? super I, ? extends R>> curryable,
			Adapter<? super Function<? super I, ? extends R>, ? extends SafeFunction<? extends R>> functionAdapter) {
		return Functions.protect(Adapters.cast(curryType), curryable, functionAdapter);
	}

	/**
	 * Protect the specified {@link Curryable} through adapters.
	 * <p>
	 * The returned function is protected via {@link Adapters#cast(Class)}, such
	 * that any value that is not a subclass of {@code functionInputType} will
	 * be converted to null.
	 * 
	 * @param <C>
	 *            the type of value that is curried
	 * @param <I>
	 *            the type of input value received by underlying functions.
	 * @param <R>
	 *            the type of value that is returned by underlying functions.
	 *            The return type is not modified by this object.
	 * @param curryAdapter
	 *            an {@link Adapter} that converts any object to a type accepted
	 *            by the underlying curryable
	 * @param curryable
	 *            the {@link Curryable} that is protected by this invocation
	 * @param functionInputType
	 *            the type that is used to cast objects to a type expected by
	 *            functions produced by the underlying curryable.
	 * @return a {@link SafeCurryable} that accepts any value, and produces
	 *         {@link SafeFunction} objects that also accept any value
	 * @see Functions#protect(Adapter, Curryable, Adapter)
	 */
	public static <C, I, R> SafeCurryable<? extends R> protect(
			Adapter<? super Object, ? extends C> curryAdapter,
			Curryable<? super C, ? extends Function<? super I, ? extends R>> curryable,
			Class<? extends I> functionInputType) {
		return Functions.protect(curryAdapter, curryable, Adapters.<I, R> protectFunctions(functionInputType));
	}

	/**
	 * Protect the specified {@link Curryable} through adapters.
	 * <p>
	 * Curried values are adapted using {@link Adapters#cast(Class)}. Any value
	 * that is not an instance of {@code curryType} is converted to {@code null}.
	 * <p>
	 * The returned function is protected via {@link Adapters#cast(Class)}, such
	 * that any value that is not a subclass of {@code functionInputType} will
	 * be converted to null.
	 * 
	 * @param <C>
	 *            the type of value that is curried
	 * @param <I>
	 *            the type of input value received by underlying functions.
	 * @param <R>
	 *            the type of value that is returned by underlying functions.
	 *            The return type is not modified by this object.
	 * @param curryType
	 *            the type that is used to dynamically cast objects to a class
	 *            expected by the underlying {@link Curryable}.
	 * @param curryable
	 *            the {@link Curryable} that is protected by this invocation
	 * @param functionInputType
	 *            the type that is used to cast objects to a type expected by
	 *            functions produced by the underlying curryable.
	 * @return a {@link SafeCurryable} that accepts any value, and produces
	 *         {@link SafeFunction} objects that also accept any value
	 * @see Functions#protect(Adapter, Curryable, Adapter)
	 */
	public static <C, I, R> SafeCurryable<? extends R> protect(
			Class<? extends C> curryType,
			Curryable<? super C, ? extends Function<? super I, ? extends R>> curryable,
			Class<? extends I> functionInputType) {
		return Functions.protect(Adapters.cast(curryType), curryable, Adapters.<I, R> protectFunctions(functionInputType));
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
