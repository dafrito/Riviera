package com.bluespot.logic.predicates.builder;

import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.predicates.AdaptingPredicate;

/**
 * Returns a builder for creating {@link AdaptingPredicate} objects.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of the source value
 * @param <D>
 *            the type of the converted value
 */
public final class AdaptingPredicateBuilder<S, D> extends PredicateBuilder<S> {

	private final Adapter<? super S, D> adapter;
	private final PredicateBuilder<D> builder;

	/**
	 * Constructs a new builder using the specified adapter for conversion.
	 * 
	 * @param adapter
	 *            the adapter used for conversion
	 */
	public AdaptingPredicateBuilder(final Adapter<? super S, D> adapter) {
		if (adapter == null) {
			throw new NullPointerException("adapter is null");
		}
		this.adapter = adapter;
		this.builder = new PredicateBuilder<D>();
	}

	/**
	 * Returns the adapter used in conversion.
	 * 
	 * @return the adapter used in conversion
	 */
	public Adapter<? super S, D> getAdapter() {
		return this.adapter;
	}

	/**
	 * Returns the builder used to construct this predicate. This refers to the
	 * predicate for the converted value.
	 * 
	 * @return the builder used to construct this predicate.
	 */
	public PredicateBuilder<D> getBuilder() {
		return this.builder;
	}

	@Override
	public AdaptingPredicate<S, D> build() {
		return new AdaptingPredicate<S, D>(this.adapter, this.builder.build());
	}

	@Override
	public String toString() {
		return String.format("has %s that is %s", this.getAdapter(), this.getBuilder());
	}

}