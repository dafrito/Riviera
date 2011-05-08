package com.bluespot.logic.functions;

import java.util.Collection;
import java.util.List;

/**
 * A short-circuiting {@link Function} implementaiton. This function returns the
 * first non-null output it encounters.
 * 
 * @author Aaron Faanes
 * 
 * @param <I>
 *            the type of input value provided to each function
 * @param <V>
 *            the type of value returned by this function
 */
public class UnilateralFunction<I, V> extends AbstractCompositeFunction<I, V> {

	public UnilateralFunction(final Collection<Function<? super I, ? extends V>> functions) {
		super(functions);
	}

	@Override
	public V apply(I input) {
		for (final Function<? super I, ? extends V> function : this.getFunctions()) {
			V result = function.apply(input);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UnilateralFunction<?, ?>)) {
			return false;
		}
		final UnilateralFunction<?, ?> other = (UnilateralFunction<?, ?>) obj;
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
		final StringBuilder builder = new StringBuilder("UnilateralFunction[");
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