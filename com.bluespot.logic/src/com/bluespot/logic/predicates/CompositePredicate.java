package com.bluespot.logic.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A composite of predicates. The behavior with this predicates is defined in
 * subclasses.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value tested by this predicate
 */
public abstract class CompositePredicate<T> implements Predicate<T> {

    /**
     * The list of predicates used with this composite
     */
    private final List<Predicate<? super T>> predicates;

    /**
     * Creates a composite predicate from the specified predicates.
     * 
     * @param predicates
     *            the predicates used in this object
     * @throws IllegalArgumentException
     *             if predicates contains no elements. Degenerate composites do
     *             not have a predictable return type, and thus are not allowed.
     * @throws NullPointerException
     *             if {@code predicates} or any predicate in {@code predicates}
     *             is null
     */
    public CompositePredicate(final Collection<Predicate<? super T>> predicates) {
        if (predicates == null) {
            throw new NullPointerException("predicates is null");
        }
        if (predicates.isEmpty()) {
            throw new IllegalArgumentException("predicates is empty");
        }
        this.predicates = new ArrayList<Predicate<? super T>>(predicates);
        for (final Predicate<? super T> predicate : this.predicates) {
            if (predicate == null) {
                throw new NullPointerException("predicates contains null values");
            }
        }
    }

    /**
     * Gets the array of predicates used in this predicate.
     * 
     * @return the array of predicates used in this predicate
     */
    public List<Predicate<? super T>> getPredicates() {
        return Collections.unmodifiableList(this.predicates);
    }

}
