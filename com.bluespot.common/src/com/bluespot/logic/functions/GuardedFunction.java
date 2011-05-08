package com.bluespot.logic.functions;

import com.bluespot.logic.predicates.Predicate;

/**
 * A {@link Function} that is applied only if its predicate returns true.
 * 
 * @author dafrito
 * 
 * @param <I>
 *            the type of value received by this function
 * @param <R>
 *            the type of value returned by this function
 */
public class GuardedFunction<I, R> implements Function<I, R> {

	private final Predicate<? super I> predicate;
	private final Function<? super I, ? extends R> function;

	public GuardedFunction(Predicate<? super I> predicate, Function<? super I, ? extends R> function) {
		if (predicate == null) {
			throw new NullPointerException("predicate must not be null");
		}
		this.predicate = predicate;
		if (function == null) {
			throw new NullPointerException("function must not be null");
		}
		this.function = function;
	}

	@Override
	public R apply(I input) {
		if (this.getPredicate().test(input)) {
			return this.getFunction().apply(input);
		}
		return null;
	}

	public Predicate<? super I> getPredicate() {
		return this.predicate;
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
		return this.getPredicate().equals(other.getPredicate()) &&
				this.getFunction().equals(other.getFunction());
	}

	@Override
	public int hashCode() {
		int result = 43;
		result *= 31 * result + getPredicate().hashCode();
		result *= 31 * result + getFunction().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("GuardedFunction[%s](%s)", predicate, function);
	}

}
