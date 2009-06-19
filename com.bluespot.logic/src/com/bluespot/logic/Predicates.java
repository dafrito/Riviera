package com.bluespot.logic;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bluespot.logic.predicates.EndsWithPredicate;
import com.bluespot.logic.predicates.EqualityPredicate;
import com.bluespot.logic.predicates.IdentityPredicate;
import com.bluespot.logic.predicates.InversePredicate;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.predicates.RegexPredicate;
import com.bluespot.logic.predicates.StartsWithPredicate;
import com.bluespot.logic.predicates.UnanimousPredicate;
import com.bluespot.logic.predicates.builder.PredicateBuilder;

/**
 * A set of factory methods for semantically constructing {@link Predicate}
 * objects.
 * 
 * @author Aaron Faanes
 * @see Predicate
 */
public final class Predicates {

    private Predicates() {
        // Suppress default constructor to ensure non-instantiability
        throw new AssertionError("Instantiation not allowed");
    }

    /**
     * A predicate that tests whether a given file exists and is a directory.
     * 
     * @see #isDirectory()
     */
    private static final Predicate<File> PREDICATE_IS_DIRECTORY = new Predicate<File>() {

        public boolean test(final File value) {
            if (value == null) {
                return false;
            }
            return value.isDirectory();
        }

        @Override
        public String toString() {
            return "is directory";
        }
    };

    /**
     * Returns a predicate that evaluates to {@code true} if and only if the
     * given {@link File} exists and is a directory. Null values evaluate to
     * {@code false}.
     * 
     * @return a predicate that tests whether a given {@code File} is a
     *         directory
     * @see File#isDirectory()
     */
    public static Predicate<File> isDirectory() {
        return PREDICATE_IS_DIRECTORY;
    }

    /**
     * A predicate that tests whether a given file exists and is a file
     * 
     * @see #isFile()
     */
    private static final Predicate<File> PREDICATE_IS_FILE = new Predicate<File>() {

        public boolean test(final File value) {
            if (value == null) {
                return false;
            }
            return value.isFile();
        }

        @Override
        public String toString() {
            return "is file";
        }
    };

    /**
     * Returns a predicate that evaluates to {@code true} if and only if the
     * given {@link File} exists and is a file. Null values evaluate to {@code
     * false}.
     * 
     * @return a predicate that tests whether a given {@code File} is a file
     * @see File#isFile()
     */
    public static Predicate<File> isFile() {
        return PREDICATE_IS_FILE;
    }

    /**
     * Returns a new {@link PredicateBuilder} for composite predicates.
     * 
     * @param <T>
     *            the type of the tested value
     * @return a builder for composite predicates
     * @see PredicateBuilder
     */
    public static <T> PredicateBuilder<T> group() {
        return new PredicateBuilder<T>();
    }

    /**
     * Returns a new {@link PredicateBuilder} for composite predicates.
     * 
     * @param <T>
     *            the type of the tested value
     * @return a builder for composite predicates
     * @see PredicateBuilder
     */
    public static <T> PredicateBuilder<T> value() {
        return new PredicateBuilder<T>();
    }

    /**
     * A predicate that tests for null values.
     * 
     * @see #nullValue()
     */
    private static final Predicate<Object> PREDICATE_NULL = new Predicate<Object>() {

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
        return Predicates.PREDICATE_NULL;
    }

    /**
     * A predicate that tests for non-null values.
     * 
     * @see #notNullValue()
     */
    private static final Predicate<Object> PREDICATE_NOT_NULL = new Predicate<Object>() {

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
        return Predicates.PREDICATE_NOT_NULL;
    }

    /**
     * A predicate that always returns {@code true}.
     * 
     * @see #truth()
     */
    private static final Predicate<Object> PREDICATE_TRUTH = new Predicate<Object>() {

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
        return Predicates.PREDICATE_TRUTH;
    }

    /**
     * A predicate that always returns {@code false}.
     * 
     * @see #never()
     */
    private static final Predicate<Object> PREDICATE_NEVER = new Predicate<Object>() {

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
        return Predicates.PREDICATE_NEVER;
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
     * Creates a {@link RegexPredicate} that evaluates to {@code true} if and
     * only if the tested string value starts with the specified string.
     * 
     * @param <T>
     *            the tested value. Tested values will be converted to strings
     *            using {@link Object#toString()}
     * @param startingValue
     *            the value that the tested string should start with
     * @return a predicate that tests for the specified string
     * @see #endsWith(String)
     */
    public static <T> Predicate<T> startsWith(final String startingValue) {
        return new StartsWithPredicate<T>(startingValue);
    }

    /**
     * Creates a {@link RegexPredicate} that evaluates to {@code true} if and
     * only if the tested string value ends with the specified string.
     * 
     * @param <T>
     *            the tested value. Tested values will be converted to strings
     *            using {@link Object#toString()}
     * @param endingValue
     *            the value that the tested string should end with
     * @return a regex predicate that tests for the specified string
     * @see #startsWith(String)
     */
    public static <T> Predicate<T> endsWith(final String endingValue) {
        return new EndsWithPredicate<T>(endingValue);
    }

    /**
     * A predicate that tests for lower-case strings.
     * 
     * @see #isLowerCase()
     */
    private static final Predicate<String> PREDICATE_LOWER_CASE = new Predicate<String>() {
        public boolean test(final String value) {
            if (value == null) {
                return false;
            }
            return value.toLowerCase().equals(value);
        }

        @Override
        public String toString() {
            return "is lower case";
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
     * @see #upperCase()
     */
    public static Predicate<String> lowerCase() {
        return Predicates.PREDICATE_LOWER_CASE;
    }

    /**
     * A predicate that tests for upper-case strings.
     * 
     * @see #isLowerCase()
     */
    private static final Predicate<String> PREDICATE_UPPER_CASE = new Predicate<String>() {
        public boolean test(final String value) {
            if (value == null) {
                return false;
            }
            return value.toUpperCase().equals(value);
        }

        @Override
        public String toString() {
            return "is upper case";
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
     * @see #lowerCase()
     */
    public static Predicate<String> upperCase() {
        return Predicates.PREDICATE_UPPER_CASE;
    }

    /**
     * A predicate that tests whether a given file exists.
     * 
     * @see #fileExists()
     */
    private static final Predicate<File> PREDICATE_FILE_EXISTS = new Predicate<File>() {
        public boolean test(final File value) {
            if (value == null) {
                return false;
            }
            return value.exists();
        }

        @Override
        public String toString() {
            return "exists";
        }
    };

    /**
     * Returns a predicate that tests whether a given file exists. The returned
     * predicate will evaluate to {@code true} if and only if
     * {@link File#exists()} returns {@code true} for the given file. Null
     * values always evaluate to {@code false}.
     * 
     * @return a predicate that tests whether a given file exists using
     *         {@link File#exists()}
     */
    public static Predicate<File> fileExists() {
        return PREDICATE_FILE_EXISTS;
    }

}
