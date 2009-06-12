package com.bluespot.logic.adapters;

/**
 * Adapts a value to another type. Null values should either be converted or
 * preserved.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the source type
 * @param <D>
 *            the destination type
 */
public interface Adapter<S, D> {

    /**
     * Adapts the specified value to this adapter's destination type.
     * <p>
     * Null values should be reasonably converted or preserved during the
     * adaption; do not assume that only non-null values will be used.
     * 
     * @param source
     *            the source value to adapt
     * @return the adapted value
     */
    public D adapt(S source);
}
