package logic.values;

import logic.adapters.Adapter;
import logic.functions.Function;

/**
 * A collection of factory methods for common {@link Value} idioms.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Values {

	private Values() {
		// Suppress default constructor to ensure non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * Returns a {@link MutableValue} object that uses the specified value as
	 * its first value.
	 * 
	 * @param initialValue
	 *            the initial value of the returned {@code MutableValue} object.
	 *            It may not be null.
	 * @param <T>
	 *            the type of value contained by the {@code Value} object
	 * @return a {@code MutableValue} object that uses the specified value for
	 *         its initial value.
	 * @throws NullPointerException
	 *             if {@code initialValue} is null
	 */
	public static <T> MutableValue<T> value(final T initialValue) {
		if (initialValue == null) {
			throw new NullPointerException("initialValue is null");
		}
		return new MutableValue<T>(initialValue);
	}

	/**
	 * Return a {@link FunctionValue} evaluated at the specified constant value.
	 * 
	 * @param <T>
	 *            the type of input value for the specified function
	 * @param <V>
	 *            the type of return value for the specified function
	 * @param func
	 *            the underlying function that is evaluated
	 * @param constant
	 *            the constant value that is provided to the specified function
	 * @return a {@link FunctionValue} evaluated at the specified constant
	 *         value.
	 */
	public static <T, V> Value<? extends V> value(Function<? super T, ? extends V> func, T constant) {
		return new FunctionValue<T, V>(func, constant);
	}

	/**
	 * Returns a {@link BufferedValue} that wraps the specified {@code source}
	 * value. See the {@code BufferedValue} class' documentation for information
	 * on how this class operates.
	 * 
	 * @param <T>
	 *            the type of the specified source value, and the returned
	 *            buffered value
	 * @param source
	 *            the source value. It must not be null.
	 * @return a new {@code BufferedValue} that uses the specified
	 *         {@code source} value
	 * @throws NullPointerException
	 *             if {@code source} is null
	 */
	public static <T> BufferedValue<T> buffered(final Value<? extends T> source) {
		if (source == null) {
			throw new NullPointerException("source is null");
		}
		return new BufferedValue<T>(source);
	}

	/**
	 * Returns a {@link Value} object that will always return the specified
	 * constant.
	 * 
	 * @param <T>
	 *            the type of the constant
	 * @param constant
	 *            the constant value that the returned {@code Value} object will
	 *            return
	 * @return a {@code Value} object that wraps the specified constant
	 * @throws NullPointerException
	 *             if {@code constant} is null; use {@link #nullValue()} if you
	 *             want a {@code Value} object that returns null.
	 */
	public static <T> Value<T> constant(final T constant) {
		if (constant == null) {
			throw new NullPointerException("constant is null");
		}
		return new ConstantValue<T>(constant);
	}

	/**
	 * A {@link Value} implementation that only returns null.
	 */
	private static final Value<?> VALUE_NULL = new Value<Object>() {
		@Override
		public Object get() {
			return null;
		}
	};

	/**
	 * Returns a {@link Value} object that always returns null.
	 * 
	 * @return a {@link Value} object that always returns null.
	 */
	public static Value<?> nullValue() {
		return VALUE_NULL;
	}

	/**
	 * Returns a {@link Value} object that, when invoked through
	 * {@link Value#get()}, converts the specified source value using the
	 * specified adapter.
	 * 
	 * @param <S>
	 *            the type of the source value
	 * @param <D>
	 *            the type of the converted value
	 * @param source
	 *            the source value, retrieved through {@link Value#get()}. This
	 *            will be passed to the specified adapter for conversion.
	 * @param adapter
	 *            the {@link Adapter} implementation used to perform the
	 *            conversion
	 * @return an adapter that converts the specified source using the specified
	 *         adapter
	 * @throws NullPointerException
	 *             if either argument is null
	 */
	public static <S, D> Value<D> adapt(final Value<? extends S> source, final Adapter<? super S, ? extends D> adapter) {
		if (source == null) {
			throw new NullPointerException("source is null");
		}
		if (adapter == null) {
			throw new NullPointerException("adapter is null");
		}
		return new AdaptingValue<S, D>(source, adapter);
	}

	/**
	 * Calls {@link Values#adapt(Value, Adapter)}, wrapping the specified
	 * constant using {@link ConstantValue}.
	 * 
	 * @param <S>
	 *            the type of the source value
	 * @param <D>
	 *            the type of the converted value
	 * @param sourceConstant
	 *            the source value. It will be wrapped by a
	 *            {@link ConstantValue} object.
	 * @param adapter
	 *            the {@link Adapter} implementation used to perform the
	 *            conversion
	 * @return an adapter that converts the specified source using the specified
	 *         adapter
	 * @throws NullPointerException
	 *             if either argument is null
	 */
	public static <S, D> Value<D> adapt(final S sourceConstant, final Adapter<? super S, ? extends D> adapter) {
		return Values.adapt(new ConstantValue<S>(sourceConstant), adapter);
	}

	/**
	 * Return a new value that will invoke an underlying {@link Function}
	 * recursively, such that values returned will resemble the following:
	 * 
	 * <ul>
	 * <li>{@code initial}
	 * <li>{@code function.apply(initial)}
	 * <li>{@code function.apply(function.apply(initial))}
	 * </ul>
	 * 
	 * @param <T>
	 *            the type of returned value
	 * @param function
	 *            the function that will be invoked
	 * @param initial
	 *            the initial value passed to the underlying function
	 * @return a new {@link Value} object
	 * @see RecursiveValue
	 */
	public static <T> Value<T> recurse(Function<? super T, ? extends T> function, T initial) {
		return new RecursiveValue<T>(function, initial);
	}
}
