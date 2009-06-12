package com.bluespot.collections.observable.deque;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bluespot.collections.proxies.DequeProxy;

/**
 * A deque that emits events as it is changed.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of element contained in this deque
 * @see DequeListener
 */
public final class ObservableDeque<E> extends DequeProxy<E> {

    private final Deque<E> sourceDeque = new ArrayDeque<E>();

    private final List<DequeListener<E>> listeners = new CopyOnWriteArrayList<DequeListener<E>>();

    /**
     * Constructs an empty observable deque.
     */
    public ObservableDeque() {
        // Do nothing
    }

    /**
     * Constructs an observable deque, populating this deque with the values in
     * the specified collection.
     * 
     * @param sourceCollection
     *            the collection containing the values to be used in this deque
     */
    public ObservableDeque(final Collection<E> sourceCollection) {
        if (sourceCollection == null) {
            throw new NullPointerException("sourceCollection cannot be null");
        }
        for (final E e : sourceCollection) {
            this.sourceDeque.addLast(e);
        }
    }

    /**
     * Adds the specified listener to listen for events from this deque
     * 
     * @param listener
     *            the listener to add
     */
    public void addDequeListener(final DequeListener<E> listener) {
        this.listeners.add(listener);
    }

    /**
     * Removes the specified listener from this deque
     * 
     * @param listener
     *            the listener to remove
     */
    public void removeDequeListener(final DequeListener<E> listener) {
        this.listeners.remove(listener);
    }

    @Override
    protected Deque<E> getSourceDeque() {
        return this.sourceDeque;
    }

    @Override
    public void addFirst(final E e) {
        final E oldFirstElement = this.peekFirst();
        super.addFirst(e);
        this.fireFirstElementAdded(oldFirstElement);
    }

    @Override
    public void addLast(final E e) {
        final E oldLastElement = this.peekLast();
        super.addLast(e);
        this.fireLastElementAdded(oldLastElement);
    }

    @Override
    public boolean offerFirst(final E e) {
        final E oldFirstElement = this.peekFirst();
        final boolean elementAccepted = super.offerFirst(e);
        if (!elementAccepted) {
            return false;
        }
        this.fireFirstElementAdded(oldFirstElement);
        return true;
    }

    @Override
    public boolean offerLast(final E e) {
        final E oldLastElement = this.peekLast();
        final boolean elementAccepted = super.offerLast(e);
        if (!elementAccepted) {
            return false;
        }
        this.fireLastElementAdded(oldLastElement);
        return true;
    }

    @Override
    public E removeFirst() {
        final E oldFirstElement = super.removeFirst();
        this.fireFirstElementRemoved(oldFirstElement);
        return oldFirstElement;
    }

    @Override
    public boolean removeFirstOccurrence(final Object o) {
        if (super.removeFirstOccurrence(o)) {
            this.fireDequeChanged();
            return true;
        }
        return false;
    }

    @Override
    public E removeLast() {
        final E oldLastElement = super.removeLast();
        this.fireLastElementRemoved(oldLastElement);
        return oldLastElement;
    }

    @Override
    public boolean removeLastOccurrence(final Object o) {
        if (super.removeLastOccurrence(o)) {
            this.fireDequeChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        if (super.removeAll(c)) {
            this.fireDequeChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        if (super.retainAll(c)) {
            this.fireDequeChanged();
            return true;
        }
        return false;
    }

    private void fireFirstElementAdded(final E oldFirstElement) {
        for (final DequeListener<E> listener : this.listeners) {
            listener.firstElementAdded(oldFirstElement);
        }
    }

    private void fireLastElementAdded(final E oldLastElement) {
        for (final DequeListener<E> listener : this.listeners) {
            listener.lastElementAdded(oldLastElement);
        }
    }

    private void fireFirstElementRemoved(final E oldFirstElement) {
        for (final DequeListener<E> listener : this.listeners) {
            listener.firstElementRemoved(oldFirstElement);
        }
    }

    private void fireLastElementRemoved(final E oldLastElement) {
        for (final DequeListener<E> listener : this.listeners) {
            listener.lastElementRemoved(oldLastElement);
        }
    }

    private void fireDequeChanged() {
        for (final DequeListener<E> listener : this.listeners) {
            listener.dequeChanged();
        }
    }
}
