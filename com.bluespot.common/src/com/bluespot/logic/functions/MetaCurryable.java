package com.bluespot.logic.functions;

/**
 * A {@link Curryable} that curries values. This class constructs
 * {@link Function} instances that will attempt to curry provided
 * {@link Curryable} objects.
 * <p>
 * * This class is implemented as both a {@link SafeCurryable} (hence the prefix
 * Meta-) and a {@link SafeFunction}. It has the unique characteristic of
 * implementing currying and function application in the same way.
 * 
 * @author Aaron Faanes
 * 
 * @param <C>
 *            the type of value that will be curried
 * @param <F>
 *            the type of function produced by curried objects
 * @see SafeMetaCurryable
 */
public final class MetaCurryable<C, F extends Function<?, ?>> implements Curryable<C,
		Function<Curryable<? super C, ? extends F>, ? extends F>>,
		Function<C, Function<? super Curryable<? super C, ? extends F>, ? extends F>> {

	private MetaCurryable() {
		// Hide this constructor since we're a singleton
	}

	@Override
	public Function<Curryable<? super C, ? extends F>, ? extends F> curry(C value) {
		return apply(value);
	}

	@Override
	public Function<Curryable<? super C, ? extends F>, ? extends F> apply(C input) {
		return new CurryFunction<C, F>(input);
	}

	private static final MetaCurryable<?, ?> INSTANCE = new MetaCurryable<Object, Function<?, ?>>();

	/**
	 * Get an instance of {@link MetaCurryable}. This method is allowed to
	 * return the same instance, as {@code MetaCurryable} objects may not have
	 * any reified state; a single instance cast to the correct generic types
	 * will suffice.
	 * 
	 * @param <C>
	 *            the type of value that will be curried
	 * @param <F>
	 *            the type of function produced by curried objects
	 * @return a {@link MetaCurryable} instance
	 * @see MetaCurryable
	 */
	@SuppressWarnings("unchecked")
	public static <C, F extends Function<?, ?>> MetaCurryable<? super C, ? extends F> newInstance() {
		// This unsafe cast will always succeed, since MetaCurryable has no state.
		return (MetaCurryable<C, F>) INSTANCE;
	}

}