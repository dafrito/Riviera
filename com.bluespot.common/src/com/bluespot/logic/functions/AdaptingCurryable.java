package com.bluespot.logic.functions;

import com.bluespot.logic.adapters.Adapter;

public class AdaptingCurryable<I, A, V extends Function<?, ?>> implements Curryable<I, V> {

	private final Adapter<? super I, ? extends A> adapter;
	private final Curryable<? super A, ? extends V> curryable;

	public AdaptingCurryable(Adapter<? super I, ? extends A> adapter, Curryable<? super A, ? extends V> curryable) {
		if (adapter == null) {
			throw new NullPointerException("adapter must not be null");
		}
		this.adapter = adapter;
		if (curryable == null) {
			throw new NullPointerException("curryable must not be null");
		}
		this.curryable = curryable;
	}

	public Adapter<? super I, ? extends A> getAdapter() {
		return adapter;
	}

	public Curryable<? super A, ? extends V> getCurryable() {
		return curryable;
	}

	@Override
	public V curry(I value) {
		return this.curryable.curry(this.adapter.adapt(value));
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof AdaptingFunction<?, ?, ?>)) {
			return false;
		}
		final AdaptingCurryable<?, ?, ?> other = (AdaptingCurryable<?, ?, ?>) obj;
		return this.getAdapter().equals(other.getAdapter()) &&
				this.getCurryable().equals(other.getCurryable());
	}

	@Override
	public int hashCode() {
		int result = 19;
		result = 31 * this.getAdapter().hashCode();
		result = 31 * this.getCurryable().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("AdaptingCurryable[%s, %s]", this.getAdapter(), this.getCurryable());
	}

}
