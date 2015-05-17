/**
 * 
 */
package logic.iterators;

import java.util.Iterator;

import logic.actors.Actor;

/**
 * An {@link Iterator} that calls a specified {@link Actor} with iterated
 * values.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 * 
 */
public class SideEffectIterator<T> implements Iterator<T> {

	private final Iterator<? extends T> iterator;
	private final Actor<? super T> actor;

	public SideEffectIterator(final Iterator<? extends T> underlying, final Actor<? super T> sideEffect) {
		if (underlying == null) {
			throw new NullPointerException("underlying must not be null");
		}
		this.iterator = underlying;
		if (sideEffect == null) {
			throw new NullPointerException("sideEffect must not be null");
		}
		this.actor = sideEffect;
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public T next() {
		T value = this.iterator.next();
		this.actor.receive(value);
		return value;
	}

	@Override
	public void remove() {
		this.iterator.remove();
	}

}
