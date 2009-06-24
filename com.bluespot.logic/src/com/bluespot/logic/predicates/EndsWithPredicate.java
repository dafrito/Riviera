package com.bluespot.logic.predicates;

import com.bluespot.logic.Adapters;
import com.bluespot.logic.adapters.Adapter;

/**
 * Creates a predicate that evaluates the string version of some value. It will
 * return {@code true} if and only if the tested string ends with the
 * predicate's specified ending string.
 * <p>
 * Values will be converted to strings using the specified adapter. By default,
 * {@link Adapters#stringValue()} is used.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the value tested in this predicate
 * @see StartsWithPredicate
 */
public final class EndsWithPredicate<T> implements Predicate<T> {

    private final String endingString;
    private final Adapter<? super T, String> adapter;

    /**
     * Constructs a predicate using the specified string. The predicate will
     * evaluate to {@code true} if and only if the tested string ends with the
     * specified string value. Values are converted to string using
     * {@link Adapters#stringValue()}.
     * 
     * @param endingString
     *            the value that tested strings must end with
     * @throws NullPointerException
     *             if {@code endingString} is null
     */
    public EndsWithPredicate(final String endingString) {
        this(endingString, Adapters.stringValue());
    }

    /**
     * Constructs a predicate using the specified string. Tested values are
     * converted to strings using the specified adapter. The predicate will
     * evaluate to {@code true} if and only if the tested string ends with the
     * specified string value.
     * 
     * @param endingString
     *            the value that tested strings must end with
     * @param adapter
     *            the adapter used to convert tested values to strings
     * @throws NullPointerException
     *             if either argument is null
     */
    public EndsWithPredicate(final String endingString, final Adapter<? super T, String> adapter) {
        if (endingString == null) {
            throw new NullPointerException("endingString is null");
        }
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        this.endingString = endingString;
        this.adapter = adapter;
    }

    /**
     * Returns the ending string used in this predicate.
     * 
     * @return the ending string used in this predicate
     */
    public String getEndingString() {
        return this.endingString;
    }

    /**
     * Returns the adapter used to convert tested values to strings.
     * 
     * @return the adapter used to convert tested values to strings
     */
    public Adapter<? super T, String> getAdapter() {
        return this.adapter;
    }

    @Override
    public boolean test(final T value) {
        final String stringValue = this.getAdapter().adapt(value);
        if (stringValue == null) {
            return false;
        }
        return stringValue.endsWith(this.getEndingString());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EndsWithPredicate<?>)) {
            return false;
        }
        final EndsWithPredicate<?> other = (EndsWithPredicate<?>) obj;
        if (!this.getEndingString().equals(other.getEndingString())) {
            return false;
        }
        if (!this.getAdapter().equals(other.getAdapter())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 19;
        result = 31 * result + this.getEndingString().hashCode();
        result = 31 * result + this.getAdapter().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s that ends with '%s'", this.getAdapter(), this.getEndingString());
    }

}
