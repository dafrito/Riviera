package logic.predicates.builder;

import logic.adapters.Adapter;
import logic.predicates.AdaptingPredicate;

/**
 * Returns a builder for creating {@link AdaptingPredicate} objects. While this
 * functions similarly to a {@link PredicateBuilder}, it is not a subtype. This
 * is because {@code AdaptingPredicateBuilder} objects are not intended to stand
 * on their own; instead, they are meant to be added to existing predicate
 * builders.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of the source value
 * @param <D>
 *            the type of the converted value
 */
public final class AdaptingPredicateBuilder<S, D> {

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

	/**
	 * Returns whether this builder's predicate builder has any predicates.
	 * 
	 * @return {@code true} if this builder's predicate builder has predicates,
	 *         otherwise {@code false}
	 */
	public boolean hasPredicates() {
		return this.getBuilder().hasPredicates();
	}

	/**
	 * Constructs a new {@link AdaptingPredicate} that uses this builder's
	 * adapter with this builder's predicate builder's constructed predicate.
	 * 
	 * @return a new {@link AdaptingPredicate} that is constructed from the
	 *         state of this builder
	 */
	public AdaptingPredicate<S, D> build() {
		return new AdaptingPredicate<S, D>(this.adapter, this.builder.build());

	}

	@Override
	public String toString() {
		return String.format("has %s that is %s", this.getAdapter(), this.getBuilder());
	}

	/**
	 * Creates a new {@link AdaptingPredicateBuilder} that uses the type
	 * parameters of the specified adapter. This makes creating new builders
	 * less verbose, and also allows us to "clone" adapters.
	 * 
	 * 
	 * @param <S>
	 *            the type of the source value
	 * @param <D>
	 *            the type of the converted value
	 * @param adapter
	 *            the adapter used for conversion
	 * @return a new {@code AdaptingPredicateBuilder} object
	 */
	public static <S, D> AdaptingPredicateBuilder<S, D> newBuilder(final Adapter<? super S, D> adapter) {
		return new AdaptingPredicateBuilder<S, D>(adapter);
	}

}