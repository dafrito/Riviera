package com.bluespot.logic.visitors;

/**
 * Represents a passive listener or actor on given values.
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
