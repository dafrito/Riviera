package com.bluespot.logic.functions.curryable;

import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.functions.AdaptingFunction;
import com.bluespot.logic.functions.Function;

/**
 * A {@link Curryable} that adapts {@link Function} objects produced by an
 * underlying curryable.
 * 
 * 
 * @author Aaron Faanes
 * 
 * @param <C>
 *            the type of the curried value. It's not adapted in any way.
 * @param <I>
 *            the type of the initial value that is received by functions
 *            produced by this curryable. It is also the type accepted by the
 *            specified adapter.
 * @param <A>
 *            the type that is accepted by underlying functions produced by this
 *            curryable. It is also the type that is produced by the specified
 *            adapter.
 * @param <R>
 *            the type of returned value from the adapted function. It is not
 *            modified by this object.
 */
public class WrappingCurryable<C, I, A, R> implements Curryable<C, Function<? super I, ? extends R>> {

	private final Curryable<? super C, ? extends Function<? super A, ? extends R>> curryable;
	private final Adapter<? super I, ? extends A> adapter;

	public WrappingCurryable(Curryable<? super C, ? extends Function<? super A, ? extends R>> curryable, Adapter<? super I, ? extends A> adapter) {
		if (adapter == null) {
			throw new NullPointerException("adapter must not be null");
		}
		this.adapter = adapter;
		if (curryable == null) {
			throw new NullPointerException("curryable must not be null");
		}
		this.curryable = curryable;
	}

	public Curryable<? super C, ? extends Function<? super A, ? extends R>> getCurryable() {
		return curryable;
	}

	public Adapter<? super I, ? extends A> getAdapter() {
		return adapter;
	}

	@Override
	public Function<? super I, ? extends R> curry(C value) {
		Function<? super A, ? extends R> function = this.curryable.curry(value);
		if (function == null) {
			return null;
		}
		return new AdaptingFunction<I, A, R>(adapter, function);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof WrappingCurryable<?, ?, ?, ?>)) {
			return false;
		}
		final WrappingCurryable<?, ?, ?, ?> other = (WrappingCurryable<?, ?, ?, ?>) obj;
		return this.getAdapter().equals(other.getAdapter()) &&
				this.getCurryable().equals(other.getCurryable());
	}

	@Override
	public int hashCode() {
		int result = 13;
		result = 31 * this.getAdapter().hashCode();
		result = 31 * this.getCurryable().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("WrappingCurryable[%s, %s]", this.getAdapter(), this.getCurryable());
	}

}