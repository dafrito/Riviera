package com.bluespot.logic.functions;

import com.bluespot.logic.adapters.Adapter;

/**
 * A {@link Function} that wraps an {@link Adapter}.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of the input value accepted by this adapter
 * @param <D>
 *            the type of the returned output value from this adapter
 */
public class AdapterFunction<S, D> implements Function<S, D> {

	private final Adapter<? super S, ? extends D> adapter;

	public AdapterFunction(Adapter<? super S, ? extends D> adapter) {
		if (adapter == null) {
			throw new NullPointerException("Adapter must not be null");
		}
		this.adapter = adapter;
	}

	public Adapter<? super S, ? extends D> getAdapter() {
		return this.adapter;
	}

	@Override
	public D apply(S input) {
		return adapter.adapt(input);
	}

	@Override
	public String toString() {
		return String.format("AdapterFunction[%s]", adapter);
	}

	@Override
	public int hashCode() {
		int result = 7;
		result *= 31 * result + getAdapter().hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AdapterFunction<?, ?>)) {
			return false;
		}
		final AdapterFunction<?, ?> other = (AdapterFunction<?, ?>) obj;
		return this.getAdapter().equals(other.getAdapter());
	}

}