package com.bluespot.logic.adapters;

/**
 * An {@link ClassCastException} that occurs when a {@link CastingAdapter} fails
 * to perform a cast on a given value. This exception describes that failure.
 * 
 * @author Aaron Faanes
 * 
 */
public final class CastingAdapterException extends ClassCastException {

    private static final long serialVersionUID = 3287225814118519929L;

    private final CastingAdapter<?, ?> adapter;
    private final Object value;

    /**
     * Constructs a {@link CastingAdapterException}. The exception uses the
     * 
     * @param adapter
     *            the adapter that was the source of this exception
     * @param value
     *            the value that failed to cast
     * @throws NullPointerException
     *             if either argument is null
     * @throws IllegalArgumentException
     *             if the value is actually valid for the specified adapter; if
     *             the cast is successful.
     */
    CastingAdapterException(final CastingAdapter<?, ?> adapter, final Object value) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (adapter.getCastType().isInstance(value)) {
            throw new IllegalArgumentException("Value is allowed to cast");
        }
        this.adapter = adapter;
        this.value = value;

    }

    @Override
    public String getMessage() {
        return String.format("Value '%s' cannot be cast to type '%s'", this.getCastType(), this.getValue());
    }

    /**
     * Returns the value that failed to cast to the destination type.
     * 
     * @return the value that failed to cast
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Returns the type that was the attempted destination type for the
     * conversion.
     * <p>
     * This is merely a convenience method for
     * {@link CastingAdapter#getCastType() this.getAdapter().getCastType()}.
     * 
     * @return the type that was the attempted destination type for the
     *         conversion
     */
    public Class<?> getCastType() {
        return this.getAdapter().getCastType();
    }

    /**
     * Returns the {@link CastingAdapter} that was the source of this exception.
     * 
     * @return the {@code CastingAdapter} that was the source of this exception
     */
    public CastingAdapter<?, ?> getAdapter() {
        return this.adapter;
    }

}