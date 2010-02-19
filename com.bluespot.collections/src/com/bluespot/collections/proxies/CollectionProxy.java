package com.bluespot.collections.proxies;

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

    public boolean add(final E e) {
        return this.getSourceCollection().add(e);
    }

    public boolean addAll(final Collection<? extends E> c) {
        return this.getSourceCollection().addAll(c);
    }

    public void clear() {
        this.getSourceCollection().clear();
    }

    public boolean contains(final Object o) {
        return this.getSourceCollection().contains(o);
    }

    public boolean containsAll(final Collection<?> c) {
        return this.getSourceCollection().containsAll(c);
    }

    @Override
    public boolean equals(final Object other) {
        return this.getSourceCollection().equals(other);
    }

    public boolean isEmpty() {
        return this.getSourceCollection().isEmpty();
    }

    public Iterator<E> iterator() {
        return this.getSourceCollection().iterator();
    }

    public boolean remove(final Object o) {
        return this.getSourceCollection().remove(o);
    }

    public boolean removeAll(final Collection<?> c) {
        return this.getSourceCollection().removeAll(c);
    }

    public boolean retainAll(final Collection<?> c) {
        return this.getSourceCollection().retainAll(c);
    }

    /**
     * @return the collection that is being proxied
     */
    protected abstract Collection<E> getSourceCollection();

    public int size() {
        return this.getSourceCollection().size();
    }

    public Object[] toArray() {
        return this.getSourceCollection().toArray();
    }

    public <T> T[] toArray(final T[] a) {
        return this.getSourceCollection().toArray(a);
    }

    @Override
    public int hashCode() {
        return this.getSourceCollection().hashCode();
    }

}
