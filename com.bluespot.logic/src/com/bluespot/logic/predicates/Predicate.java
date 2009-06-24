package com.bluespot.logic.predicates;

import com.bluespot.logic.Predicates;
import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.predicates.builder.PredicateBuilder;

/**
 * Represents a boolean expression. Predicates evaluate some value and return
 * some value based on implementation-specific conditions. Predicates are
 * defined by a strict contract. Specifically:
 * <ul>
 * <li><em>Predicates are immutable.</em> Once created, predicates do not
 * change. Many classes depend on the immutability of predicates to function
 * properly. This means that all predicates only have one state. An example of a
 * violating predicate would be one that provides a {@code switch()} method that
 * inverts its result.
 * <li><em>Predicates evaluate consistently.</em> Predicates test values
 * independent of the context or ordering of the values. If a predicate
 * evaluates to {@code true} for some value, it will always evaluate to {@code
 * true} for that value. A predicate that tests whether some date is in the
 * future violates this principle because the current date varies, and the
 * predicate could not be guaranteed to return consistent results.
 * <p>
 * Since tests are independent, predicates cannot test whether any specified
 * value is equal to the first value. The predicate's state must be completely
 * finalized before any tests are made.
 * <li><em>Predicates respect {@link #equals(Object)}.</em> Predicates always
 * provide a reliable {@code equals(Object)} implementation, and respect and
 * depend on reliable {@code equals(Object)} implementations of their tested
 * values. This means that equal predicates evaluate equally for all values.
 * This also means that a predicate that evaluated to {@code true} for some
 * object will evaluate to {@code true} for all objects that are equal to that
 * object.
 * <p>
 * The only exception to this rule would be predicates that explicitly test for
 * identity. As a general rule, the set of {@code true} values for an
 * identity-based predicate must be a strict subset of a equality-based
 * predicate.
 * <li><em>Predicates evaluate the given value.</em> Predicates are independent
 * of their context. Their only input is any values from their constructor and
 * the tested value. They must evaluate against the specified value, and not the
 * context of the value. An example would be a predicate that tests whether
 * we're on the EDT. While this predicate is consistent with the current thread,
 * it is not consistent with the tested value.
 * <li><em>Predicates produce no side-effects.</em> Predicates can be used an
 * arbitrary amount of time on an arbitrary number of times in any order, and do
 * not change the state of their tested values or themselves. The only exception
 * to this rule is if a predicate is producing logging or testing information.
 * <li><em>Predicates are robust.</em> Predicates accept as many values as is
 * possible to be evaluated. They should throw exceptions only in cases where
 * some external failure has occurred, rather than a failure with the tested
 * value. If some value violates some implicit conditions of a predicate, that
 * value evaluates to {@code false}. A typical case is that a value must be
 * non-null; the null value typically evaluates to {@code false} since
 * non-nullity is an implicit condition of most predicates.
 * <p>
 * On the other hand, predicates must throw exceptions if they depend on
 * unreliable systems and those systems fail during testing. This is because the
 * test cannot be completed due to a condition external to the state of the
 * predicate and the tested value.
 * <li><em>Predicates are atomic.</em> Predicates should fully test for only one
 * condition. A predicate should not normally test for multiple conditions since
 * doing so would lead to ambiguous results. Predicates should test for one
 * whole condition. However, implicit conditions are a necessary part of testing
 * for the condition, and failing an implicit condition implies a failure of the
 * final condition.
 * <p>
 * In practice, it would be ideal to not have implicit conditions, and
 * well-designed predicates minimize these dependencies. Most simple predicates
 * only have the implicit condition of the target value being non-null.
 * </ul>
 * Predicates can be constructed using a {@link PredicateBuilder}. This allows
 * you to construct predicates in a readable, domain-specific way. These
 * builders also provide more traditional imperative methods if you're not
 * interesting in working with the DSL.
 * <p>
 * Many common predicates have already been written and factory methods for them
 * reside in {@link Predicates}. Using these in conjunction with builders makes
 * creating simple predicates easy and complex predicates possible.
 * <p>
 * Predicates are, in their crudest form, an {@link Adapter} implementations
 * that convert some arbitrary value to a {@code Boolean}. I opted against
 * realizing this in the code, however, because it does not make sense
 * semantically. Predicates are responsible for testing values, whereas adapters
 * are responsible for converting values. These are separate responsibilities,
 * and I don't believe that testing is a special case of conversion
 * semantically, so I didn't extend {@code Adapter}.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value this predicate should expect
 */
public interface Predicate<T> {

    /**
     * Tests the specified value. This method should accept the widest range of
     * values as is appropriate. Specifically, null and other degenerate values
     * should be expected cases for this method.
     * <p>
     * Unless otherwise noted, values that are equal should cause this predicate
     * to evaluate in an equal manner. This method should also evaluate
     * consistently given consistent inputs. This intentionally resembles the
     * requirements for a well-written {@link Object#equals(Object)}
     * implementation.
     * 
     * @param value
     *            the value to evaluate
     * @return some boolean value that is a response to the specified value
     */
    public boolean test(final T value);
}
