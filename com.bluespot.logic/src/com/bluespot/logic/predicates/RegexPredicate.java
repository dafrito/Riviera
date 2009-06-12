package com.bluespot.logic.predicates;

import java.util.regex.Pattern;

/**
 * Creates a predicate that evaluates the string version of some value. It will
 * return {@code true} if and only if the specified pattern matches its string
 * value.
 * <p>
 * The string value is determined by {@link #getString(Object)}. Subclasses may
 * override this method if they wish to specify a different method of obtaining
 * a string value.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the value tested in this predicate
 */
public class RegexPredicate<T> implements Predicate<T> {

    private final Pattern pattern;

    /**
     * Constructs a predicate using the specified pattern
     * 
     * @param pattern
     *            the pattern used in this predicate
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

    /**
     * Returns the string version of the specified non-null value.
     * <p>
     * By default, this calls {@link Object#toString()}.
     * 
     * @param value
     *            the value used to generate the string
     * @return the string value of the specified value
     */
    protected String getString(final T value) {
        return value.toString();
    }

    @Override
    public boolean test(final T value) {
        if (value == null) {
            return false;
        }
        return this.getPattern().matcher(this.getString(value)).matches();
    }

}
