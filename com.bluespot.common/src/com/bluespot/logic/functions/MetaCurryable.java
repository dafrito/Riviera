package com.bluespot.logic.functions;

/**
 * A {@link Curryable} that curries values.
 * <p>
 * It some ways, this class represents a fundamental unit of dynamic
 * programming, as it produces functions that produce curryables. It implements
 * both {@link Curryable} and {@link Function} to provide users with either
 * method.
 * 
 * @author Aaron Faanes
 * 
 * @param <C>
 *            the type of value that will be curried
 * @param <F>
 *            the type of function produced by curried objects
 * @see #newInstance()
 */
public final class MetaCurryable<C, F extends Function<?, ?>> implements Curryable<C,
		Function<Curryable<? super C, ? extends F>, ? extends F>>,
		Function<C, Function<Curryable<? super C, ? extends F>, ? extends F>> {

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
	public static <C, F extends Function<?, ?>> MetaCurryable<C, F> newInstance() {
		// This unsafe cast will always succeed, since MetaCurryable has no state.
		return (MetaCurryable<C, F>) INSTANCE;
	}

}