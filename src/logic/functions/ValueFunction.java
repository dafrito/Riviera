package logic.functions;

import logic.values.Value;

/**
 * A {@link Function} that returns a value retrieved from a {@link Value}. The
 * input is ignored, even if it is {@code null}.
 * 
 * @author Aaron Faanes
 * 
 * @param <V>
 *            the type of the returned value
 */
public class ValueFunction<V> implements SafeFunction<V> {

	private final Value<? extends V> value;

	public ValueFunction(Value<? extends V> value) {
		if (value == null) {
			throw new NullPointerException("Value must not be null");
		}
		this.value = value;
	}

	public Value<? extends V> getValue() {
		return this.value;
	}

	@Override
	public V apply(Object input) {
		return value.get();
	}

	@Override
	public String toString() {
		return String.format("ValueFunction[%s]", value);
	}

	@Override
	public int hashCode() {
		int result = 97;
		result *= 31 * result + getValue().hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ValueFunction<?>)) {
			return false;
		}
		final ValueFunction<?> other = (ValueFunction<?>) obj;
		return this.getValue().equals(other.getValue());
	}

}
