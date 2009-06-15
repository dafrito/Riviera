package com.bluespot.logic.predicates;

import com.bluespot.logic.Adapters;
import com.bluespot.logic.adapters.Adapter;

/**
 * Creates a predicate that evaluates the string version of some value. It will
 * return {@code true} if and only if the tested string starts with the
 * predicate's specified starting string.
 * <p>
 * Values will be converted to strings using the specified adapter. By default,
 * {@link Adapters#stringValue()} is used.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the value tested in this predicate
 * @see EndsWithPredicate
 */
public class StartsWithPredicate<T> implements Predicate<T> {

    private final String startingString;
    private final Adapter<? super T, String> adapter;

    /**
     * Constructs a predicate using the specified string. Tested values are
     * converted to string using {@link Adapters#stringValue()}. The predicate
     * will evaluate to {@code true} if and only if the tested object has a
     * string value that starts with the specified string value.
     * 
     * @param startingString
     *            the value that tested strings must end with
     * @throws NullPointerException
     *             if {@code startingString} is null
     */
    public StartsWithPredicate(final String startingString) {
        this(startingString, Adapters.stringValue());
    }

    /**
     * Constructs a predicate using the specified string. Tested values are
     * converted to strings using the specified adapter. The predicate will
     * evaluate to {@code true} if and only if the tested object has a string
     * value that starts with the specified string value.
     * 
     * @param startingString
     *            the value that tested strings must end with
     * @param adapter
     *            the adapter used to convert tested values to strings
     * @throws NullPointerException
     *             if either argument is null
     */
    public StartsWithPredicate(final String startingString, final Adapter<? super T, String> adapter) {
        if (startingString == null) {
            throw new NullPointerException("startingString is null");
        }
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        this.startingString = startingString;
        this.adapter = adapter;
    }

    /**
     * Returns the starting string used in this predicate.
     * 
     * @return the starting string used in this predicate
     */
    public String getStartingString() {
        return this.startingString;
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
        return stringValue.startsWith(this.getStartingString());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StartsWithPredicate<?>)) {
            return false;
        }
        final StartsWithPredicate<?> other = (StartsWithPredicate<?>) obj;
        if (!this.getStartingString().equals(other.getStartingString())) {
            return false;
        }
        if (!this.getAdapter().equals(other.getAdapter())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 13;
        result = 31 * result + this.getStartingString().hashCode();
        result = 31 * result + this.getAdapter().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s that starts with \"%s\"", this.getAdapter(), this.getStartingString());
    }

}