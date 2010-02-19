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
