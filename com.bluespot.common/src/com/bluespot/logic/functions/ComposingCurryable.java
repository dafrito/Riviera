package com.bluespot.logic.functions;

/**
 * Compose provided functions using a specified inner-function. This object is
 * both a {@link Function} and a {@link Curryable}. Invoking
 * {@link #apply(Object)} will invoke the underlying inner-function with the
 * specified value. Invoking {@link #curry(Function)} will compose the
 * underlying inner function with the specified function, such that the returned
 * produced function evaluates to: {@code func.apply(this.apply(input))};
 * 
 * @author Aaron Faanes
 * 
 * @param <I>
 *            the type of input value received by {@link Function} objects
 *            produced by this {@link Curryable}
 * @param <R>
 *            the type of value returned by {@link Function} objects produced by
 *            this {@link Curryable}
 * 
 */
public class ComposingCurryable<I, R> implements Function<I, R>, Curryable<Function<? super R, ? extends R>, ComposingCurryable<? super I, R>> {

	private final Function<? super I, ? extends R> innerFunction;

	/**
	 * @param innerFunction
	 *            the function that is wrapped by this object
	 */
	public ComposingCurryable(Function<? super I, ? extends R> innerFunction) {
		if (innerFunction == null) {
			throw new NullPointerException("innerFunction must not be null");
		}
		this.innerFunction = innerFunction;
	}

	/**
	 * @return the innerFunction
	 */
	public Function<? super I, ? extends R> getInnerFunction() {
		return this.innerFunction;
	}

	@Override
	public R apply(I input) {
		return this.getInnerFunction().apply(input);
	}

	@Override
	public ComposingCurryable<? super I, R> curry(Function<? super R, ? extends R> outerFunction) {
		return new ComposingCurryable<I, R>(Functions.compose(this, outerFunction));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ComposingCurryable<?, ?>)) {
			return false;
		}
		ComposingCurryable<?, ?> other = (ComposingCurryable<?, ?>) obj;
		return this.getInnerFunction().equals(other.getInnerFunction());
	}

	@Override
	public int hashCode() {
		int result = 29;
		result = 31 * result + this.getInnerFunction().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("ComposingCurryable[%s]", this.getInnerFunction());
	}
}
