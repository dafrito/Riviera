package logic.functions.curryable;

import logic.adapters.Adapter;
import logic.functions.Function;
import logic.functions.SafeFunction;

/**
 * Protect a {@link Curryable} via adapters.
 * 
 * @author Aaron Faanes
 * 
 * @param <C>
 *            the type of the curried value. This is also the type that
 * @param <I>
 *            the type of the input value from functions produced by the
 *            underlying curryable
 * @param <R>
 *            the type of return value from functions produced by the underlying
 *            {@link Curryable}
 */
public class AdaptingSafeCurryable<C, I, R> implements SafeCurryable<R> {

	private final Adapter<? super Object, ? extends C> curryAdapter;
	private final Curryable<? super C, ? extends Function<? super I, ? extends R>> curryable;
	private final Adapter<? super Function<? super I, ? extends R>, ? extends SafeFunction<? extends R>> functionAdapter;

	/**
	 * Protect the specified {@link Curryable} through adapters. The curried
	 * value is adapted to the expected type {@code C} for the underlying
	 * curryable. The returned function is similarly protected by adapted
	 * received values to the function's input type {@code I}.
	 * 
	 * @param curryAdapter
	 *            an {@link Adapter} that converts any object to a type accepted
	 *            by the underlying curryable
	 * @param curryable
	 *            the {@link Curryable} that is protected by this invocation
	 * @param functionAdapter
	 *            an {@link Adapter} that converts functions returned by the
	 *            underlying {@link Curryable} to {@link SafeFunction} objects
	 */
	public AdaptingSafeCurryable(
			Adapter<? super Object, ? extends C> curryAdapter,
			Curryable<? super C, ? extends Function<? super I, ? extends R>> curryable,
			Adapter<? super Function<? super I, ? extends R>, ? extends SafeFunction<? extends R>> functionAdapter) {
		if (curryAdapter == null) {
			throw new NullPointerException("curryAdapter must not be null");
		}
		this.curryAdapter = curryAdapter;
		if (curryable == null) {
			throw new NullPointerException("curryable must not be null");
		}
		this.curryable = curryable;
		if (functionAdapter == null) {
			throw new NullPointerException("functionAdapter must not be null");
		}
		this.functionAdapter = functionAdapter;
	}

	/**
	 * @return the {@link Adapter} that adapts values so they are accepted to
	 *         the underlying {@link Curryable} object
	 */
	public Adapter<? super Object, ? extends C> getCurryAdapter() {
		return this.curryAdapter;
	}

	/**
	 * @return the {@link Curryable} that produces {@link Function} objects
	 */
	public Curryable<? super C, ? extends Function<? super I, ? extends R>> getCurryable() {
		return this.curryable;
	}

	/**
	 * @return the {@link Adapter} that converts {@link Function} objects
	 *         returned by the underlying {@link Curryable} to
	 *         {@link SafeFunction} objects
	 */
	public Adapter<? super Function<? super I, ? extends R>, ? extends SafeFunction<? extends R>> getFunctionAdapter() {
		return this.functionAdapter;
	}

	@Override
	public SafeFunction<? extends R> curry(Object value) {
		return this.functionAdapter.adapt(this.curryable.curry(this.curryAdapter.adapt(value)));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AdaptingSafeCurryable<?, ?, ?>)) {
			return false;
		}
		AdaptingSafeCurryable<?, ?, ?> other = (AdaptingSafeCurryable<?, ?, ?>) obj;
		return this.getCurryAdapter().equals(other.getCurryAdapter()) &&
				this.getCurryable().equals(other.getCurryable()) &&
				this.getFunctionAdapter().equals(other.getFunctionAdapter());
	}

	@Override
	public int hashCode() {
		int result = 41;
		result *= 31 * result + this.getCurryAdapter().hashCode();
		result *= 31 * result + this.getCurryable().hashCode();
		result *= 31 * result + this.getFunctionAdapter().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("AdaptingSafeCurryable[%s, %s, %s]", this.getCurryAdapter(), this.getCurryable(), this.getFunctionAdapter());
	}

}
