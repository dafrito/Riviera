package com.bluespot.logic.predicates;

/**
 * An interface that defines a predicate. Predicates represent only boolean
 * methods and should not affect any values in any way. Predicates should accept
 * as wide a range of values as possible; only in the most severe cases should
 * an exception be thrown instead of returning {@code false}. Null values should
 * generally evaluate to {@code false}.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value this predicate should expect
 */
public interface Predicate<T> {

    /**
     * Tests the specified value.
     * 
     * @param value
     *            the value to evaluate
     * @return some boolean value
     */
    public boolean test(final T value);
}
