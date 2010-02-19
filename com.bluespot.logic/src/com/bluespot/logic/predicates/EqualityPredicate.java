package com.bluespot.logic.predicates;

import com.bluespot.logic.Predicates;

/**
 * A predicate that tests for equality against a provided constant. The constant
 * is not allowed to be null. This predicate essentially wraps
 * {@link Object#equals(Object)}.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value to test
 */
public final class EqualityPredicate<T> implements Predicate<T> {

    private final T constant;

    /**
     * Constructs a new {@link EqualityPredicate} using the specified constant.
     * 
     * @param constant
     *            the constant value that determines the result of this
     *            predicate's evaluation
     * @throws NullPointerException
     *             if the specified constant is {@code null}. Use
     *             {@link Predicates#nullValue()} if you need to test for null
     *             values.
     */
    public EqualityPredicate(final T constant) {
        if (constant == null) {
            throw new NullPointerException("constant is null");
        }
        this.constant = constant;
    }

    /**
     * Returns the constant used by this predicate.
     * 
     * @return the constant used by this predicate during evaluation
     */
    public T getConstant() {
        return this.constant;
    }

    @Override
    public boolean test(final T candidate) {
        return this.constant.equals(candidate);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EqualityPredicate<?>)) {
            return false;
        }
        final EqualityPredicate<?> predicate = (EqualityPredicate<?>) obj;
        if (!this.getConstant().equals(predicate.getConstant())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getConstant().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("is %s", this.getConstant());
    }
}
