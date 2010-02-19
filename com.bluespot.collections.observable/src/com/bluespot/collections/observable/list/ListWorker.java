package com.bluespot.collections.observable.list;

import java.util.List;

import javax.swing.event.ListDataListener;

/**
 * Represents a simple listener for lists. This is useful for when you're
 * interesting in adapting some list events, but don't want to write a
 * full-fledged {@link ListDataListener}.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of the expected element
 */
public interface ListWorker<T> {

    /**
     * Invoked when an index has changed values.
     * 
     * @param index
     *            the index of the changed element
     * @param newValue
     *            the new value
     */
    public void elementSet(final int index, final T newValue);

    /**
     * Invoked when an element has been added. Indices used here will not have
     * to be adjusted.
     * <p>
     * If multiple elements were added, this method will be called in increasing
     * order: {@code 0, 1, 2}, etc.
     * 
     * @param index
     *            the index of the added element. It can be used directly in a
     *            {@link List#add(int, Object)} call.
     * @param newValue
     *            the added element
     */
    public abstract void elementAdded(final int index, final T newValue);

    /**
     * Invoked when an element has been removed. Indices used here will not have
     * to be adjusted.
     * 
     * @param index
     *            the index of the element to remove. It can be used directly in
     *            a {@link List#remove(int)} call.
     */
    public abstract void elementRemoved(final int index);
}
