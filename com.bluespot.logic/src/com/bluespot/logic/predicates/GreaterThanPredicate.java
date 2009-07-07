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
public class GreaterThanPredicate<T extends Comparable<? super T>> implements Predicate<T> {

    private final T constant;

    /**
     * Constructs a new {@link GreaterThanPredicate} using the specified
     * constant as the reference.
     * 
     * @param constant
     *            the constant that is used in the evaluation of this predicate
     * @throws NullPointerException
     *             if {@code constant} is null
     */
    public GreaterThanPredicate(final T constant) {
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
    public boolean test(final T value) {
        if (value == null) {
            return false;
        }
        return value.compareTo(this.getConstant()) > 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GreaterThanPredicate<?>)) {
            return false;
        }
        final GreaterThanPredicate<?> other = (GreaterThanPredicate<?>) obj;
        if (!this.getConstant().equals(other.getConstant())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 11;
        result = 31 * result + this.getConstant().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("is greater than %s", this.getConstant().toString());
    }
}
