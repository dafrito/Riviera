package com.bluespot.collections.observable.state;

import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.bluespot.collections.observable.list.TypedListModel;
import com.bluespot.dispatcher.SimpleDispatcher;

public class StateModel<T> implements SingleSelectionModel {

	private final SimpleDispatcher<ChangeEvent, ChangeListener> changeDispatcher = new SimpleDispatcher<ChangeEvent, ChangeListener>() {

		public void dispatch(final ChangeEvent value, final ChangeListener listener) {
			listener.stateChanged(value);
		}

	};

	private final ListDataListener listDataListener = new ListDataListener() {

		public void contentsChanged(final ListDataEvent e) {
			StateModel.this.setSelectedIndex(StateModel.this.getSelectedIndex());
		}

		public void intervalAdded(final ListDataEvent e) {
			final int selected = StateModel.this.getSelectedIndex();
			if (selected < 0) {
				StateModel.this.setSelectedIndex(0);
			} else if (selected >= e.getIndex0()) {
				StateModel.this.selectedIndex += e.getIndex1() - e.getIndex0() + 1;
			}
		}

		public void intervalRemoved(final ListDataEvent e) {
			final int selected = StateModel.this.getSelectedIndex();
			if (selected >= e.getIndex0() && selected <= e.getIndex1()) {
				StateModel.this.setSelectedIndex(Math.min(StateModel.this.getStates().getSize() - 1, e.getIndex0()));
			}
		}

	};
	private final SimpleDispatcher<StateChangeEvent<T>, StateChangeListener<T>> stateChangeDispatcher = new SimpleDispatcher<StateChangeEvent<T>, StateChangeListener<T>>() {

		public void dispatch(final StateChangeEvent<T> value, final StateChangeListener<T> listener) {
			listener.stateChanging(value);
		}

	};
	private TypedListModel<T> states;

	protected int selectedIndex = -1;

	protected T state;

	public StateModel(final TypedListModel<T> states) {
		this.setStates(states);
	}

	public void addChangeListener(final ChangeListener listener) {
		this.changeDispatcher.addListener(listener);
	}

	public void addStateChangeListener(final StateChangeListener<T> listener) {
		this.stateChangeDispatcher.addListener(listener);
		this.changeDispatcher.addListener(listener);
	}

	public void clearSelection() {
		this.setState(null);
	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	public T getState() {
		return this.state;
	}

	public TypedListModel<T> getStates() {
		return this.states;
	}

	public boolean isSelected() {
		return this.contains(this.getState());
	}

	public void removeChangeListener(final ChangeListener listener) {
		this.changeDispatcher.removeListener(listener);
	}

	public void removeStateChangeListener(final StateChangeListener<T> listener) {
		this.stateChangeDispatcher.removeListener(listener);
		this.changeDispatcher.removeListener(listener);
	}

	public void setSelectedIndex(final int index) {
		if (index < 0) {
			this.setState(null);
			return;
		}
		this.setState(this.getStates().getElementAt(index));
	}

	public void setState(final T state) {
		if (this.state == state) {
			return;
		}
		if (this.state != null && this.state.equals(state)) {
			return;
		}
		final int index = this.indexOf(state);
		if (index < 0) {
			throw new IllegalArgumentException("State provided is not in the list of possible states");
		}
		final StateChangeEvent<T> event = new StateChangeEvent<T>(this, this.getState(), state);
		this.stateChangeDispatcher.dispatch(event);
		this.selectedIndex = index;
		this.state = state;
		this.changeDispatcher.dispatch(event);
	}

	public void setStates(final TypedListModel<T> states) {
		if (this.states != null) {
			this.states.removeListDataListener(this.listDataListener);
		}
		this.states = states;
		if (this.states != null && this.states.getSize() > 0) {
			this.setState(this.states.getElementAt(0));
		} else {
			this.setState(null);
		}
		this.states.addListDataListener(this.listDataListener);
	}

	protected boolean contains(final T testState) {
		return this.indexOf(testState) >= 0;
	}

	protected int indexOf(final T testState) {
		for (int i = 0; i < this.getStates().getSize(); i++) {
			final T element = this.getStates().getElementAt(i);
			if (element == testState || (element != null && element.equals(testState))) {
				return i;
			}
		}
		return -1;
	}

}
