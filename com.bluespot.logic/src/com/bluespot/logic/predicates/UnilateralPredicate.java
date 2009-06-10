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
public class UnilateralPredicate<T> extends CompositePredicate<T> {

	/**
	 * Constructs a unilateral predicate using the specified predicates.
	 * 
	 * @param predicates
	 *            the predicates used in this predicate
	 */
	public UnilateralPredicate(final Collection<Predicate<? super T>> predicates) {
		super(predicates);
	}

	public boolean test(final T value) {
		for (final Predicate<? super T> predicate : this.getPredicates()) {
			if (predicate.test(value)) {
				return true;
			}
		}
		return false;
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
