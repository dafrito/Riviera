package com.bluespot.logic.predicates;

/**
 * A {@link Predicate} that tests {@link Comparable} objects. This predicate
 * will evaluate to {@code true} if the tested value is strictly greater than
 * the provided constant value. Null and equal values evaluate to {@code false}.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the tested type
 */
public class LessThanPredicate<T extends Comparable<? super T>> implements Predicate<T> {

    private final T constant;

    /**
     * Constructs a new {@link LessThanPredicate} using the specified constant
     * as the reference.
     * 
     * @param constant
     *            the constant that is used in the evaluation of this predicate
     * @throws NullPointerException
     *             if {@code constant} is null
     */
    public LessThanPredicate(final T constant) {
        if (constant == null) {
            throw new NullPointerException("constant is null");
        }
        this.constant = constant;
    }

    /**
     * Returns the constant used by this predicate.
     * 
     * @return the constant used by this predicate
     */
    public T getConstant() {
        return this.constant;
    }

    @Override
    public boolean test(final T candidate) {
        if (candidate == null) {
            return false;
        }
        return candidate.compareTo(this.getConstant()) < 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LessThanPredicate<?>)) {
            return false;
        }
        final LessThanPredicate<?> other = (LessThanPredicate<?>) obj;
        if (!this.getConstant().equals(other.getConstant())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 13;
        result = 31 * result + this.getConstant().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("is less than %s", this.getConstant().toString());
    }
}
