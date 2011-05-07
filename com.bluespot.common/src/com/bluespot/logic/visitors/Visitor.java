package com.bluespot.logic.visitors;

import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.values.Value;

/**
 * Represents a passive listener or actor on given values. Visitors are similar
 * to {@link Value} implementations in that they are relatively free to act on
 * specified value. They do not follow the garbage-in/garbage-out philosophy of
 * {@link Predicate} and {@link Adapter}; in fact, some {@link Visitor}
 * implementations <em>only</em> throw exceptions. This freedom allows them to
 * be extremely flexible listeners for any given event.
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
