package com.bluespot.logic.functions;

/**
 * An interface that represents a curryable object. A curryable object behaves
 * similarly to a function in that {@link #curry(Object)} associates some state
 * or context similar to an argument being passed.
 * <p>
 * Implementations do not need to rigidly implement currying as strictly
 * argument-passing. In fact, implementations should <b>not</b> produce
 * degenerate "constant" or "fully applied" functions, such as those made by
 * {@link ValueFunction}. While that behavior fits the formal definition of
 * currying, I don't consider it to be very useful here.
 * <p>
 * Instead, implementations should treat currying as though they are
 * variable-argument functions. For many numeric operations, this translates to
 * an accumulating process. Of course, this is not required: some functions
 * should treat currying in the conservative sense of partially applying
 * results.
 * 
 * @author Aaron Faanes
 * 
 * @param <C>
 *            the type of curried value
 * @param <R>
 *            the function produced by this currying operation
 * @see {@link Functions#curry(Function, Object)}
 */
public interface Curryable<C, R extends Function<?, ?>> {

	/**
	 * Curry the specified value to this object, returning a function that
	 * behaves as though the specified value was applied to it.
	 * 
	 * @param value
	 *            the curried value
	 * @return a new {@link Function} object if the value was successfully
	 *         curried, {@code null} otherwise
	 */
	public R curry(C value);
}
