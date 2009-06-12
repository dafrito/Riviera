package com.bluespot.collections.proxies;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

/**
 * A proxy for deques.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of element contained in this deque
 */
public abstract class DequeProxy<E> extends QueueProxy<E> implements Deque<E> {

    /**
     * @return the source deque that is being proxied
     */
    protected abstract Deque<E> getSourceDeque();

    @Override
    protected Queue<E> getSourceQueue() {
        return this.getSourceDeque();
    }

    @Override
    public void addFirst(final E e) {
        this.getSourceDeque().addFirst(e);
    }

    @Override
    public void addLast(final E e) {
        this.getSourceDeque().addLast(e);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return this.getSourceDeque().descendingIterator();
    }

    @Override
    public E getFirst() {
        return this.getSourceDeque().getFirst();
    }

    @Override
    public E getLast() {
        return this.getSourceDeque().getLast();
    }

    @Override
    public boolean offerFirst(final E e) {
        return this.getSourceDeque().offerFirst(e);
    }

    @Override
    public boolean offerLast(final E e) {
        return this.getSourceDeque().offerLast(e);
    }

    @Override
    public E peekFirst() {
        return this.getSourceDeque().peekFirst();
    }

    @Override
    public E peekLast() {
        return this.getSourceDeque().peekLast();
    }

    @Override
    public E pollFirst() {
        return this.getSourceDeque().pollFirst();
    }

    @Override
    public E pollLast() {
        return this.getSourceDeque().pollLast();
    }

    @Override
    public E removeFirst() {
        return this.getSourceDeque().removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(final Object o) {
        return this.getSourceDeque().removeFirstOccurrence(o);
    }

    @Override
    public E removeLast() {
        return this.getSourceDeque().removeLast();
    }

    @Override
    public boolean removeLastOccurrence(final Object o) {
        return this.getSourceDeque().removeLastOccurrence(o);
    }

    /**
     * This method defers to {@link #removeFirst()}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public E pop() {
        return this.removeFirst();
    }

    /**
     * This method defers to {@link #addFirst(Object)}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void push(final E e) {
        this.addFirst(e);
    }

    /**
     * This method defers to {@link #addLast(Object)}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean add(final E e) {
        this.addLast(e);
        return true;
    }

    /**
     * This method defers to {@link #getFirst()}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public E element() {
        return this.getFirst();
    }

    /**
     * This method defers to {@link #offerLast(Object)}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean offer(final E e) {
        return this.offerLast(e);
    }

    /**
     * This method defers to {@link #peekFirst()}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public E peek() {
        return this.peekFirst();
    }

    /**
     * This method defers to {@link #pollFirst()}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public E poll() {
        return this.pollFirst();
    }

    /**
     * This method defers to {@link #removeFirst()}
     * <p>
     * {@inheritDoc}
     */
    @Override
    public E remove() {
        return this.removeFirst();
    }

    /**
     * This method defers to {@link #removeFirstOccurrence(Object)}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean remove(final Object o) {
        return this.removeFirstOccurrence(o);
    }

    /**
     * This method defers to {@link #add(Object)} for every element in the
     * specified collection.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        boolean modified = false;
        for (final E value : c) {
            if (this.add(value)) {
                modified = true;
            }
        }
        return modified;
    }

}
