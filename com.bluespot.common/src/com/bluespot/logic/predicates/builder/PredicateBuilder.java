package com.bluespot.logic.predicates.builder;

import java.util.ArrayList;
import java.util.List;

import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.predicates.Predicates;
import com.bluespot.logic.predicates.UnanimousPredicate;

/**
 * A builder that constructs {@link Predicate} objects in a readable fashion.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of test value for the constructed predicate
 */
public class PredicateBuilder<T> {

	private final AndPredicateBuilder<T> andBuilder = new AndPredicateBuilder<T>();

	private final List<AdaptingPredicateBuilder<? super T, ?>> subBuilders = new ArrayList<AdaptingPredicateBuilder<? super T, ?>>();

	private final List<OrPredicateBuilder<T>> orBuilders = new ArrayList<OrPredicateBuilder<T>>();

	/**
	 * Creates an {@link AdaptingPredicateBuilder} that uses the specified
	 * adapter for conversion. The constructed predicate will evaluate to
	 * {@code true} if and only if the specified builder's constructed predicate
	 * evaluates to {@code true}. Adapting predicates are useful when you're
	 * writing expressions that test some component of a given test value.
	 * <p>
	 * This builder will implicitly add a {@link Predicates#notNullValue()}
	 * predicate to any builder created by this method if that builder is empty
	 * during construction. (Emptiness is determined by
	 * {@link PredicateBuilder#hasPredicates()}). This allows for this intuitive
	 * behavior to work properly:
	 * 
	 * <pre>
	 * builder.has(childFile(&quot;hello.txt&quot;));
	 * </pre>
	 * 
	 * If we didn't add the rule, the adapting predicate would always evaluate
	 * to {@code true}. If the builder is not empty, then no predicates will be
	 * added. Otherwise, the following rule would never be true:
	 * 
	 * <pre>
	 * builder.has(childFile(&quot;hello.txt&quot;)).that(is(nullValue()));
	 * </pre>
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
	 * predicate. This builder's constructed predicate will evaluate to
	 * {@code true} if and only if at least one of the returned builder's child
	 * predicates evaluate to {@code true}.
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
	 * Adds the specified predicate to this builder. The constructed predicate
	 * will evaluate to {@code true} if and only if the specified predicate will
	 * evaluate to {@code true}.
	 * 
	 * @param predicate
	 *            the predicate to add to this builder
	 * @see UnanimousPredicate
	 */
	public void addRequirement(final Predicate<? super T> predicate) {
		this.andBuilder.and(predicate);
	}

	/**
	 * Adds the specified predicate to this builder. This is an alias for
	 * {@link #addRequirement(Predicate)}.
	 * 
	 * @param predicate
	 *            the predicate to add to this builder
	 * @return this builder
	 */
	public PredicateBuilder<T> and(final Predicate<? super T> predicate) {
		this.addRequirement(predicate);
		return this;
	}

	/**
	 * Adds the specified predicate to this builder. This is an alias for
	 * {@link #addRequirement(Predicate)}.
	 * 
	 * @param predicate
	 *            the predicate to add to this builder
	 * @return this builder
	 */
	public PredicateBuilder<T> is(final Predicate<? super T> predicate) {
		this.addRequirement(predicate);
		return this;
	}

	/**
	 * Adds the specified predicate to this builder. This is an alias for
	 * {@link #addRequirement(Predicate)}.
	 * 
	 * @param predicate
	 *            the predicate to add to this builder
	 * @return this builder
	 */
	public PredicateBuilder<T> that(final Predicate<? super T> predicate) {
		this.addRequirement(predicate);
		return this;
	}

	/**
	 * Returns whether this builder contains any child predicates.
	 * 
	 * @return {@code true} if this builder contains any child predicates,
	 *         otherwise {@code false}
	 */
	public boolean hasPredicates() {
		if (this.andBuilder.hasPredicates()) {
			return true;
		}
		if (!this.subBuilders.isEmpty()) {
			return true;
		}
		if (!this.orBuilders.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Constructs a predicate that represents the predicates in this builder.
	 * This builder may optimize the returned predicate, so redundant or
	 * unnecessary predicates may be removed.
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
		for (final AdaptingPredicateBuilder<? super T, ?> subBuilder : this.subBuilders) {
			if (!subBuilder.hasPredicates()) {
				/*
				 * We assume that empty adapted builders mean
				 * "adapt this value and check if it's null." Therefore, we want
				 * to add Predicates#notNullValue() to any constructed
				 * predicate. However, we don't want to go around adding
				 * predicates to builders that may be used in other places, so
				 * we make a new builder and add our requirement to that.
				 */
				final AdaptingPredicateBuilder<? super T, ?> builder = AdaptingPredicateBuilder.newBuilder(subBuilder.getAdapter());
				builder.getBuilder().addRequirement(Predicates.notNullValue());
				predicates.add(builder.build());
			} else {
				predicates.add(subBuilder.build());
			}
		}

		// Build all 'or' builders
		for (final OrPredicateBuilder<T> orBuilder : this.orBuilders) {
			predicates.add(orBuilder.build());
		}

		if (predicates.isEmpty()) {
			// If we're empty, we're always true, so don't make a more
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
