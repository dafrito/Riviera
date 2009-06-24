package com.bluespot.logic.predicates;

import com.bluespot.logic.adapters.Adapter;

/**
 * A {@link Predicate} that uses the specified {@link Adapter} to convert a
 * given value. The adapted value will then be passed to the specified
 * predicate.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of the source value
 * @param <D>
 *            the type of the converted value
 */
public final class AdaptingPredicate<S, D> implements Predicate<S> {

    private final Adapter<? super S, D> adapter;

    private final Predicate<? super D> predicate;

    /**
     * Constructs a predicate that uses the specified adapter to convert a given
     * value. The adapted value will then be passed to the specified predicate.
     * 
     * @param adapter
     *            the adapter used for conversion
     * @param predicate
     *            the predicate for the adapted value
     * @throws NullPointerException
     *             if either argument is null
     */
    public AdaptingPredicate(final Adapter<? super S, D> adapter, final Predicate<? super D> predicate) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        this.adapter = adapter;
        this.predicate = predicate;
    }

    /**
     * Returns the adapter that performs the conversion for this predicate.
     * 
     * @return the adapter that performs the conversion for this predicate
     */
    public Adapter<? super S, D> getAdapter() {
        return this.adapter;
    }

    /**
     * Returns the predicate for the adapted value
     * 
     * @return the predicate for the adapted value
     * @see Adapter
     */
    public Predicate<? super D> getPredicate() {
        return this.predicate;
    }

    @Override
    public boolean test(final S value) {
        final D adaptedValue = this.adapter.adapt(value);
        return this.predicate.test(adaptedValue);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AdaptingPredicate<?, ?>)) {
            return false;
        }
        final AdaptingPredicate<?, ?> other = (AdaptingPredicate<?, ?>) obj;
        if (!this.getAdapter().equals(other.getAdapter())) {
            return false;
        }
        if (!this.getPredicate().equals(other.getPredicate())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * this.getAdapter().hashCode();
        result = 31 * this.getPredicate().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("has %s that %s", this.getAdapter(), this.getPredicate());
    }

}
