package com.bluespot.logic.functions;

import com.bluespot.logic.predicates.Predicate;

/**
 * A {@link Function} that is applied only if its predicate returns true.
 * 
 * @author Aaron Faanes
 * 
 * @param <I>
 *            the type of value received by this function
 * @param <R>
 *            the type of value returned by this function
 */
public class GuardedFunction<I, R> implements Function<I, R> {

	private final Predicate<? super I> guard;
	private final Function<? super I, ? extends R> function;

	public GuardedFunction(Predicate<? super I> guard, Function<? super I, ? extends R> function) {
		if (guard == null) {
			throw new NullPointerException("predicate must not be null");
		}
		this.guard = guard;
		if (function == null) {
			throw new NullPointerException("function must not be null");
		}
		this.function = function;
	}

	@Override
	public R apply(I input) {
		if (this.getGuard().test(input)) {
			return this.getFunction().apply(input);
		}
		return null;
	}

	public Predicate<? super I> getGuard() {
		return this.guard;
	}

	public Function<? super I, ? extends R> getFunction() {
		return this.function;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GuardedFunction<?, ?>)) {
			return false;
		}
		final GuardedFunction<?, ?> other = (GuardedFunction<?, ?>) obj;
		return this.getGuard().equals(other.getGuard()) &&
				this.getFunction().equals(other.getFunction());
	}

	@Override
	public int hashCode() {
		int result = 43;
		result *= 31 * result + getGuard().hashCode();
		result *= 31 * result + getFunction().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("GuardedFunction[%s](%s)", guard, function);
	}

}
