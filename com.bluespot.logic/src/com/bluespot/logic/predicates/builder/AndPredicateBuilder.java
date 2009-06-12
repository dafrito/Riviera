package com.bluespot.logic.predicates.builder;

import java.util.ArrayList;
import java.util.List;

import com.bluespot.logic.Predicates;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.predicates.UnanimousPredicate;

/**
 * A builder that constructs predicates objects that evaluate to
 * <code>true</code> if and only if all child predicates are <code>true</code>.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of test value expected by this builder's predicate
 */
public final class AndPredicateBuilder<T> {

    private final List<Predicate<? super T>> predicates = new ArrayList<Predicate<? super T>>();

    /**
     * Adds the specified predicate to this builder. It must evaluate to {@code
     * true} for the built predicate to evaluate to {@code true}.
     * 
     * @param predicate
     *            the predicated to add
     * @return this builder
     */
    public AndPredicateBuilder<T> and(final Predicate<? super T> predicate) {
        this.predicates.add(predicate);
        return this;
    }

    /**
     * Returns whether this builder has any predicates that would be included in
     * a constructed {@link UnanimousPredicate} object.
     * 
     * @return {@code true} if this builder has any predicates, otherwise
     *         {@code false}
     * @see #build()
     */
    public boolean hasPredicates() {
        return !this.predicates.isEmpty();
    }

    /**
     * Constructs the predicate from the predicates in this builder. The builder
     * may optimize the created predicate, and as a consequence, a
     * {@link UnanimousPredicate} may not always returned.
     * 
     * @return a predicate representing this builder's predicates. It will
     *         evaluate to <code>true</code> if and only if all child predicates
     *         evaluate to {@code true}.
     */
    public Predicate<? super T> build() {
        if (this.predicates.isEmpty()) {
            return Predicates.truth();
        }
        if (this.predicates.size() == 1) {
            return this.predicates.get(0);
        }
        return new UnanimousPredicate<T>(this.predicates);
    }
}
