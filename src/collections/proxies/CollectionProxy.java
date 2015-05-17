package collections.proxies;

import java.util.Collection;
import java.util.Iterator;

/**
 * Proxies all requests to a specified collection.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of element contained in this collection
 */
public abstract class CollectionProxy<E> implements Collection<E> {

	@Override
	public boolean add(final E e) {
		return this.getSourceCollection().add(e);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		return this.getSourceCollection().addAll(c);
	}

	@Override
	public void clear() {
		this.getSourceCollection().clear();
	}

	@Override
	public boolean contains(final Object o) {
		return this.getSourceCollection().contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return this.getSourceCollection().containsAll(c);
	}

	@Override
	public boolean equals(final Object other) {
		return this.getSourceCollection().equals(other);
	}

	@Override
	public boolean isEmpty() {
		return this.getSourceCollection().isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return this.getSourceCollection().iterator();
	}

	@Override
	public boolean remove(final Object o) {
		return this.getSourceCollection().remove(o);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return this.getSourceCollection().removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return this.getSourceCollection().retainAll(c);
	}

	/**
	 * @return the collection that is being proxied
	 */
	protected abstract Collection<E> getSourceCollection();

	@Override
	public int size() {
		return this.getSourceCollection().size();
	}

	@Override
	public Object[] toArray() {
		return this.getSourceCollection().toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return this.getSourceCollection().toArray(a);
	}

	@Override
	public int hashCode() {
		return this.getSourceCollection().hashCode();
	}

}
