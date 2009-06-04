package com.bluespot.collections.observable.state;

import javax.swing.event.ChangeEvent;

public class StateChangeEvent<T> extends ChangeEvent {

	private final T newState;
	private final T oldState;
	private final StateModel<T> stateModel;

	public StateChangeEvent(final StateModel<T> stateModel, final T oldState, final T newState) {
		super(stateModel);
		this.stateModel = stateModel;
		this.oldState = oldState;
		this.newState = newState;
	}

	public T getNewState() {
		return this.newState;
	}

	public T getOldState() {
		return this.oldState;
	}

	public StateModel<T> getStateModel() {
		return this.stateModel;
	}

}
