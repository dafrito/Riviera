package logic.functions;

import logic.adapters.Adapter;

/**
 * A {@link SafeFunction} that adapts inputs using a specified {@link Adapter}.
 * 
 * @author Aaron Faanes
 * 
 * @param <D>
 *            the type of input value accepted by the underlying function
 * @param <R>
 *            the type of return value from the produced function. This type is
 *            not modified by this object.
 */
public class AdaptingSafeFunction<D, R> extends AdaptingFunction<Object, D, R> implements SafeFunction<R> {
	public AdaptingSafeFunction(Adapter<? super Object, ? extends D> adapter, Function<? super D, ? extends R> function) {
		super(adapter, function);
	}
}