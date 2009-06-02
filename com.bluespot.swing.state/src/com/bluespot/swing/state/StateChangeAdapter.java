package com.bluespot.swing.state;

import javax.swing.event.ChangeEvent;

public class StateChangeAdapter<T> implements StateChangeListener<T> {

	public void stateChanged(final ChangeEvent e) {
		// No-op implementation
	}

	public void stateChanging(final StateChangeEvent<T> e) {
		// No-op implementation
	}

}
