package com.bluespot.logic.actors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * A {@link Actor} that forwards all values to its child actors.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value expected by this sentinel
 */
public final class CompositeActor<T> implements Actor<T> {

	private final Collection<? extends Actor<? super T>> actors;

	/**
	 * Constructs a {@link CompositeActor} that forwards any received value to
	 * the specified actors.
	 * 
	 * @param actors
	 *            the actors that will receive objects
	 * @throws NullPointerException
	 *             if any of the actors are null
	 */
	public CompositeActor(final Actor<? super T>... actors) {
		this(Arrays.asList(actors));
	}

	/**
	 * Constructs a {@link CompositeActor} that forwards any received value to
	 * the specified collection of actors.
	 * 
	 * @param actors
	 *            a collection of actors
	 * @throws NullPointerException
	 *             if {@code actors} is null, or if any actor contained in
	 *             {@code actors} is null
	 */
	public CompositeActor(final Collection<? extends Actor<? super T>> actors) {
		if (actors == null) {
			throw new NullPointerException("actors is null");
		}
		this.actors = Collections.unmodifiableList(new ArrayList<Actor<? super T>>(actors));
		if (this.actors.contains(null)) {
			throw new NullPointerException("actors cannot contain null elements");
		}
	}

	/**
	 * Returns this actors's children.
	 * 
	 * @return an unmodifiable view of this actors's children
	 */
	public Collection<? extends Actor<? super T>> getActors() {
		// It's safe to return our list directly since it's already
		// unmodifiable.
		return this.actors;
	}

	/**
	 * Forwards the specified value to all of the child actors of this
	 * composite.
	 * <p>
	 * <em>This operation is not guaranteed to be atomic</em>. This class makes
	 * no effort to recover from exceptions. This is unfortunate, but I believe
	 * it's safer than attempting to handle exceptions or ignoring them until
	 * all actors have been called.
	 */
	@Override
	public void receive(final T value) {
		for (final Actor<? super T> actor : this.actors) {
			actor.receive(value);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CompositeActor<?>)) {
			return false;
		}
		final CompositeActor<?> other = (CompositeActor<?>) obj;
		if (!this.getActors().equals(other.getActors())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 11;
		result = 31 * result + this.getActors().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("CompositeActor[%s]", this.getActors());
	}

}
