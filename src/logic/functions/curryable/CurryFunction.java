package logic.functions.curryable;

import logic.functions.Function;

/**
 * A {@link Function} that curries a specified value to provided
 * {@link Curryable} objects.
 * 
 * @author Aaron Faanes
 * 
 * @param <C>
 *            the type of the curried value. {@link Curryable} input values will
 *            be passed an object of this type.
 * @param <F>
 *            the function that is returned as a result of the currying
 */
public class CurryFunction<C, F extends Function<?, ?>> implements Function<Curryable<? super C, ? extends F>, F> {

	private final C curriedValue;

	/**
	 * Create a new {@link CurryFunction} that curries the specified value.
	 * 
	 * @param curriedValue
	 *            the value to curry.
	 */
	public CurryFunction(C curriedValue) {
		if (curriedValue == null) {
			throw new NullPointerException("curriedValue must not be null");
		}
		this.curriedValue = curriedValue;
	}

	/**
	 * @return the value used in currying.
	 */
	public C getCurriedValue() {
		return this.curriedValue;
	}

	@Override
	public F apply(Curryable<? super C, ? extends F> input) {
		if (input == null) {
			return null;
		}
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
