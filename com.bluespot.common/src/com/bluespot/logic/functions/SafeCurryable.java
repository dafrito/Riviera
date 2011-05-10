package com.bluespot.logic.functions;

/**
 * A {@link Curryable} interface that accepts any object. This is a marker
 * interface that is used to indicate universal {@link Curryable} objects.
 * Methods and classes exist to adapt naive {@code Curryable} instances to this
 * type.
 * 
 * @author Aaron Faanes
 * 
 * @param <R>
 *            the type of value returned by functions produced through currying
 * @see SafeFunction
 * @see Functions#protect(com.bluespot.logic.adapters.Adapter, Curryable,
 *      com.bluespot.logic.adapters.Adapter)
 */
public interface SafeCurryable<R> extends Curryable<Object, SafeFunction<? extends R>> {

}
