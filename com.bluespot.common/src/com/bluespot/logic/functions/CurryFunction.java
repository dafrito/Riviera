package com.bluespot.logic.functions;

/**
 * A {@link Function} that curries a specified value to provided
 * {@link Curryable} objects.
 * 
 * @author Aaron Faanes
 * 
 * @param <C>
 *            the type of the curried value
 * @param <R>
 *            the function that is returned as a result of the currying
 */
public class CurryFunction<C, R extends Function<?, ?>> implements Function<Curryable<? super C, ? extends R>, R> {

	private final C curriedValue;

	public CurryFunction(C curriedValue) {
		this.curriedValue = curriedValue;
	}

	public C getCurriedValue() {
		return this.curriedValue;
	}

	@Override
	public R apply(Curryable<? super C, ? extends R> input) {
		return input.curry(this.getCurriedValue());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CurryFunction<?, ?>)) {
			return false;
		}
		CurryFunction<?, ?> other = (CurryFunction<?, ?>) obj;
		return this.getCurriedValue().equals(other.getCurriedValue());
	}

	@Override
	public int hashCode() {
		int result = 23;
		result *= 31 * result + this.getCurriedValue().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("CurryFunction[%s]", this.getCurriedValue());
	}

}
