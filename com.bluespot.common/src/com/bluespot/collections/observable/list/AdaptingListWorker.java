package com.bluespot.collections.observable.list;

import java.util.ConcurrentModificationException;
import java.util.List;

import com.bluespot.logic.adapters.Adapter;

/**
 * A {@link ListWorker} that adapts one list to another, keeping both lists in
 * sync. Values will be converted using the specified {@link Adapter}.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of element in the source list
 * @param <D>
 *            the type of element in the target list
 */
public final class AdaptingListWorker<S, D> implements ListWorker<S> {

    private final List<? super D> targetList;
    private final Adapter<? super S, ? extends D> adapter;

    private int lastSize;

    /**
     * Constructs a {@code AdaptingListWorker} using the specified adapter to
     * synchronize the two specified lists.
     * 
     * @param adapter
     *            the adapter that converts values in the source list to the
     *            target
     * @param targetList
     *            the target list that will be populated with converted values.
     *            It must not contain any elements.
     * @throws NullPointerException
     *             if any of the arguments are null
     * @throws IllegalArgumentException
     *             if the target list is not empty
     */
    public AdaptingListWorker(final Adapter<? super S, ? extends D> adapter, final List<? super D> targetList) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        if (targetList == null) {
            throw new NullPointerException("targetList is null");
        }
        if (!targetList.isEmpty()) {
            throw new IllegalArgumentException("targetList is not empty");
        }
        this.adapter = adapter;
        this.targetList = targetList;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ConcurrentModificationException
     *             if the list is ever modified outside the scope of this
     *             method. This exception is thrown on a best-effort basis, and
     *             should only be used to detect bugs.
     */
    public void elementSet(final int index, final S newValue) {
        this.checkForComodification();
        this.targetList.set(index, this.adapter.adapt(newValue));
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ConcurrentModificationException
     *             if the list is ever modified outside the scope of this
     *             method. This exception is thrown on a best-effort basis, and
     *             should only be used to detect bugs.
     */
    public void elementAdded(final int index, final S newValue) {
        this.checkForComodification();
        this.targetList.add(index, this.adapter.adapt(newValue));
        this.lastSize++;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ConcurrentModificationException
     *             if the list is ever modified outside the scope of this
     *             method. This exception is thrown on a best-effort basis, and
     *             should only be used to detect bugs.
     */
    public void elementRemoved(final int index) {
        this.checkForComodification();
        this.targetList.remove(index);
        this.lastSize--;
    }

    /**
     * Returns the adapter used by this worker
     * 
     * @return the adapter used by this worker
     */
    public Adapter<? super S, ? extends D> getAdapter() {
        return this.adapter;
    }

    /**
     * Returns the list that is populated by this worker.
     * 
     * @return the list that is popualte by this worker
     */
    public List<? super D> getTargetList() {
        return this.targetList;
    }

    @Override
    public String toString() {
        return String.format("AdaptingListWorker[adapter: %s]", this.adapter);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AdaptingListWorker<?, ?>)) {
            return false;
        }
        final AdaptingListWorker<?, ?> other = (AdaptingListWorker<?, ?>) obj;
        if (!this.getAdapter().equals(other.getAdapter())) {
            return false;
        }
        // We intentionally use == instead of .equals because we're
        // interested in references, not equality
        if (this.getTargetList() != other.getTargetList()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.adapter.hashCode();
        result = 31 * result + this.targetList.hashCode();
        return result;
    }

    private void checkForComodification() {
        if (this.targetList.size() != this.lastSize) {
            throw new ConcurrentModificationException("sizes were unequal");
        }
    }

}