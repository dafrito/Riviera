package logic.actors;

import logic.adapters.Adapter;
import logic.predicates.Predicate;
import logic.runnables.Closure;

/**
 * Represents an action that depends on received values. Actors are unrestricted
 * in what they can do once they receive an object. They do not follow the
 * garbage-in/garbage-out philosophy of {@link Predicate} and {@link Adapter}.
 * This freedom allows actors to be extremely flexible listeners for any given
 * event.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of element this actor expects.
 * @see Actors
 * @see Producer
 * @see Closure
 */
public interface Actor<T> {

	/**
	 * This method is invoked when the actor receives a value.
	 * 
	 * @param value
	 *            the received value
	 */
	void receive(T value);
}
