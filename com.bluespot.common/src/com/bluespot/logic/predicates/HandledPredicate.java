package com.bluespot.logic.predicates;

import com.bluespot.logic.actors.Actor;
import com.bluespot.logic.actors.Actors;
import com.bluespot.logic.adapters.HandledAdapter;

/**
 * A {@link Predicate} that notifies a given {@link Actor} for failed
 * evaluations. The {@code Actor} will immediately receive the tested value once
 * the target predicate has evaluated to {@code false}. This allows actors to
 * potentially throw exceptions before this predicate returns.
 * <p>
 * This class is similar to {@link HandledAdapter} in that it provides a
 * mechanism for observing the state of a process. Like that interface, this
 * class only provides a setter for the handler, does not allow null handlers,
 * and does not include the handler in testing for equality.
 * <p>
 * It differs from {@link HandledAdapter} in that the handler is invoked once
 * and only once for every value that evaluates to {@code false}. This behavior
 * is essentially a subset of the behavior that a {@link HandledAdapter} could
 * perform. The reason for this restriction, and why there is not a
 * {@code HandledPredicated} interface instead, is because {@link Predicate} are
 * not allowed to effect any real change, only test a value.
 * <p>
 * This similar but not equivalent behavior is a result of the fact that a
 * {@link Predicate} is, at its most fundamental level, a {@code Adapter<?,
 * Boolean>}; predicates enforce a stricter contract, so this class must enforce
 * a stricter contract as well.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value that is tested
 */
public final class HandledPredicate<T> implements Predicate<T> {

	private final Predicate<? super T> predicate;
	private Actor<? super T> handler;

	/**
	 * Constructs a {@link HandledPredicate} using the specified predicate and a
	 * no-op actor for the handler.
	 * 
	 * @param predicate
	 *            the predicate used by this object. It may not be null.
	 * @throws NullPointerException
	 *             if {@code predicate} is null
	 */
	public HandledPredicate(final Predicate<? super T> predicate) {
		this(predicate, Actors.noop());
	}

	/**
	 * Constructs a {@link HandledPredicate} using the specified predicate and
	 * the specified actor for the handler.
	 * 
	 * @param predicate
	 *            the predicate used by this object. It may not be null.
	 * @param handler
	 *            the handler that is notified for all {@code false} evaluations
	 * @throws NullPointerException
	 *             if either argument is null
	 */
	public HandledPredicate(final Predicate<? super T> predicate, final Actor<? super T> handler) {
		if (predicate == null) {
			throw new NullPointerException("predicate is null");
		}
		if (handler == null) {
			throw new NullPointerException("handler is null");
		}
		this.predicate = predicate;
		this.handler = handler;
	}

	/**
	 * Returns the handled predicate that actually represents the boolean test
	 * of this predicate.
	 * 
	 * @return the handled predicate
	 */
	public Predicate<? super T> getPredicate() {
		return this.predicate;
	}

	private Actor<? super T> getHandler() {
		return this.handler;
	}

	/**
	 * Sets the handler for failed tests to the specified handler. It will be
	 * invoked for all values that cause this predicate to evaluate to
	 * {@code false}, including null values.
	 * 
	 * @param handler
	 *            the handler that is notified of {@code false} evaluations. It
	 *            may not be null.
	 * 
	 * @throws NullPointerException
	 *             if {@code handler} is null. Use {@link Actors#noop()} for a
	 *             no-op actor.
	 */
	public void setHandler(final Actor<? super T> handler) {
		if (handler == null) {
			throw new NullPointerException("handler is null");
		}
		this.handler = handler;
	}

	@Override
	public boolean test(final T candidate) {
		final boolean result = this.getPredicate().test(candidate);
		if (!result) {
			this.getHandler().receive(candidate);
		}
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HandledPredicate<?>)) {
			return false;
		}
		final HandledPredicate<?> other = (HandledPredicate<?>) obj;
		if (!this.getPredicate().equals(other.getPredicate())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 11;
		result = 31 * result + this.getPredicate().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("HandledPredicate[%s]", this.getPredicate());
	}
}
