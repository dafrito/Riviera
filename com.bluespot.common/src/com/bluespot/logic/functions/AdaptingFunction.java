package com.bluespot.logic.functions;

import com.bluespot.logic.adapters.Adapter;

/**
 * A {@link Function} that adapts a value before passing it to another
 * {@link Function}.
 * 
 * @author Aaron Faanes
 * 
 * @param <I>
 *            the type of the initial value received by this function
 * @param <A>
 *            the common type of the adapted value and the input to the
 *            specified function
 * @param <V>
 *            the type that is returned by this function
 */
public class AdaptingFunction<I, A, V> implements Function<I, V> {

	private final Adapter<? super I, ? extends A> adapter;
	private final Function<? super A, ? extends V> function;

	public AdaptingFunction(Adapter<? super I, ? extends A> adapter, Function<? super A, ? extends V> function) {
		this.adapter = adapter;
		this.function = function;
	}

	public Adapter<? super I, ? extends A> getAdapter() {
		return adapter;
	}

	public Function<? super A, ? extends V> getFunction() {
		return function;
	}

	@Override
	public V apply(I input) {
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
