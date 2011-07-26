/**
 * 
 */
package com.bluespot.logic.actors;

/**
 * A producer of values that are passed to an underlying {@link Actor}. This is
 * useful if you have a recurring source of values that you'd like to pass to
 * some actor.
 * <p>
 * When implementing this class, you'll need to explicitly call
 * {@link #produce(Object)} in order to pass values to the underlying actor.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of produced values
 * @see Actor
 */
public class Producer<T> {

	private Actor<? super T> actor;

	/**
	 * Construct a producer with a noop actor.
	 * 
	 * @see Actors#noop()
	 */
	public Producer() {
		this(Actors.noop());
	}

	/**
	 * Construct a producer that uses the specified actor.
	 * 
	 * @param actor
	 *            the actor that will receive produced values. If null, then
	 *            {@link Actors#noop()} is used as the actor.
	 */
	public Producer(Actor<? super T> actor) {
		this.setActor(actor);
	}

	/**
	 * Immediately change the underlying actor to the specified value.
	 * 
	 * @param actor
	 *            the actor that will receive produced values. If null, then
	 *            {@link Actors#noop()} is used as the actor.
	 */
	public void setActor(Actor<? super T> actor) {
		if (actor == null) {
			actor = Actors.noop();
		}
		this.actor = actor;
	}

	/**
	 * Return the underlying actor.
	 * 
	 * @return the underlying actor
	 */
	public Actor<? super T> getActor() {
		return this.actor;
	}

	/**
	 * Send the specified value to the underlying actor.
	 * 
	 * @param value
	 *            the produced value
	 */
	protected void produce(T value) {
		if (value != null) {
			this.actor.receive(value);
		}
	}

}
