package com.bluespot.logic.adapters;

/**
 * An {@link HandledAdapter} that safely performs a cast. If the cast is not
 * allowed, then a {@link CastingAdapterException} is dispatched to the
 * associated handler.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the source type
 * @param <D>
 *            the destination type. This must be a subtype of {@code S}.
 */
public class CastingAdapter<S, D extends S> extends AbstractHandledAdapter<S, D, CastingAdapterException> {

    private final Class<D> castType;
    private final Class<S> sourceType;

    /**
     * Constructs a {@link CastingAdapter} that converts to the specified type.
     * 
     * @param sourceType
     *            the type from which given values are cast. It may not be null.
     * 
     * @param castType
     *            the type to which given values are cast. It may not be null.
     */
    public CastingAdapter(final Class<S> sourceType, final Class<D> castType) {
        if (sourceType == null) {
            throw new NullPointerException("sourceType is null");
        }
        if (castType == null) {
            throw new NullPointerException("castType is null");
        }
        this.sourceType = sourceType;
        this.castType = castType;
    }

    /**
     * Returns the source type of this adapter.
     * 
     * @return the source type of this adapter
     */
    public Class<S> getSourceType() {
        return this.sourceType;
    }

    /**
     * Returns the destination type of this adapter.
     * 
     * @return the destination type of this adapter
     */
    public Class<D> getCastType() {
        return this.castType;
    }

    @Override
    public D adapt(final S source) {
        if (source == null) {
            return null;
        }
        if (!this.getCastType().isInstance(source)) {
            this.dispatch(new CastingAdapterException(this, source));
            return null;
        }
        // This cast is guaranteed to succeed.
        return this.getCastType().cast(source);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CastingAdapter<?, ?>)) {
            return false;
        }
        final CastingAdapter<?, ?> other = (CastingAdapter<?, ?>) obj;
        if (!this.getSourceType().equals(other.getSourceType())) {
            return false;
        }
        if (!this.getCastType().equals(other.getCastType())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getSourceType().hashCode();
        result = 31 * result + this.getCastType().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("cast %s to %s", this.getSourceType(), this.getCastType());
    }

}
