package com.bluespot.logic.functions;

/**
 * A {@link Function} that will an input of any type. Typically, this means
 * there is a dynamic cast involved before a value is used.
 * 
 * @author dafrito
 * 
 * @param <T>
 *            the type of the returned value
 * @see AdaptingSafeFunction
 */
public interface SafeFunction<T> extends Function<Object, T> {

	@Override
	public T apply(Object input);
}
