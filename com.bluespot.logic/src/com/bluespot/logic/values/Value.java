package com.bluespot.logic.values;

/**
 * Represents some value that can be retrieved. {@code Value} objects abstract
 * the process of producing some value.
 * <p>
 * The {@code Value} interface is very similar to a {@code Procedure} interface
 * in other libraries: a single method with no arguments that returns some
 * value. While {@code Value} objects do consist of procedures, their
 * responsibility is to house and return some value, and I find this abstraction
 * much more suitable than a {@code Procedure} interface.
 * <p>
 * {@code Value} implementations are intentionally given a wide range of freedom
 * in how they are implemented. They do not need to be immutable nor do they
 * need be consistent in the values they return.
 * <p>
 * Since this interface does not specify that exceptions are thrown, {@code
 * Value} implementations should return {@code null} when the value cannot not
 * be retrieved. Recovery from failed retrievals depends on the {@code Value}
 * implementation: common examples would be IO exceptions or missing input from
 * the user.
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
