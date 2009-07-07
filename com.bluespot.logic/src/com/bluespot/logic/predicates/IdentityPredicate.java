package com.bluespot.logic.predicates;

import com.bluespot.logic.Predicates;

/**
 * A predicate that evaluates to {@code true} if and only if the tested value is
 * equal to the specified value.
 * <p>
 * Null values are not allowed.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of value used in evaluation
 */
public final class IdentityPredicate<T> implements Predicate<T> {

    private final T constant;

    /**
     * Constructs an identity predicate using the specified value.
     * 
     * @param constant
     *            the value used in evaluation. Only values identical to this
     *            value will evaluate to {@code true}.
     * @throws NullPointerException
     *             if {@code constant} is null. Use
     *             {@link Predicates#nullValue()} if you wish to compare with a
     *             null value.
     */
    public IdentityPredicate(final T constant) {
        if (constant == null) {
            throw new NullPointerException("constant is null");
        }
        this.constant = constant;
    }

    /**
     * Returns the constant value of this predicate.
     * 
     * @return the constant value of this predicate
     */
    public T getConstant() {
        return this.constant;
    }

    @Override
    public boolean test(final T value) {
        return this.constant == value;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getConstant().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("is exactly %s", this.getConstant());
    }

}
