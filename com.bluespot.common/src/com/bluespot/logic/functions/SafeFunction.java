package com.bluespot.logic.functions;

/**
 * A {@link Function} that will take an input of any type. Typically, this means
 * there is a dynamic cast involved before a value is used.
 * 
 * @author Aaron Faanes
 * 
 * @param <R>
 *            the type of the returned value
 * @see AdaptingSafeFunction
 */
public interface SafeFunction<R> extends Function<Object, R> {

}
