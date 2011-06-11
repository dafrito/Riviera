/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterable} that saves retrieved values. I use this class in places
 * where I'm likely to stop iterating midway through and iteration is relatively
 * expensive (such as file I/O).
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of iterated value
 */
public class SavingIterable<T> implements Iterable<T> {

	private final List<T> produced = new ArrayList<T>();

	private final Iterator<? extends T> producer;

	public SavingIterable(final Iterator<? extends T> producer) {
		if (producer == null) {
			throw new NullPointerException("producer must not be null");
		}
		this.producer = producer;
	}

	@Override
	public Iterator<T> iterator() {
		return new ConsumingIterator();
	}

	private class ConsumingIterator implements Iterator<T> {
		private final Iterator<T> producedIterator = produced.iterator();

		@Override
		public boolean hasNext() {
			return producedIterator.hasNext() || producer.hasNext();
		}

		@Override
		public T next() {
			if (producedIterator.hasNext()) {
				return producedIterator.next();
			}
			T next = producer.next();
			produced.add(next);
			return next;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Removal is not supported");
		}
	}

}
