package com.bluespot.logic.values;

/**
 * Value represents some value that can be retrieved at some point in time. At
 * the bare minimum, {@code Value} objects merely house some constant value.
 * While this case is useful, the purpose of {@code Value} objects is to
 * abstract the process of producing some value. In this way, they represent a
 * procedure.
 * <p>
 * Values are very different in purpose from their {@code Adapter} and {@code
 * Predicate} counterparts. They do not need to be immutable; in fact, their
 * mutability is a great asset and reason for their existence. Their main
 * intention is to wrap a process of creating or retrieving a value.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of the returned value
 */
public interface Value<T> {

    /**
     * Returns the value of this {@code Value} object.
     * 
     * @return the value of this {@code Value} object.
     */
    public T get();

}
