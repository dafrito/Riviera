/**
 * 
 */
package logic.iterators;

import java.util.Iterator;

import logic.adapters.Adapter;

/**
 * An {@link Iterator} that adapts iterated values using an {@link Adapter}.
 * 
 * @author Aaron Faanes
 * @param <S>
 *            the source value returned by the underlying iterator
 * @param <D>
 *            the adapted value returned by the specified adapter
 * 
 */
public class AdaptingIterator<S, D> implements Iterator<D> {

	private final Iterator<? extends S> iterator;
	private final Adapter<? super S, ? extends D> adapter;

	public AdaptingIterator(final Iterator<? extends S> underlying, final Adapter<? super S, ? extends D> adapter) {
		if (underlying == null) {
			throw new NullPointerException("underlying must not be null");
		}
		this.iterator = underlying;
		if (adapter == null) {
			throw new NullPointerException("adapter must not be null");
		}
		this.adapter = adapter;
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public D next() {
		return this.adapter.adapt(this.iterator.next());
	}

	@Override
	public void remove() {
		this.iterator.remove();
	}

}
