package com.bluespot.collections.observable.state;

import java.util.List;

import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeListener;

/**
 * A model representing an observable switch between several states. This
 * interface offers much of the same functionality as a
 * {@link SingleSelectionModel} but provides a more robust API for clients.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of state
 */
public interface StateModel<T> extends SingleSelectionModel {

    /**
     * Returns the list of available states.
     * 
     * @return the list of available states
     */
    public List<T> getStates();

    /**
     * Returns the currently selected state of this model.
     * 
     * @return the currently selected state
     */
    public T getState();

    /**
     * Sets the state of this model to the specified value, if that value is an
     * allowed state.
     * <p>
     * Before the state is changed, a
     * {@link StateChangeListener#stateChanging(StateChangeEvent)} event will be
     * fired. After the state is changed, a
     * {@link ChangeListener#stateChanged(javax.swing.event.ChangeEvent)} event
     * will be fired.
     * 
     * @param state
     *            the new state. If the specified state is equal to this model's
     *            current state, no change occurs and no listeners are fired
     * @return {@code true} if the state changed as a result of this call
     * @throws IllegalArgumentException
     *             if the specified state is not allowed
     */
    public boolean changeState(final T state);

    /**
     * Adds a {@link StateChangeListener} to this model. Note that this listener
     * will be implicitly added as a {@link ChangeListener}.
     * 
     * @param listener
     *            the listener to add
     */
    public void addStateChangeListener(final StateChangeListener<T> listener);

    /**
     * Removes the specified {@link StateChangeListener} from this model. This
     * will also remove it as a {@link ChangeListener}.
     * <p>
     * If the listener is not registered, no action is taken.
     * 
     * @param listener
     *            the listener to remove
     */
    public void removeStateChangeListener(final StateChangeListener<T> listener);

}
