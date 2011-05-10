package com.bluespot.logic.functions;

/**
 * A {@link Curryable} interface that accepts any object.
 * 
 * @author Aaron Faanes
 * 
 * @param <R>
 *            the type of function produced through currying
 * @see SafeFunction
 */
public interface SafeCurryable<R extends SafeFunction<?>> extends Curryable<Object, R> {

}
