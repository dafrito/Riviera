package com.bluespot.collections.observable.state;

import javax.swing.event.ChangeListener;

/**
 * A listener for {@link StateModel} objects.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of state in the {@code StateModel}
 */
public interface StateChangeListener<T> extends ChangeListener {

	/**
	 * This method is invoked whenever the state is about to change. The
	 * specified event describes the change.
	 * 
	 * @param event
	 *            the event that describes the state change
	 */
	void stateChanging(StateChangeEvent<T> event);
}
