package logic.functions;

import java.util.Collection;
import java.util.List;

/**
 * A {@link Function} that operates by committee. This function returns an
 * output if and only if all child functions evaluate to that same result.
 * 
 * @author Aaron Faanes
 * 
 * @param <I>
 *            the type of input value provided to each function
 * @param <V>
 *            the type of value returned by this function
 */
public class UnanimousFunction<I, V> extends AbstractCompositeFunction<I, V> {

	public UnanimousFunction(final Collection<? extends Function<? super I, ? extends V>> functions) {
		super(functions);
	}

	@Override
	public V apply(I input) {
		V result = null;
		for (final Function<? super I, ? extends V> function : this.getFunctions()) {
			V thisResult = function.apply(input);
			if (thisResult == null) {
				// No reason to continue since our returned value will always be null
				// regardless of what subsequent functions return.
				return null;
			}
			if (result == null) {
				// First result
				result = thisResult;
			} else if (!result.equals(thisResult)) {
				// No consensus, so return null
				return null;
			}
		}
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UnanimousFunction<?, ?>)) {
			return false;
		}
		final UnanimousFunction<?, ?> other = (UnanimousFunction<?, ?>) obj;
		return this.getFunctions().equals(other.getFunctions());
	}

	@Override
	public int hashCode() {
		int result = 3;
		result = 31 * result + this.getFunctions().hashCode();
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder("UnanimousFunction[");
		final List<? extends Function<? super I, ? extends V>> functions = this.getFunctions();
		for (int i = 0; i < functions.size(); i++) {
			if (i > 0 && i < functions.size() - 1) {
				builder.append(", ");
			}
			builder.append(functions.get(i));
		}
		builder.append(']');
		return builder.toString();
	}

}
