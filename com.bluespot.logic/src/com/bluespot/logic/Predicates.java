package com.bluespot.logic;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bluespot.logic.predicates.EqualityPredicate;
import com.bluespot.logic.predicates.IdentityPredicate;
import com.bluespot.logic.predicates.InversePredicate;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.predicates.RegexPredicate;
import com.bluespot.logic.predicates.UnanimousPredicate;
import com.bluespot.logic.predicates.builder.PredicateBuilder;

/**
 * A set of pre-built predicates. Predicates are essentially poor man's function
 * objects.
 * 
 * @author Aaron Faanes
 */
public final class Predicates {

    private Predicates() {
        // Suppress default constructor to ensure non-instantiability
        throw new AssertionError();
    }

    /**
     * Returns a builder for composite predicates.
     * 
     * @param <T>
     *            the type of the tested value
     * @return a builder for composite predicates
     */
    public static <T> PredicateBuilder<T> group() {
        return new PredicateBuilder<T>();
    }

    /**
     * A predicate that tests for null values.
     * 
     * @see #nullValue()
     */
    private static final Predicate<Object> NULL = new Predicate<Object>() {

        public boolean test(final Object value) {
            return value == null;
        }

        @Override
        public String toString() {
            return "is null";
        }
    };

    /**
     * Returns a predicate that evaluates to {@code true} if and only if the
     * tested value is null.
     * 
     * @return a predicate that evaluates based on the nullity of the specified
     *         value
     */
    public static Predicate<Object> nullValue() {
        return Predicates.NULL;
    }

    /**
     * A predicate that tests for non-null values.
     * 
     * @see #notNullValue()
     */
    private static final Predicate<Object> NOT_NULL = new Predicate<Object>() {

        public boolean test(final Object value) {
            return value != null;
        }

        @Override
        public String toString() {
            return "is not null";
        }
    };

    /**
     * Returns a predicate that evaluates to {@code true} if and only if the
     * tested value is not null.
     * 
     * @return a predicate that evaluates based on the non-nullity of the
     *         specified value
     */
    public static Predicate<Object> notNullValue() {
        return Predicates.NOT_NULL;
    }

    /**
     * A predicate that always returns {@code true}.
     * 
     * @see #truth()
     */
    private static final Predicate<Object> TRUTH = new Predicate<Object>() {

        public boolean test(final Object value) {
            return true;
        }

        @Override
        public String toString() {
            return "is anything";
        }
    };

    /**
     * Returns a predicate that always evaluates to {@code true}.
     * 
     * @return a predicate that always evaluates to {@code true}
     */
    public static Predicate<Object> truth() {
        return Predicates.TRUTH;
    }

    /**
     * A predicate that always returns {@code false}.
     * 
     * @see #never()
     */
    private static final Predicate<Object> NEVER = new Predicate<Object>() {

        public boolean test(final Object value) {
            return false;
        }

        @Override
        public String toString() {
            return "is impossible";
        }
    };

    /**
     * Returns a predicate that always evaluates to {@code false}.
     * 
     * @return a predicate that always evaluates to {@code false}
     */
    public static Predicate<Object> never() {
        return Predicates.NEVER;
    }

    /**
     * Returns an {@link IdentityPredicate} that tests values for identity. It
     * will evaluate to {@code true} only for the specified value.
     * 
     * @param <T>
     *            the type of value used in the predicate
     * @param constant
     *            the one {@code true} value to this predicate
     * @return an identity predicate for the specified value
     * @throws NullPointerException
     *             if {@code constant} is null
     */
    public static <T> IdentityPredicate<T> exact(final T constant) {
        return new IdentityPredicate<T>(constant);
    }

    /**
     * Returns a {@link RegexPredicate} that tests values against the specified
     * value. Null values are automatically {@code false}.
     * <p>
     * This method merely defers to {@link Predicates#matches(Pattern)} after
     * compiling the specified pattern.
     * 
     * @param <T>
     *            the type of the tested value
     * @param regexPattern
     *            the pattern used as this predicate's test
     * @return a predicate using the specified pattern
     * @see Pattern#matcher(CharSequence)
     * @see Matcher#matches()
     */
    public static <T> RegexPredicate<T> matches(final String regexPattern) {
        return Predicates.matches(Pattern.compile(regexPattern));
    }

