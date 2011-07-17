package com.bluespot.logic.functions.curryable;

import com.bluespot.logic.adapters.Adapters;
import com.bluespot.logic.functions.Functions;
import com.bluespot.logic.functions.SafeFunction;

/**
 * A {@link Curryable} that curries values. This class constructs
 * {@link SafeFunction} instances that will attempt to curry provided
 * {@link SafeCurryable} objects.
 * <p>
 * This class is implemented as both a {@link SafeCurryable} (hence the prefix
 * Meta-) and a {@link SafeFunction}. It has the unique characteristic of
 * implementing currying and function application in the same way.
 * 
 * @author Aaron Faanes
 * @param <R>
 *            the type of return value from curried functions
 * 
 * @see MetaCurryable
 */
public class SafeMetaCurryable<R> implements SafeCurryable<SafeFunction<R>>,
		SafeFunction<SafeFunction<? extends SafeFunction<R>>> {

	private SafeMetaCurryable() {
		// Hide this constructor since we're a singleton
	}

	@Override
	public SafeFunction<? extends SafeFunction<R>> curry(Object value) {
		return apply(value);
	}

	@Override
	public SafeFunction<? extends SafeFunction<R>> apply(Object input) {
		return Functions.protect(Adapters.cast(Functions.<R> safeCurryableType()),
				Functions.<Object, SafeFunction<R>> curry(input));
	}

	private static final SafeMetaCurryable<?> INSTANCE = new SafeMetaCurryable<Object>();

	/**
	 * Get an instance of {@link SafeMetaCurryable}. This method is allowed to
	 * return the same instance, as {@code SafeMetaCurryable} objects may not
	 * have any reified state; a single instance cast to the correct generic
	 * types will suffice.
	 * 
	 * @param <R>
	 *            the type of return value from curried functions
	 * 
	 * @return a {@link SafeMetaCurryable} instance
	 * @see MetaCurryable
	 */
	@SuppressWarnings("unchecked")
	public static <R> SafeMetaCurryable<? extends R> newInstance() {
		// This unsafe cast will always succeed, since MetaCurryable has no state.
		return (SafeMetaCurryable<R>) INSTANCE;
	}

	@Override
	public String toString() {
		return "SafeMetaCurryable";
	}
}