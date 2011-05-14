package com.bluespot.logic.actors;

import com.bluespot.logic.predicates.Predicate;

/**
 * A {@link Actor} that conditionally forwards received values to an underlying
 * actor. The predicate must evaluate to {@code true} before the underlying
 * actor receives provided values.
 * <p>
 * At a higher level, this class can be considered a sentinel that filters
 * provided data. It provides a bridge between {@code Predicate} and
 * {@code Actor} instances. Since this class also implements {@link Actor}, it
 * can be nested within other {@code GuardedActor} objects.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value expected by this sentinel
 */
public final class GuardedActor<T> implements Actor<T> {

	private final Predicate<? super T> predicate;
	private final Actor<? super T> actor;

	/**
	 * Constructs a sentinel that guards the specified actor with the specified
	 * predicate.
	 * 
	 * @param predicate
	 *            the predicate that guards this sentinel's actor
	 * @param actor
	 *            the actor that is guarded by this sentinel
	 * @throws NullPointerException
	 *             if either argument is null
	 */
	public GuardedActor(final Predicate<? super T> predicate, final Actor<? super T> actor) {
		if (predicate == null) {
			throw new NullPointerException("predicate is null");
		}
		if (actor == null) {
			throw new NullPointerException("actor is null");
		}
		this.predicate = predicate;
		this.actor = actor;
	}

	/**
	 * Returns the {@link Predicate} used to guard the underlying actor.
	 * 
	 * @return the {@code Predicate} used to guard the underlying actor
	 */
	public Predicate<? super T> getPredicate() {
		return this.predicate;
	}

	/**
	 * Returns the {@link Actor} that is guarded by this sentinel
	 * 
	 * @return the {@code Actor} that is guarded by this sentinel
	 */
	public Actor<? super T> getActor() {
		return this.actor;
	}

	/**
	 * Checks the specified value. If this sentinel's predicate evaluates the
	 * value to {@code true}, it will be passed to the underlying actor.
	 * 
	 * @param value
	 *            the value to check
	 */
	@Override
	public void receive(final T value) {
		if (!this.predicate.test(value)) {
			return;
		}
		this.actor.receive(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GuardedActor<?>)) {
			return false;
		}
		final GuardedActor<?> other = (GuardedActor<?>) obj;
		if (!this.getPredicate().equals(other.getPredicate())) {
			return false;
		}
		if (!this.getActor().equals(other.getActor())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.getPredicate().hashCode();
		result = 31 * result + this.getActor().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("If value %s, then do %s.", this.getPredicate(), this.getActor());
	}

}
