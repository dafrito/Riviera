package com.bluespot.logic.functions;

import com.bluespot.logic.adapters.Adapter;

/**
 * A {@link Function} that adapts a value before passing it to an underlying
 * {@link Function}.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of the initial value received by this function. It is
 *            also the type accepted by the adapter.
 * @param <D>
 *            the type that is accepted by the underlying function. It is also
 *            the type that is produced by the specified adapter.
 * @param <R>
 *            the type that is returned by this function. It is not modified by
 *            this object.
 */
public class AdaptingFunction<S, D, R> implements Function<S, R> {

	private final Adapter<? super S, ? extends D> adapter;
	private final Function<? super D, ? extends R> function;

	public AdaptingFunction(Adapter<? super S, ? extends D> adapter, Function<? super D, ? extends R> function) {
		this.adapter = adapter;
		this.function = function;
	}

	public Adapter<? super S, ? extends D> getAdapter() {
		return adapter;
	}

	public Function<? super D, ? extends R> getFunction() {
		return function;
	}

	@Override
	public R apply(S input) {
		return this.function.apply(this.adapter.adapt(input));
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof AdaptingFunction<?, ?, ?>)) {
			return false;
		}
		final AdaptingFunction<?, ?, ?> other = (AdaptingFunction<?, ?, ?>) obj;
		return this.getAdapter().equals(other.getAdapter()) &&
				this.getFunction().equals(other.getFunction());
	}

	@Override
	public int hashCode() {
		int result = 23;
		result = 31 * this.getAdapter().hashCode();
		result = 31 * this.getFunction().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("has %s that %s", this.getAdapter(), this.getFunction());
	}

}
