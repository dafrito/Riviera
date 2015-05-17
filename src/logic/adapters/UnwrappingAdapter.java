/**
 * 
 */
package logic.adapters;

import logic.values.Value;

/**
 * Retrieves a value from a {@link Value} implementation. Null values are
 * returned directly.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of wrapped value
 * @see Value
 */
public class UnwrappingAdapter<T> implements Adapter<Value<? extends T>, T> {

	@Override
	public T adapt(Value<? extends T> source) {
		if (source == null) {
			return null;
		}
		return source.get();
	}

}
