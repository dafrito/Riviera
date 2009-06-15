package com.bluespot.logic.adapters;

import com.bluespot.logic.Adapters;
import com.bluespot.logic.predicates.AdaptingPredicate;
import com.bluespot.logic.predicates.Predicate;

/**
 * Adapts a given value to another type. Many common adapters can be obtained
 * from the static factory methods in {@link Adapters}. Adapters are also widely
 * used by {@link Predicate} and their builders.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the source type
 * @param <D>
 *            the destination type
 * @see Adapters
 * @see AdaptingPredicate
 */
public interface Adapter<S, D> {

    /**
     * Adapts the specified value to this adapter's destination type. Adapters
     * should make a best effort to return appropriate values for the widest
     * range of inputs; exceptions should rarely be thrown. Forgiving adapters
     * allow them to be used freely without unexpected results cropping up.
     * However, null values are commonly preserved in adapters; a null source
     * value should be converted to a null value unless the adapter explicitly
     * states otherwise.
     * 
     * @param source
     *            the source value to adapt
     * @return the adapted value
     */
    public D adapt(S source);
}
