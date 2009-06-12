package com.bluespot.logic.predicates;

/**
 * A predicate that inverts a provided predicate.
 * <p>
 * This predicate does not perform different when passed a null value.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value of the inverted predicate
 */
public final class InversePredicate<T> implements Predicate<T> {

    private final Predicate<? super T> predicate;

    /**
     * Constructs a predicate that will return the inverse of the specified
     * predicate.
     * 
     * @param predicate
     *            the predicate to invert
     */
    public InversePredicate(final Predicate<? super T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        this.predicate = predicate;
    }

    /**
     * Returns the predicate that this object will invert
     * 
     * @return the predicate that this object will invert
     */
    public Predicate<? super T> getPredicate() {
        return this.predicate;
    }

    @Override
    public boolean test(final T value) {
        return !this.getPredicate().test(value);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.predicate.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof InversePredicate<?>)) {
            return false;
        }
        final InversePredicate<?> other = (InversePredicate<?>) obj;
        return this.getPredicate().equals(other.getPredicate());
    }

    @Override
    public String toString() {
        return String.format("not(%s)", this.getPredicate());
    }
}
