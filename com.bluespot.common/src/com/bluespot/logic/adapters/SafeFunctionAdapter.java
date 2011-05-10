package com.bluespot.logic.adapters;

import com.bluespot.logic.functions.AdaptingFunction;
import com.bluespot.logic.functions.AdaptingSafeFunction;
import com.bluespot.logic.functions.Function;
import com.bluespot.logic.functions.SafeFunction;

/**
 * Adapts functions to safe functions using a specified {@link Adapter} object.
 * 
 * @author Aaron Faanes
 * 
 * @param <I>
 *            the type of input value of underlying functions
 * @param <R>
 *            the return type of underlying functions
 * @see AdaptingFunction
 */
public class SafeFunctionAdapter<I, R> implements Adapter<Function<? super I, ? extends R>, SafeFunction<? extends R>> {

	private Adapter<? super Object, ? extends I> adapter;

	public SafeFunctionAdapter(Adapter<? super Object, ? extends I> adapter) {
		this.adapter = adapter;
	}

	public Adapter<? super Object, ? extends I> getAdapter() {
		return this.adapter;
	}

	@Override
	public SafeFunction<? extends R> adapt(Function<? super I, ? extends R> function) {
		if (function == null) {
			return null;
		}
		return new AdaptingSafeFunction<I, R>(this.getAdapter(), function);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SafeFunctionAdapter<?, ?>)) {
			return false;
		}
		final SafeFunctionAdapter<?, ?> other = (SafeFunctionAdapter<?, ?>) obj;
		return this.getAdapter().equals(other.getAdapter());
	}

	@Override
	public int hashCode() {
		int result = 19;
		result = 31 * this.getAdapter().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("SafeFunctionAdapter[%s]", this.getAdapter());
	}

}
