package com.bluespot.logic.predicates.builder;

import java.util.ArrayList;
import java.util.List;

import com.bluespot.logic.Predicates;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.predicates.UnilateralPredicate;

/**
 * A builder that constructs a predicate that evaluates to <code>true</code> if
 * any of its child predicates evaluate to <code>true</code>.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of test value used in the constructed and child
 *            predicates
 */
public class OrPredicateBuilder<T> {

	private final List<Predicate<? super T>> predicates = new ArrayList<Predicate<? super T>>();

	/**
	 * Adds the specified predicate to this builder. If it evaluates to
	 * <code>true</code>, then the constructed predicate will evaluate to
	 * <code>true</code>.
	 * 
	 * @param predicate
	 *            the predicate to add
	 * @return this builder
	 */
	public OrPredicateBuilder<T> or(final Predicate<? super T> predicate) {
		this.predicates.add(predicate);
		return this;
	}

	/**
	 * Constructs the predicate from the predicates in this builder. The builder
	 * may optimize the created predicate, and as a consequence, a
	 * {@link UnilateralPredicate} may not always returned.
	 * 
	 * @return a predicate representing this builder's predicates. The
	 *         constructed predicate will evaluate to <code>true</code> if any
	 *         of its child predicates evaluate to <code>true</code>.
	 */
	public Predicate<? super T> build() {
		if (this.predicates.isEmpty()) {
			return Predicates.never();
		}
		if (this.predicates.size() == 1) {
			return this.predicates.get(0);
		}
		return new UnilateralPredicate<T>(this.predicates);
	}

}
