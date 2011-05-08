package com.bluespot.logic.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An {@link Adapter} implementation that uses multiple options.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of the source value
 * @param <D>
 *            the type of the returned value
 */
public class UnilateralAdapter<S, D> implements Adapter<S, D> {

	private final List<Adapter<? super S, ? extends D>> options;

	/**
	 * Creates a composite adapter that uses the adapters in the specified
	 * collection.
	 * 
	 * @param options
	 *            the adapters used in this object. The order is significant, as
	 *            the first adapter that returns a non-null value will be used.
	 * @throws IllegalArgumentException
	 *             if {@code choices} is empty. Empty composite adapters do not
	 *             make sense, so they're not allowed.
	 * @throws NullPointerException
	 *             if {@code choices} or any adapter in {@code choices} is null
	 */
	public UnilateralAdapter(final Collection<Adapter<? super S, ? extends D>> options) {
		if (options == null) {
			throw new NullPointerException("choices must not be null");
		}
		if (options.isEmpty()) {
			throw new IllegalArgumentException("choices must contain at least one adapter");
		}
		this.options = Collections.unmodifiableList(new ArrayList<Adapter<? super S, ? extends D>>(options));
		for (final Adapter<? super S, ? extends D> adapter : this.options) {
			if (adapter == null) {
				throw new NullPointerException("choices must not contain null values");
			}
		}
	}

	/**
	 * @return an unmodifiable list of adapters used by this object
	 */
	public List<Adapter<? super S, ? extends D>> getOptions() {
		return this.options;
	}

	@Override
	public D adapt(S source) {
		for (final Adapter<? super S, ? extends D> adapter : this.options) {
			D result = adapter.adapt(source);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

}