package com.bluespot.logic.values;

import com.bluespot.logic.adapters.HandledAdapter;

/**
 * Represents some value that can be retrieved. {@code Value} objects abstract
 * the process of producing some value.
 * <p>
 * The {@code Value} interface is very similar to a {@code Procedure} interface
 * in other libraries: a single method with no arguments that returns some
 * value. Programmers from functional languages would recognize it as a simple
 * function. It would therefore seem that {@code Procedure} or {@code Function}
 * would have made a more suitable name. I believe that, while {@code Value}
 * implementations do consist of steps to generate a value, their primary
 * responsibility is to represent a value, not represent a procedure.
 * <p>
 * {@code Value} implementations are intentionally given a wide range of freedom
 * in how they are implemented. They do not need to be consistent in the values
 * they return, but the procedure used to generate the values should not change.
 * In this way, {@code Value} objects do not necessarily represent constant,
 * immutable values, but they do represent constant, immutable procedures to
 * generate values.
 * <p>
 * Since this interface does not specify that exceptions are thrown, {@code
 * Value} implementations should return {@code null} when the value cannot not
 * be retrieved. Recovery from failed retrievals depends on the {@code Value}
 * implementation: common examples would be IO exceptions or missing input from
 * the user. {@link HandledAdapter} implementations assist in handling these
 * errors gracefully. Consequently, a {@code HandledValue} interface may be
 * written for {@code Value} implementations that perform work on unreliable
 * inputs, but the need hasn't yet arisen.
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
