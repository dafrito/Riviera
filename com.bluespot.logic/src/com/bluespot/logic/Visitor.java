package com.bluespot.logic;

/**
 * A visitor of some elements {@code T}.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of element this visitor expects.
 */
public interface Visitor<T> {
    /**
     * This method is invoked when the visitor receives a value.
     * 
     * @param value
     *            the received value
     */
    void accept(T value);
}
