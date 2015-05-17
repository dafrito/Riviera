package logic.functions.curryable;

import logic.functions.Function;
import logic.functions.Functions;
import logic.functions.SafeFunction;

/**
 * Compose safe functions together. This object is both a {@link Function} and a
 * {@link Curryable}. Invoking {@link #apply(Object)} will invoke the underlying
 * inner-function with the specified value.
 * <p>
 * Invoking {@link #curry(Function)} will compose the underlying inner function
 * with the specified function, such that the returned produced function
 * evaluates to: {@code func.apply(this.apply(input))}. The produced function's
 * return type will be {@code Object} since no return type information is
 * preserved at runtime.
 * <p>
 * The {@link SafeFunction} marker interface is used to distinguish functions
 * that can accept any argument. As a result,
 * 
 * @author Aaron Faanes
 * 
 * @param <R>
 *            the type of value returned by the underlying function.
 * 
 */
public class SafeComposingCurryable<R> implements SafeFunction<R>, SafeCurryable<Object> {

	private final Function<? super Object, ? extends R> innerFunction;

	public SafeComposingCurryable() {
		this(Functions.<R> safeIdentity());
	}

	/**
	 * @param innerFunction
	 *            the function that is wrapped by this object
	 */
	public SafeComposingCurryable(Function<? super Object, ? extends R> innerFunction) {
		if (innerFunction == null) {
			throw new NullPointerException("innerFunction must not be null");
		}
		this.innerFunction = innerFunction;
	}

	/**
	 * @return the innerFunction
	 */
	public Function<? super Object, ? extends R> getInnerFunction() {
		return this.innerFunction;
	}

	@Override
	public R apply(Object input) {
		return this.getInnerFunction().apply(input);
	}

	@Override
	public SafeComposingCurryable<Object> curry(Object obj) {
		if (obj == null || !SafeFunction.class.isInstance(obj)) {
			return null;
		}
		return new SafeComposingCurryable<Object>(Functions.compose(this, (SafeFunction<?>) obj));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SafeComposingCurryable<?>)) {
			return false;
		}
		SafeComposingCurryable<?> other = (SafeComposingCurryable<?>) obj;
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
		return String.format("SafeComposingCurryable[%s]", this.getInnerFunction());
	}
}
