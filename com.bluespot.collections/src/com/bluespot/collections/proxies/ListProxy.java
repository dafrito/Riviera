package com.bluespot.collections.proxies;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Proxies all requests to a specified list.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of element contained in this list
 */
public abstract class ListProxy<E> extends CollectionProxy<E> implements List<E> {

    public void add(final int index, final E element) {
        this.getSourceList().add(index, element);
    }

    public boolean addAll(final int index, final Collection<? extends E> c) {
        return this.getSourceList().addAll(index, c);
    }

    public E get(final int index) {
        return this.getSourceList().get(index);
    }

    /**
     * @return the list that is being proxied
     */
    protected abstract List<E> getSourceList();

    public int indexOf(final Object o) {
        return this.getSourceList().indexOf(o);
    }

    public int lastIndexOf(final Object o) {
        return this.getSourceList().lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return this.getSourceList().listIterator();
    }

    public ListIterator<E> listIterator(final int index) {
        return this.getSourceList().listIterator(index);
    }

    public E remove(final int index) {
        return this.getSourceList().remove(index);
    }

    public E set(final int index, final E element) {
        return this.getSourceList().set(index, element);
    }

    public List<E> subList(final int fromIndex, final int toIndex) {
        return this.getSourceList().subList(fromIndex, toIndex);
    }

    @Override
    protected Collection<E> getSourceCollection() {
        return this.getSourceList();
    }

}
