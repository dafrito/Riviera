package logic.adapters;

import logic.actors.Actor;

/**
 * Represents an {@link Adapter} that can be observed for notable events. The
 * definition of what events are produced is intentionally left vague, but some
 * guidelines still apply. Generally speaking, events should be specific,
 * important, and consistent with the domain of the adapter. Failures should
 * usually be indicated with an {@link Exception} event type or subtype. Simple
 * messages should use a {@link String} event type.
 * <p>
 * Since the type of events, and the times that they are generated, is
 * implementation-specific, this means that handlers can be tightly coupled to
 * the adapter they handle. In practice, this tight coupling is necessary in
 * order for the handler to intelligently manage the events that are produced by
 * this adapter.
 * <p>
 * Implementations should not expose their handler. To enforce this constraint,
 * a {@code getHandler()} method has been deliberately omitted. This allows
 * {@link HandledAdapter} to have the minimum required level of mutable state.
 * It also reinforces the constraint that {@code Adapter} implementations do not
 * use their handlers when testing for equality.
 * <p>
 * In practice, the most common types of events that are produced are ones that
 * describe failure. This is natural since the {@code Adapter} interface on its
 * own requires a no-throw exception policy. As a consequence, {@code Adapter}
 * implementations must return null on failed conversions. While this keeps the
 * {@code Adapter} interface clean for common cases, it causes a loss of useful
 * information since exceptions would be swallowed. {@code HandledAdapter}
 * allows these implementations to still abide by the {@code Adapter}
 * interface's exception policy and notify clients of errors and exceptions that
 * were encountered during conversion.
 * <p>
 * One note is that null values should not be handled by an event. This is
 * consistent with the {@code Adapter} interface. This is because {@code null}
 * values represent an error state in most cases, so reporting a {@code null}
 * value would cause an event for every adapter in a chain.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of the source object used during conversion
 * @param <D>
 *            the final type of the converted or adapted object
 * @param <E>
 *            the type of the handled value
 * @see AbstractHandledAdapter
 */
public interface HandledAdapter<S, D, E> extends Adapter<S, D> {

	/**
	 * Sets the handler for this adapter to the specified, non-null
	 * {@link Actor}. The {@code Actor} will be notified for events that occur
	 * during the adapting process.
	 * <p>
	 * This method is not optional: {@link HandledAdapter} implementations are
	 * required to accept changing handlers at all times. Because of this, do
	 * not include the current handler when performing equality or hashing
	 * operations.
	 * 
	 * @param handler
	 *            the handler that will accept events from this adapter. It must
	 *            not be null.
	 * @throws NullPointerException
	 *             if {@code handler} is null. Use {@link Actor#noop()} to
	 *             represent an empty or null handler.
	 * @see Actor#noop()
	 */
	public void setHandler(Actor<? super E> handler);

}
