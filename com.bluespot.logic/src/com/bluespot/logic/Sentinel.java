package com.bluespot.logic;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bluespot.logic.predicates.Predicate;

/**
 * A sentinel checks values and passes accepted ones to visitors.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value expected by this sentinel
 */
public final class Sentinel<T> implements Visitor<T> {

	private final List<Visitor<? super T>> visitors = new CopyOnWriteArrayList<Visitor<? super T>>();
	private final Predicate<? super T> predicate;

	/**
	 * Constructs a sentinel that uses the specified predicate
	 * 
	 * @param predicate
	 *            the predicate that guards this sentinel's visitors
	 */
	public Sentinel(final Predicate<? super T> predicate) {
		if (predicate == null) {
			throw new NullPointerException("predicate is null");
		}
		this.predicate = predicate;
	}

	/**
	 * Adds a visitor to this sentinel.
	 * 
	 * @param visitor
	 *            the visitor to add
	 * @throws NullPointerException
	 *             if {@code visitor} is null
	 */
	public void addVisitor(final Visitor<? super T> visitor) {
		if (visitor == null) {
			throw new NullPointerException("visitor is null");
		}
		this.visitors.add(visitor);
	}

	/**
	 * Removes the specified visitor from this sentinel. If the visitor is not
	 * in this list, no action is taken.
	 * 
	 * @param visitor
	 *            the visitor to remove
	 */
	public void removeVisitor(final Visitor<? super T> visitor) {
		this.visitors.remove(visitor);
	}

	/**
	 * Checks the specified value. If it passes this sentinel's test, it will be
	 * passed to all of this sentinel's visitors.
	 * 
	 * @param value
	 *            the value to check
	 */
	public void accept(final T value) {
		if (!this.predicate.test(value)) {
			return;
		}
		for (final Visitor<? super T> visitor : this.visitors) {
			visitor.accept(value);
		}
	}

}
