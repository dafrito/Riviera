package com.bluespot.collections.proxies;

import java.util.Collection;
import java.util.Queue;

/**
 * Proxies all requests to a specified queue.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of element in the queue
 */
public abstract class QueueProxy<E> extends CollectionProxy<E> implements Queue<E> {

    /**
     * @return the queue that is being proxied
     */
    protected abstract Queue<E> getSourceQueue();

    @Override
    protected Collection<E> getSourceCollection() {
        return this.getSourceQueue();
    }

    @Override
    public E element() {
        return this.getSourceQueue().element();
    }

    @Override
    public boolean offer(final E e) {
        return this.getSourceQueue().offer(e);
    }

    @Override
    public E peek() {
        return this.getSourceQueue().peek();
    }

    @Override
    public E poll() {
        return this.getSourceQueue().poll();
    }

    @Override
    public E remove() {
        return this.getSourceQueue().remove();
    }

}
