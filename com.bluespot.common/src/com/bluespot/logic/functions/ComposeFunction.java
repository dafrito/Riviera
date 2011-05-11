/**
 * 
 */
package com.bluespot.logic.functions;

/**
 * Compose two {@link Function} objects together.
 * 
 * @author Aaron Faanes
 * @param <I>
 *            the type of the inner function's input value. It is not modified
 *            by this object.
 * @param <K>
 *            the common type between the inner function's return value and the
 *            outer function's input value.
 * @param <R>
 *            the outer function's return value. It is not modified by this
 *            object.
 * 
 */
public class ComposeFunction<I, K, R> implements Function<I, R> {

	private final Function<? super K, ? extends R> outerFunction;

	private final Function<? super I, ? extends K> innerFunction;

	public ComposeFunction(Function<? super I, ? extends K> innerFunction, Function<? super K, ? extends R> outerFunction) {
		if (outerFunction == null) {
			throw new NullPointerException("outerFunction must not be null");
		}
		this.outerFunction = outerFunction;
		if (innerFunction == null) {
			throw new NullPointerException("innerFunction must not be null");
		}
		this.innerFunction = innerFunction;
	}

	/**
	 * @return the outer function that is evaluated using the result of the
	 *         inner function
	 * @see #getInnerFunction()
	 */
	public Function<? super K, ? extends R> getOuterFunction() {
		return this.outerFunction;
	}

	/**
	 * @return the inner function that is evaluated first during composition
	 * @see #getOuterFunction()
	 */
	public Function<? super I, ? extends K> getInnerFunction() {
		return this.innerFunction;
	}

	@Override
	public R apply(I input) {
		return this.outerFunction.apply(this.innerFunction.apply(input));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ComposeFunction<?, ?, ?>)) {
			return false;
		}
		ComposeFunction<?, ?, ?> other = (ComposeFunction<?, ?, ?>) obj;
		return this.getInnerFunction().equals(other.getInnerFunction()) &&
				this.getOuterFunction().equals(other.getOuterFunction());
	}

	@Override
	public int hashCode() {
		int result = 67;
		result = 31 * result + this.getInnerFunction().hashCode();
		result = 31 * result + this.getOuterFunction().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("Compose[%s -> %s]", this.getInnerFunction(), this.getOuterFunction());
	}
}
