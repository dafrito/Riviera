package com.bluespot.logic.predicates;

/**
 * Creates a predicate that evaluates {@link String} values. It will return
 * {@code true} for a given string if, and only if, the string ends with the
 * predicate's specified starting string.
 * 
 * @author Aaron Faanes
 * 
 * @see StartsWithPredicate
 */
public final class EndsWithPredicate implements Predicate<String> {

    private final String endingString;

    /**
     * Constructs a {@link EndsWithPredicate} using the specified string.
     * 
     * @param endingString
     *            the value that tested strings must end with
     * @throws NullPointerException
     *             if {@code endingString} is null
     */
    public EndsWithPredicate(final String endingString) {
        if (endingString == null) {
            throw new NullPointerException("endingString is null");
        }
        this.endingString = endingString;
    }

    /**
     * Returns the ending string used in this predicate.
     * 
     * @return the ending string used in this predicate
     */
    public String getEndingString() {
        return this.endingString;
    }

    @Override
    public boolean test(final String value) {
        if (value == null) {
            return false;
        }
        return value.endsWith(this.getEndingString());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EndsWithPredicate)) {
            return false;
        }
        final EndsWithPredicate other = (EndsWithPredicate) obj;
        if (!this.getEndingString().equals(other.getEndingString())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 19;
        result = 31 * result + this.getEndingString().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("ends with '%s'", this.getEndingString());
    }

}
