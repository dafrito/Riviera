package com.bluespot.logic.predicates;

/**
 * Creates a predicate that evaluates {@link String} values. It will return
 * {@code true} for a given string if, and only if, the string starts with the
 * predicate's specified starting string.
 * 
 * @author Aaron Faanes
 * 
 * @see EndsWithPredicate
 */
public final class StartsWithPredicate implements Predicate<String> {

    private final String startingString;

    /**
     * Constructs a {@link StartsWithPredicate} using the specified string.
     * 
     * @param startingString
     *            the value that tested strings must start with
     * @throws NullPointerException
     *             if {@code startingString} is null
     */
    public StartsWithPredicate(final String startingString) {
        if (startingString == null) {
            throw new NullPointerException("startingString is null");
        }
        this.startingString = startingString;
    }

    /**
     * Returns the starting string used in this predicate.
     * 
     * @return the starting string used in this predicate
     */
    public String getStartingString() {
        return this.startingString;
    }

    @Override
    public boolean test(final String candidate) {
        if (candidate == null) {
            return false;
        }
        return candidate.startsWith(this.getStartingString());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StartsWithPredicate)) {
            return false;
        }
        final StartsWithPredicate other = (StartsWithPredicate) obj;
        if (!this.getStartingString().equals(other.getStartingString())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 13;
        result = 31 * result + this.getStartingString().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("starts with \"%s\"", this.getStartingString());
    }

}