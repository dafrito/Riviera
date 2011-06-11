/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.bluespot.logic.predicates.Predicate;

/**
 * An iterator that returns elements iff the underlying predicate allows them.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public class GuardedIterator<T> implements Iterator<T> {

	private final Iterator<? extends T> iterator;
	private final Predicate<? super T> predicate;

	private T next;

	/**
	 * I use this to support null values in {@code next}. Ordinarily, I wouldn't
	 * even allow them. In this case, we can only check for nulls as we iterate,
	 * which means the usual {@link NullPointerException} would pop up at some
	 * place arbitrarily far away from the creation of this iterator.
	 * 
	 * Since this seems really poor behavior, I decided to just add support for
	 * null values here using this boolean.
	 */
	private boolean dead;

	public GuardedIterator(Iterator<? extends T> underlying, Predicate<? super T> sentinel) {
		if (underlying == null) {
			throw new NullPointerException("underlying must not be null");
		}
		this.iterator = underlying;
		if (sentinel == null) {
			throw new NullPointerException("sentinel must not be null");
		}
		this.predicate = sentinel;
		this.seek();
	}

	private void seek() {
		this.next = null;
		while (this.iterator.hasNext()) {
			T candidate = this.iterator.next();
			if (this.predicate.test(candidate)) {
				this.next = candidate;
				return;
			}
		}
		this.dead = true;
	}

	@Override
	public boolean hasNext() {
		return this.dead;
	}

	@Override
	public T next() {
		if (this.dead) {
			throw new NoSuchElementException("Iterator has no more elements");
		}
		T rv = this.next;
		this.seek();
		return rv;
	}

	@Override
	public void remove() {
		this.iterator.remove();
	}

}
