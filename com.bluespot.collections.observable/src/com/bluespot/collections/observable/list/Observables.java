package com.bluespot.collections.observable.list;

import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.bluespot.logic.adapters.Adapter;

/**
 * A collection of some adapters and listeners for common patterns when dealing
 * with observable objects.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Observables {

    private Observables() {
        // Suppress default constructor to ensure non-instantiability.
        throw new AssertionError("Instantiation not allowed");
    }

    /**
     * Creates and adds a listener to the specified source list. The worker will
     * synchronize the two lists using the adapter for conversion.
     * <p>
     * Do not directly modify the target list. The only changes to it should
     * come from changes made to the source list. Changes made to the target
     * list outside this method will result in
     * {@link ConcurrentModificationException}'s being thrown.
     * 
     * @param <S>
     *            the type of element in the source list
     * @param <D>
     *            the type of element in the target list
     * 
     * @param adapter
     *            the adapter that converts values in the source list to the
     *            target
     * @param sourceList
     *            the observed list. Changes will be reflected in the target
     *            list
     * @param targetList
     *            the target list that will be populated with converted values.
     *            It must not contain any elements.
     * @return the listener that represents this adaptation. It has already been
     *         added to the source list.
     * @throws NullPointerException
     *             if any of the arguments are null
     * @throws IllegalArgumentException
     *             if the target list is not empty
     */
    public static <S, D> ListDataListener adapt(final Adapter<? super S, ? extends D> adapter,
            final ObservableList<S> sourceList, final List<? super D> targetList) {
        return Observables.listen(sourceList, new AdaptingListWorker<S, D>(adapter, targetList));
    }

    /**
     * Creates and adds a listener to the specified source list. The worker will
     * synchronize the two lists using the adapter for conversion.
     * <p>
     * Do not directly modify the target list. The only changes to it should
     * come from changes made to the source list. Changes made to the target
     * list outside this method will result in
     * {@link ConcurrentModificationException}'s being thrown.
     * 
     * @param <S>
     *            the type of element in the source list
     * @param <D>
     *            the type of element in the target list
     * 
     * @param adapter
     *            the adapter that converts values in the source list to the
     *            target
     * @param sourceList
     *            the observed list. Changes will be reflected in the target
     *            list
     * @param targetList
     *            the target list that will be populated with converted values.
     *            It must not contain any elements.
     * @return the listener that represents this adaptation. It has already been
     *         added to the source list.
     * @throws NullPointerException
     *             if any of the arguments are null
     * @throws IllegalArgumentException
     *             if the target list is not empty
     */
    public static <S, D> ListDataListener adaptOnEdt(final Adapter<? super S, ? extends D> adapter,
            final ObservableList<S> sourceList, final List<? super D> targetList) {
        return Observables.listenOnEdt(sourceList, new AdaptingListWorker<S, D>(adapter, targetList));
    }

    /**
     * Creates and adds a listener to the specified observable list. Any
     * modifications made to the observed list will be passed along to the
     * worker through the created listener.
     * <p>
     * This method will restrict any notification to Swing's event dispatch
     * thread and thus allows safe bridging between client lists and lists that
     * are tied to the user interface.
     * <p>
     * If any elements are already in the model, the worker will be immediately
     * invoked for them. This update will be delayed so that it will run on the
     * event dispatch thread.
     * 
     * @param <T>
     *            the type of element in the model
     * @param model
     *            the listened model. If the model contains any initial
     *            elements, they will be iterated over immediately.
     * @param worker
     *            the worker to add as a listener
     * @return the created listener. It is already added to the specified model,
     *         and is only returned if you wish to remove it later.
     * @throws NullPointerException
     *             if model or worker is null
     */
    public static <T> ListDataListener listen(final ObservableList<? extends T> model,
            final ListWorker<? super T> worker) {
        final ListWorkerBridge<T> listener = new ListWorkerBridge<T>(model, worker);
        model.addListDataListener(listener);
        for (int i = 0; i < model.getSize(); i++) {
            worker.elementAdded(i, model.getElementAt(i));
        }
        return listener;
    }

    /**
     * Creates and adds a listener to the specified observable list. Any
     * modifications made to the observed list will be passed along to the
     * worker through the created listener.
     * <p>
     * This method will restrict any notification to Swing's event dispatch
     * thread and thus allows safe bridging between client lists and lists that
     * are tied to the user interface.
     * <p>
     * If any elements are already in the model, the worker will be immediately
     * invoked for them. This update will be delayed so that it will run on the
     * event dispatch thread.
     * 
     * @param <T>
     *            the type of element in the model
     * @param model
     *            the listened model. If the model contains any initial
     *            elements, they will be iterated over immediately.
     * @param worker
     *            the worker to add as a listener
     * @return the created listener. It is already added to the specified model,
     *         and is only returned if you wish to remove it later.
     * @throws NullPointerException
     *             if model or worker is null
     */
    public static <T> ListDataListener listenOnEdt(final ObservableList<? extends T> model,
            final ListWorker<? super T> worker) {
        final ListWorkerBridge<T> internalListener = new ListWorkerBridge<T>(model, worker);
        final ListDataListener listener = new ListDataListener() {

            @Override
            public void intervalRemoved(final ListDataEvent e) {
                if (SwingUtilities.isEventDispatchThread()) {
                    internalListener.intervalRemoved(e);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            internalListener.intervalRemoved(e);
                        }
                    });
                }
            }

            @Override
            public void intervalAdded(final ListDataEvent e) {
                if (SwingUtilities.isEventDispatchThread()) {
                    internalListener.intervalAdded(e);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            internalListener.intervalAdded(e);
                        }
                    });
                }
            }

            @Override
            public void contentsChanged(final ListDataEvent e) {
                if (SwingUtilities.isEventDispatchThread()) {
                    internalListener.contentsChanged(e);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            internalListener.contentsChanged(e);
                        }
                    });
                }
            }
        };
        model.addListDataListener(listener);
        if (SwingUtilities.isEventDispatchThread()) {
            for (int i = 0; i < model.getSize(); i++) {
                worker.elementAdded(i, model.getElementAt(i));
            }
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    for (int i = 0; i < model.getSize(); i++) {
                        worker.elementAdded(i, model.getElementAt(i));
                    }
                }
            });
        }
        return listener;
    }
}

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
final class AdaptingListWorker<S, D> implements ListWorker<S> {

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
        return String.format("ListWorker from Observables.adapt[adapter: %s]", this.adapter);
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

/**
 * A listener that bridges all changes from the specified list to a specified
 * worker.
 * 
 * @param <T>
 *            the type of element in the model
 * @see Observables#listen(ObservableList, ListWorker)
 */
final class ListWorkerBridge<T> implements ListDataListener {

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

    public void contentsChanged(final ListDataEvent e) {
        final int startIndex = e.getIndex0();
        final int endIndex = e.getIndex1();
        assert startIndex <= endIndex;
        assert startIndex >= 0 && endIndex >= 0;
        for (int i = startIndex; i <= endIndex; i++) {
            final T value = this.model.get(i);
            this.worker.elementSet(i, value);
        }
    }

    public void intervalAdded(final ListDataEvent e) {
        final int startIndex = e.getIndex0();
        final int endIndex = e.getIndex1();
        assert startIndex <= endIndex;
        assert startIndex >= 0 && endIndex >= 0;
        for (int i = startIndex; i <= endIndex; i++) {
            final T value = this.model.get(i);
            this.worker.elementAdded(i, value);
        }
    }

    public void intervalRemoved(final ListDataEvent e) {
        final int startIndex = e.getIndex0();
        final int endIndex = e.getIndex1();
        assert startIndex <= endIndex;
        assert startIndex >= 0 && endIndex >= 0;
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
