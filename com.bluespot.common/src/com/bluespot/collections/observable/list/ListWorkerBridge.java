package com.bluespot.collections.observable.list;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A listener that bridges all changes from the specified list to a specified
 * worker.
 * 
 * @param <T>
 *            the type of element in the model
 * @see Observables#listen(ObservableList, ListWorker)
 */
public final class ListWorkerBridge<T> implements ListDataListener {

    private final ObservableList<? extends T> model;
    private final ListWorker<? super T> worker;

    /**
     * Constructs a {@code ListWorkerBridge} using the specified model that is
     * synchronized with the specified worker.
     * <p>
     * This method does not add itself to the specified model, nor does it
     * perform any iteration on the initial elements of the model.
     * 
     * @param model
     *            the listened model.
     * @param worker
     *            the worker to add as a listener
     * @throws NullPointerException
     *             if model or worker is null
     */
    public ListWorkerBridge(final ObservableList<? extends T> model, final ListWorker<? super T> worker) {
        if (model == null) {
            throw new NullPointerException("model is null");
        }
        if (worker == null) {
            throw new NullPointerException("worker is null");
        }
        this.worker = worker;
        this.model = model;
    }

    @Override
	public void contentsChanged(final ListDataEvent e) {
        final int startIndex = e.getIndex0();
        final int endIndex = e.getIndex1();
        assert startIndex <= endIndex;
        assert (startIndex >= 0) && (endIndex >= 0);
        for (int i = startIndex; i <= endIndex; i++) {
            final T value = this.model.get(i);
            this.worker.elementSet(i, value);
        }
    }

    @Override
	public void intervalAdded(final ListDataEvent e) {
        final int startIndex = e.getIndex0();
        final int endIndex = e.getIndex1();
        assert startIndex <= endIndex;
        assert (startIndex >= 0) && (endIndex >= 0);
        for (int i = startIndex; i <= endIndex; i++) {
            final T value = this.model.get(i);
            this.worker.elementAdded(i, value);
        }
    }

    @Override
	public void intervalRemoved(final ListDataEvent e) {
        final int startIndex = e.getIndex0();
        final int endIndex = e.getIndex1();
        assert startIndex <= endIndex;
        assert (startIndex >= 0) && (endIndex >= 0);
        for (int i = startIndex; i <= endIndex; i++) {
            this.worker.elementRemoved(endIndex - i);
        }
    }

    /**
     * Returns the observed model of this bridge.
     * 
     * @return the observed model of this bridge
     */
    public ObservableList<? extends T> getModel() {
        return this.model;
    }

    /**
     * Returns the target worker of this bridge.
     * 
     * @return the worker this adapter uses
     */
    public ListWorker<? super T> getWorker() {
        return this.worker;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ListWorkerBridge<?>)) {
            return false;
        }
        final ListWorkerBridge<?> other = (ListWorkerBridge<?>) obj;
        if (this.model != other.getModel()) {
            return false;
        }
        if (!this.worker.equals(other.getWorker())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.worker.hashCode();
        result = 31 * result + this.model.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("ListWorkerBridge[worker: %s]", this.worker);
    }
}
