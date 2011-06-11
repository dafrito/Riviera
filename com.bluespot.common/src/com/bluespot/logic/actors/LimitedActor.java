/**
 * 
 */
package com.bluespot.logic.actors;

/**
 * An {@link Actor} that executes its underlying action a finite number of
 * times.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of received value
 * @see Actors#limited(Actor, int)
 */
public class LimitedActor<T> implements Actor<T> {

	private int charges;
	private final Actor<? super T> actor;

	public LimitedActor(final Actor<? super T> actor, final int charges) {
		if (actor == null) {
			throw new NullPointerException("actor must not be null");
		}
		this.actor = actor;
		if (charges <= 0) {
			throw new NullPointerException("charges must be a positive integer");
		}
		this.charges = charges;
	}

	@Override
	public void receive(T value) {
		if (this.charges-- > 0) {
			this.actor.receive(value);
		}
	}
}
