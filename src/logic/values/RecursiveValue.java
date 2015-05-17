/**
 * 
 */
package logic.values;

import logic.functions.Function;

/**
 * A {@link Value} that feeds an underlying {@link Function} with its own
 * outputs.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value, and the common supertype between the
 *            function's input and output
 * @see Values#recurse(Function, Object)
 * @see Function
 */
public class RecursiveValue<T> implements Value<T> {

	private T input;
	private final Function<? super T, ? extends T> function;

	public RecursiveValue(Function<? super T, ? extends T> function, T initial) {
		if (function == null) {
			throw new NullPointerException("function must not be null");
		}
		this.function = function;
		this.input = initial;
	}

	@Override
	public T get() {
		T rv = input;
		this.input = this.function.apply(this.input);
		return rv;
	}
}
