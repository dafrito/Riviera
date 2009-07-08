package com.bluespot.logic.values;

/**
 * A {@link Value} implementation that simply returns the value given at
 * construction. This is the simplest of {@code Value} implementations.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of the constant value
 */
public class ConstantValue<T> implements Value<T> {

    private final T value;

    /**
     * Constructs a new {@link ConstantValue} that uses the specified constant.
     * 
     * @param constant
     *            the constant used in this {@link ConstantValue}. It does not
     *            have to be immutable, but should implement
     *            {@link #equals(Object)} appropriately. Null values are not
     *            allowed.
     * @throws NullPointerException
     *             if {@code constant} is null
     */
    public ConstantValue(final T constant) {
        if (constant == null) {
            throw new NullPointerException("value is null");
        }
        this.value = constant;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConstantValue<?>)) {
            return false;
        }
        final ConstantValue<?> other = (ConstantValue<?>) obj;
        if (!this.get().equals(other.get())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.get().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("ConstantValue[%s]", this.get().toString());
    }

}
