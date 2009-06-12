package com.bluespot.logic.predicates;

import java.util.Collection;
import java.util.List;

/**
 * Creates a predicate that evaluates to {@code true} only when all child
 * predicates evaluate to {@code true}.
 * <p>
 * If no predicates are provided, {@code true} is returned.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value evaluated by this predicate
 */
public final class UnanimousPredicate<T> extends CompositePredicate<T> {

    /**
     * Constructs a predicate that uses the specified child predicates
     * 
     * @param predicates
     *            the collection of predicates used in evaluation
     * @throws NullPointerException
     *             if {@code predicates} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code predicates} is empty
     */
    public UnanimousPredicate(final Collection<Predicate<? super T>> predicates) {
        super(predicates);
    }

    @Override
    public boolean test(final T value) {
        for (final Predicate<? super T> predicate : this.getPredicates()) {
            if (!predicate.test(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getPredicates().hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UnanimousPredicate<?>)) {
            return false;
        }
        final UnanimousPredicate<?> other = (UnanimousPredicate<?>) obj;
        return this.getPredicates().equals(other.getPredicates());
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
                    builder.append(" and ");
                }
            }
            builder.append(predicates.get(i));
        }
        return builder.toString();
    }
}