    /**
     * Returns a {@link RegexPredicate} that tests values against the specified
     * value. Null values are automatically {@code false}.
     * 
     * @param <T>
     *            the type of the tested value
     * @param pattern
     *            the pattern used as this predicate's test
     * @return a predicate using the specified pattern
     * @see Pattern#matcher(CharSequence)
     * @see Matcher#matches()
     */
    public static <T> RegexPredicate<T> matches(final Pattern pattern) {
        return new RegexPredicate<T>(pattern);
    }

    /**
     * Returns a {@link EqualityPredicate} that tests for equality with the
     * specified value.
     * 
     * @param <T>
     *            the type of the specified value
     * @param value
     *            the value used in the returned predicate
     * @return an equality predicate using the specified value
     * @throws NullPointerException
     *             if {@code value} is null
     */
    public static <T> EqualityPredicate<T> is(final T value) {
        return new EqualityPredicate<T>(value);
    }

    /**
     * Returns a predicate that is logically equivalent to the specified
     * predicate.
     * 
     * @param <T>
     *            the type of the specified predicate
     * @param predicate
     *            the predicate to use
     * @return an equal predicate
     */
    public static <T> Predicate<T> is(final Predicate<T> predicate) {
        return predicate;
    }

    /**
     * Returns an {@link InversePredicate} that is the inverse of the specified
     * predicate.
     * 
     * @param <T>
     *            the type of tested value
     * @param predicate
     *            the predicate to invert
     * @return a predicate that is the inverse of the specified predicate
     */
    public static <T> InversePredicate<T> not(final Predicate<T> predicate) {
        return new InversePredicate<T>(predicate);
    }

    /**
     * Returns an {@link InversePredicate} that is {@code true} if and only if
     * the tested value is not equal to the specified value.
     * 
     * @param <T>
     *            the type of the tested values
     * @param value
     *            the reference value
     * @return a predicate that tests for inequality
     * @throws NullPointerException
     *             if {@code value} is null
     */
    public static <T> InversePredicate<T> not(final T value) {
        return new InversePredicate<T>(Predicates.is(value));
    }

    /**
     * Creates a {@link UnanimousPredicate} that evaluates to {@code true} if
     * and only if all specified predicates evaluate to {@code true}.
     * 
     * @param <T>
     *            the type of value common to all predicates
     * @param predicates
     *            the child predicates
     * @return a unanimous predicate
     */
    public static <T> UnanimousPredicate<T> all(final Predicate<? super T>[] predicates) {
        return new UnanimousPredicate<T>(Arrays.asList(predicates));
    }

    /**
     * A predicate that tests for lower-case strings.
     * 
     * @see #isLowerCase()
     */
    private static final Predicate<String> LOWER_CASE = new Predicate<String>() {
        public boolean test(final String value) {
            if (value == null) {
                return false;
            }
            return value.toLowerCase().equals(value);
        }
    };

    /**
     * Returns a new {@link Predicate} that tests whether a string is
     * lower-case. Specifically, the predicate tests whether the lower-cased
     * version of a given string is equal to the original version. As a
     * consequence, strings that contain characters that cannot be upper or
     * lower-case will implicitly evaluate to {@code true}. This includes
     * numbers, the empty string {@code ""}, whitespace, and all other special
     * characters. Null values, however, will evaluate to {@code false}.
     * <p>
     * Since this method relies on {@link String#toLowerCase()}, results will be
     * locale-dependent.
     * 
     * @return a new {@code Predicate} that tests whether a string is lower-case
     * @see #isUpperCase()
     */
    public static Predicate<String> isLowerCase() {
        return Predicates.LOWER_CASE;
    }

    /**
     * A predicate that tests for upper-case strings.
     * 
     * @see #isLowerCase()
     */
    private static final Predicate<String> UPPER_CASE = new Predicate<String>() {
        public boolean test(final String value) {
            if (value == null) {
                return false;
            }
            return value.toUpperCase().equals(value);
        }
    };

    /**
     * Returns a new {@link Predicate} that tests whether a string is
     * lower-case. Specifically, the predicate tests whether the upper-cased
     * version of a given string is equal to the original version. As a
     * consequence, strings that contain characters that cannot be upper or
     * lower-case will implicitly evaluate to {@code true}. This includes
     * numbers, the empty string {@code ""}, whitespace, and all other special
     * characters. Null values, however, will evaluate to {@code false}.
     * <p>
     * Since this method relies on {@link String#toLowerCase()}, results will be
     * locale-dependent.
     * 
     * @return a new {@code Predicate} that tests whether a string is lower-case
     * @see #isLowerCase()
     */
    public static Predicate<String> isUpperCase() {
        return Predicates.UPPER_CASE;
    }

}
