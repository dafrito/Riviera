package com.bluespot.logic.predicates;

import java.util.Collection;
import java.util.List;

/**
 * A predicate that evaluates to {@code true} if at least one of its predicates
 * evaluates to {@code true}.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of the tested value
 */
public final class UnilateralPredicate<T> extends AbstractCompositePredicate<T> {

	/**
	 * Constructs a unilateral predicate using the specified predicates.
	 * 
	 * @param predicates
	 *            the predicates used in this predicate
	 */
	public UnilateralPredicate(final Collection<Predicate<? super T>> predicates) {
		super(predicates);
	}

	@Override
	public boolean test(final T candidate) {
		for (final Predicate<? super T> predicate : this.getPredicates()) {
			if (predicate.test(candidate)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UnilateralPredicate<?>)) {
			return false;
		}
		final UnilateralPredicate<?> other = (UnilateralPredicate<?>) obj;
		return this.getPredicates().equals(other.getPredicates());
	}

	@Override
	public int hashCode() {
		int result = 11;
		result = 31 * result + this.getPredicates().hashCode();
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		final List<Predicate<? super T>> predicates = this.getPredicates();
		for (int i = 0; i < predicates.size(); i++) {
			if (i > 0) {
				if (i < predicates.size() - 1) {
					builder.append(", ");
				} else {
					if (predicates.size() > 2) {
						builder.append(",");
					}
					builder.append(" or ");
				}
			}
			builder.append(predicates.get(i));
		}
		return builder.toString();
	}

}
