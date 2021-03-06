package logic.actors;

import java.awt.Component;
import java.util.Collection;

import logic.adapters.Adapter;
import logic.predicates.Predicate;
import logic.values.MutableValue;

/**
 * A collection of factory methods for common {@link Actor} idioms. The names of
 * these methods are intentionally "conversational" since this library is
 * usually statically imported. If this is the case, you can end up with such
 * statements like:
 * 
 * <pre>
 * when(lowerCase(), addToList(strings));
 * </pre>
 * 
 * On the other hand, sporadic uses of these factory methods are much harder to
 * understand. In those cases, you're better off using the object constructors
 * directly.
 * 
 * 
 * @author Aaron Faanes
 * @see Actor
 */
public final class Actors {

	private Actors() {
		// Suppress default constructor to ensure non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * A {@link Actor} implementation that does nothing.
	 * 
	 * @see Actors#noop()
	 */
	private static final Actor<Object> ACTOR_NOOP = new Actor<Object>() {
		@Override
		public void receive(final Object value) {
			// We intentionally do nothing with the specified value.
		}
	};

	/**
	 * Returns a {@link Actor} object that does nothing. The returned actor is
	 * functionally equivalent to {@code /dev/null}. This object is useful if an
	 * actor is required, but no action needs to be taken.
	 * 
	 * @param <T>
	 *            the type of received value
	 * 
	 * @return a {@link Actor} object that does nothing.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Actor<T> noop() {
		// This cast is safe because we never use the provided value anyway.
		return (Actor<T>) ACTOR_NOOP;
	}

	/**
	 * Returns a new {@link GuardedActor} that guards an underlying actor with
	 * the specified predicate.
	 * 
	 * @param <T>
	 *            the type of the received element
	 * @param predicate
	 *            the predicate that guards the specified actor. For any value,
	 *            the predicate must first evaluate to {@code true} before the
	 *            specified actor is allowed to accept it.
	 * @param actor
	 *            the actor that will accept all items that evaluate to
	 *            {@code true} according to the specified predicate
	 * @return a new {@code GuardedActor} that guards the underlying actor with
	 *         the specified predicate.
	 * @see GuardedActor
	 */
	public static <T> GuardedActor<T> when(final Predicate<? super T> predicate, final Actor<? super T> actor) {
		return new GuardedActor<T>(predicate, actor);
	}

	/**
	 * Returns a new {@link AdaptingActor} that accepts values of type {@code S}
	 * , converts them with the specified adapter, and passes the converted
	 * value to the specified actor.
	 * 
	 * @param <S>
	 *            the type that is initially accepted by the returned actor and
	 *            converted by the specified adapter
	 * @param <D>
	 *            the type that is accepted by the specified actor
	 * @param adapter
	 *            the adapter that performs the conversion of the accepted value
	 * @param target
	 *            the actor that ultimately accepted the converted value
	 * @return a {@code Actor} that adapts an accepted value and passes it to
	 *         the specified actor
	 * @throws NullPointerException
	 *             if either argument is null
	 * @see AdaptingActor
	 */
	public static <S, D> Actor<S> with(final Adapter<? super S, ? extends D> adapter,
			final Actor<? super D> target) {
		return new AdaptingActor<S, D>(adapter, target);
	}

	/**
	 * Returns a new {@link PopulatingActor} that adds all received elements to
	 * the specified collection. This method uses {@link Collection#add(Object)}
	 * and does not respond to failed or ignored addition requests.
	 * 
	 * @param <T>
	 *            the type of element in the collection
	 * @param collection
	 *            the collection that is populated by the returned actor
	 * @return a new {@code Actor} that adds elements to the specified
	 *         collection
	 * @see PopulatingActor
	 */
	public static <T> Actor<T> addTo(final Collection<? super T> collection) {
		return new PopulatingActor<T>(collection);
	}

	/**
	 * Returns a new {@link PruningActor} that removes all received elements
	 * from the specified collection. This method uses
	 * {@link Collection#remove(Object)} and does not respond to failed or
	 * ignored addition requests.
	 * 
	 * @param <T>
	 *            the type of element in the collection
	 * @param collection
	 *            the collection that is modified by the returned actors
	 * @return a new {@code Actor} that adds elements to the specified
	 *         collection
	 * @see PruningActor
	 */
	public static <T> Actor<T> removeFrom(final Collection<? super T> collection) {
		return new PruningActor<T>(collection);
	}

	/**
	 * Returns a new {@link Actor} that will call the underlying actor, up to
	 * {@code charges} times. After this limit is exceed, no action is taken.
	 * 
	 * @param <T>
	 *            the type of received value
	 * @param underlying
	 *            the underlying actor that will be used
	 * @param charges
	 *            the maximum number of times that underlying actor will be
	 *            called
	 * @return a new {@link Actor}
	 * @see LimitedActor
	 */
	public static <T> Actor<T> limited(Actor<? super T> underlying, int charges) {
		return new LimitedActor<T>(underlying, charges);
	}

	public static <T> HoldingActor<T> hold(T initial) {
		return new HoldingActor<T>(initial);
	}

	/**
	 * A {@link Actor} implementation that throws all exceptions it is given.
	 * 
	 * @see #throwException()
	 */
	private static final Actor<RuntimeException> ACTOR_THROWER = new Actor<RuntimeException>() {
		@Override
		public void receive(final RuntimeException value) {
			if (value != null) {
				throw value;
			}
		}
	};

	/**
	 * Returns a {@link Actor} that throws any {@link RuntimeException} that it
	 * is given.
	 * 
	 * @return a {@link Actor} that throws any {@link RuntimeException} objects
	 *         passed to it
	 */
	public static Actor<RuntimeException> throwException() {
		return ACTOR_THROWER;
	}

	private static final Actor<Component> REPAINTING_ACTOR = new Actor<Component>() {
		@Override
		public void receive(final Component component) {
			if (component != null) {
				component.repaint();
			}
		}
	};

	public static Actor<Component> repaint() {
		return REPAINTING_ACTOR;
	}

	/**
	 * Update the specified {@link MutableValue} with values sent to the
	 * returned {@link Actor}.
	 * 
	 * @param mutableValue
	 *            the value that is modified
	 * @param <T>
	 *            the type of underlying value
	 * @return an {@link Actor} that updates an underlying {@link MutableValue}.
	 * @see #hold(Object)
	 */
	public static <T> Actor<T> set(final MutableValue<T> mutableValue) {
		if (mutableValue == null) {
			throw new NullPointerException("mutableValue must not be null");
		}
		return new Actor<T>() {
			@Override
			public void receive(T value) {
				mutableValue.set(value);
			}
		};
	}

	/**
	 * Return a {@link LongActor} that forwards values to the underlying
	 * {@code Actor<Long>}.
	 * 
	 * @param actor
	 *            the underlying actor that will receive long values
	 * @return a new {@link LongActor}
	 * @see LongActor
	 */
	public static LongActor wrapLong(final Actor<? super Long> actor) {
		if (actor == null) {
			throw new NullPointerException("actor must not be null");
		}
		return new LongActor() {
			@Override
			public void receive(long value) {
				actor.receive(value);
			}
		};
	}
}
