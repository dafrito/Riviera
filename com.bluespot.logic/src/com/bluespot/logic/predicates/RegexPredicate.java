package com.bluespot.logic.predicates;

import java.util.regex.Pattern;

/**
 * Creates a predicate that evaluates the string version of some value. It will
 * return {@code true} if and only if the specified pattern matches its string
 * value.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the value tested in this predicate
 */
public final class RegexPredicate<T> implements Predicate<T> {

    private final Pattern pattern;

    /**
     * Constructs a predicate using the specified pattern
     * 
     * @param pattern
     *            the pattern used in this predicate
     * @throws NullPointerException
     *             if {@code pattern} is null
     */
    public RegexPredicate(final Pattern pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern is null");
        }
        this.pattern = pattern;
    }

    /**
     * Returns the regular expression used in this predicate.
     * 
     * @return the regular expression used in this predicate
     */
    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public boolean test(final T value) {
        if (value == null) {
            return false;
        }
        return this.getPattern().matcher(value.toString()).matches();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RegexPredicate<?>)) {
            return false;
        }
        final RegexPredicate<?> other = (RegexPredicate<?>) obj;
        return this.getPattern().equals(other.getPattern());
    }

    @Override
    public int hashCode() {
        int result = 13;
        result = 31 * result + this.getPattern().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("matches '%s'", this.getPattern());
    }

}
