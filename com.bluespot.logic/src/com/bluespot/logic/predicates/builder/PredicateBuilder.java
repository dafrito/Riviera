package com.bluespot.logic.predicates.builder;

import java.util.ArrayList;
import java.util.List;

import com.bluespot.logic.Predicates;
import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.predicates.UnanimousPredicate;

/**
 * A builder that constructs predicates in a readable fashion.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of test value for the constructed predicate
 */
public class PredicateBuilder<T> {

	private final AndPredicateBuilder<T> andBuilder = new AndPredicateBuilder<T>();

	private final List<PredicateBuilder<T>> subBuilders = new ArrayList<PredicateBuilder<T>>();

	private final List<OrPredicateBuilder<T>> orBuilders = new ArrayList<OrPredicateBuilder<T>>();

	/**
	 * Creates an {@link PredicateBuilder} that uses the specified adapter for
	 * conversion. Useful when writing expressions that test some component of a
	 * given test value.
	 * 
	 * @param <D>
	 *            the type of the converted value
	 * @param adapter
	 *            the adapter that performs the conversion
	 * @return a builder for the constructed adapted value
	 */
	public <D> PredicateBuilder<D> has(final Adapter<? super T, D> adapter) {
		final AdaptingPredicateBuilder<T, D> builder = new AdaptingPredicateBuilder<T, D>(adapter);
		this.subBuilders.add(builder);
		return builder.getBuilder();
	}

	/**
	 * Returns a {@link OrPredicateBuilder} that includes the specified
	 * predicate.
	 * 
	 * @param predicate
	 *            the first predicate to add to the predicate builder
	 * @return the created {@code OrPredicateBuilder} object
	 */
	public OrPredicateBuilder<T> anyOf(final Predicate<? super T> predicate) {
		final OrPredicateBuilder<T> builder = new OrPredicateBuilder<T>();
		this.orBuilders.add(builder);
		builder.or(predicate);
		return builder;
	}

	/**
	 * Adds the specified predicate to this builder.
	 * 
	 * @param predicate
	 *            the predicate to add to this builder
	 * @return this builder
	 */
	public PredicateBuilder<T> and(final Predicate<? super T> predicate) {
		return this.is(predicate);
	}

	/**
	 * Adds the specified predicate to this builder. This is an alias for
	 * {@link #and(Predicate)}, but is useful for readability purposes.
	 * 
	 * @param predicate
	 *            the predicate to add to this builder
	 * @return this builder
	 */
	public PredicateBuilder<T> is(final Predicate<? super T> predicate) {
		this.andBuilder.and(predicate);
		return this;
	}

	/**
	 * Adds the specified predicate to this builder. This is an alias for
	 * {@link #and(Predicate)}, but is useful for readability purposes.
	 * 
	 * @param predicate
	 *            the predicate to add to this builder
	 * @return this builder
	 */
	public PredicateBuilder<T> that(final Predicate<? super T> predicate) {
		return this.is(predicate);
	}

	/**
	 * Constructs a predicate that represents the predicates in this builder.
	 * This builder may optimize the returned predicate, so the order or format
	 * may be modified. The meaning of the constructed predicate is not
	 * affected, but ensure that the predicates used do not depend on the order
	 * of invocation; all predicates are self-contained and atomic.
	 * 
	 * @return a predicate that represents the predicates in this builder
	 */
	public Predicate<? super T> build() {
		final List<Predicate<? super T>> predicates = new ArrayList<Predicate<? super T>>();

		// Build all 'and' predicates
		if (this.andBuilder.hasPredicates()) {
			predicates.add(this.andBuilder.build());
		}

		// Build all 'has' predicates
		for (final PredicateBuilder<T> subBuilder : this.subBuilders) {
			predicates.add(subBuilder.build());
		}

		// Build all 'or' builders
		for (final OrPredicateBuilder<T> orBuilder : this.orBuilders) {
			predicates.add(orBuilder.build());
		}

		if (predicates.isEmpty()) {
			// If we're empty, we're always true, so don't even make a more
			// complicated predicate
			return Predicates.truth();
		}

		if (predicates.size() == 1) {
			// Optimization so we don't unnecessarily nest these
			return predicates.get(0);
		}

		return new UnanimousPredicate<T>(predicates);
	}
}
