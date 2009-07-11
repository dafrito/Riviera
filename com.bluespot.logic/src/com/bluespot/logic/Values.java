package com.bluespot.logic;

import com.bluespot.logic.values.Value;

/**
 * A collection of factory methods for common {@link Value} idioms.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Values {

    private Values() {
        // Suppress default constructor to ensure non-instantiability.
        throw new AssertionError("Instantiation not allowed");
    }

    /**
     * A {@link Value} implementation that only returns null.
     */
    private static final Value<?> VALUE_NULL = new Value<Object>() {
        public Object get() {
            return null;
        }
    };

    /**
     * Returns a {@link Value} object that always returns null.
     * 
     * @return a {@link Value} object that always returns null.
     */
    public static Value<?> nullValue() {
        return VALUE_NULL;
    }

}
