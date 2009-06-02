package com.bluespot.swing.state;

import javax.swing.event.ChangeEvent;

public class StateChangeEvent<T> extends ChangeEvent {

	private final T oldState;
	private final T newState;
	private final StateModel<T> stateModel;

	public StateChangeEvent(StateModel<T> stateModel, T oldState, T newState) {
		super(stateModel);
		this.stateModel = stateModel;
		this.oldState = oldState;
		this.newState = newState;
	}

	public StateModel<T> getStateModel() {
		return this.stateModel;
	}

	public T getOldState() {
		return this.oldState;
	}

	public T getNewState() {
		return this.newState;
	}

}
