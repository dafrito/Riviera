package com.bluespot.logic.adapters;

import com.bluespot.logic.functions.AdaptingFunction;
import com.bluespot.logic.functions.Function;

/**
 * Adapts the input value accepted by specified {@link Function} objects.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type that will be accepted as input by adapted functions
 * @param <D>
 *            the type of input value of underlying functions
 * @param <R>
 *            the return type of underlying functions
 * @see AdaptingFunction
 */
public class FunctionInputAdapter<S, D, R> implements Adapter<Function<? super D, ? extends R>, Function<? super S, ? extends R>> {

	private Adapter<? super S, ? extends D> adapter;

	public FunctionInputAdapter(Adapter<? super S, ? extends D> adapter) {
		this.adapter = adapter;
	}

	@Override
	public Function<? super S, ? extends R> adapt(Function<? super D, ? extends R> function) {
		if (function == null) {
			return null;
		}
		return new AdaptingFunction<S, D, R>(adapter, function);
	}

	public Adapter<? super S, ? extends D> getAdapter() {
		return this.adapter;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof FunctionInputAdapter<?, ?, ?>)) {
			return false;
		}
		final FunctionInputAdapter<?, ?, ?> other = (FunctionInputAdapter<?, ?, ?>) obj;
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
		return String.format("FunctionInputAdapter[%s]", this.getAdapter());
	}

}
