package com.bluespot.logic.values;

import com.bluespot.logic.adapters.Adapter;

/**
 * A {@link Value} implementation that converts a specified source value to a
 * new value using the specified adapter. Functional programmers would recognize
 * this as a curried adapter. It's somewhat more flexible than that since the
 * source value itself represents an arbitrarily complex procedure, instead of
 * just a value.
 * <p>
 * Since this adapts a value to create essentially a new {@code Value} object,
 * these can be chained together. A builder will be written to further expedite
 * creation of these chained adapters.
 * <p>
 * TODO AdaptedValueBuilder or something of the sort to fulfill the above, if
 * necessary.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of the source {@link Value} object
 * @param <D>
 *            the type of the converted value
 */
public final class AdaptingValue<S, D> implements Value<D> {

    private final Value<? extends S> source;
    private final Adapter<? super S, ? extends D> adapter;

    /**
     * Constructs a {@link AdaptingValue} object using the specified
     * {@link Value} object as its source. It will be converted to the
     * destination type using the specified {@link Adapter}.
     * 
     * @param source
     *            the {@code Value} implementation used as the source. It may
     *            not be null.
     * @param adapter
     *            the {@code Adapter} implementation used as the converter. It
     *            may not be null.
     * @throws NullPointerException
     *             if either argument is null
     */
    public AdaptingValue(final Value<? extends S> source, final Adapter<? super S, ? extends D> adapter) {
        if (source == null) {
            throw new NullPointerException("source is null");
        }
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        this.source = source;
        this.adapter = adapter;
    }

    /**
     * Returns the {@link Value} that acts as the source value for this value.
     * 
     * @return the source value for this value object
     */
    public Value<? extends S> getSource() {
        return this.source;
    }

    /**
     * Returns the {@link Adapter} that performs conversion for this object.
     * 
     * @return the {@code Adapter} that converts the specified source value
     */
    public Adapter<? super S, ? extends D> getAdapter() {
        return this.adapter;
    }

    @Override
    public D get() {
        return this.getAdapter().adapt(this.getSource().get());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdaptingValue<?, ?>)) {
            return false;
        }
        final AdaptingValue<?, ?> other = (AdaptingValue<?, ?>) obj;
        if (!this.getSource().equals(other.getSource())) {
            return false;
        }
        if (!this.getAdapter().equals(other.getAdapter())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getSource().hashCode();
        result = 31 * result + this.getAdapter().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("AdaptedValue[source: %s, adapter: %s]", this.getSource(), this.getAdapter());
    }
}
