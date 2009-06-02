package com.bluespot.swing.state;

import javax.swing.event.ChangeListener;

public interface StateChangeListener<T> extends ChangeListener {
	void stateChanging(StateChangeEvent<T> e);
}
