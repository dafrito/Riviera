package com.bluespot.swing.state;

import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.bluespot.dispatcher.SimpleDispatcher;
import com.bluespot.swing.list.TypedListModel;

public class StateModel<T> implements SingleSelectionModel {

	private final ListDataListener listDataListener = new ListDataListener() {

		public void contentsChanged(ListDataEvent e) {
			StateModel.this.setSelectedIndex(StateModel.this.getSelectedIndex());
		}

		public void intervalAdded(ListDataEvent e) {
			int selected = StateModel.this.getSelectedIndex();
			if (selected < 0) {
				StateModel.this.setSelectedIndex(0);
			} else if (selected >= e.getIndex0()) {
				StateModel.this.selectedIndex += e.getIndex1() - e.getIndex0() + 1;
			}
		}

		public void intervalRemoved(ListDataEvent e) {
			int selected = StateModel.this.getSelectedIndex();
			if (selected >= e.getIndex0() && selected <= e.getIndex1()) {
				StateModel.this.setSelectedIndex(Math.min(StateModel.this.getStates().getSize() - 1, e.getIndex0()));
			}
		}

	};

	private TypedListModel<T> states;
	protected T state;
	protected int selectedIndex = -1;

	public StateModel(TypedListModel<T> states) {
		this.setStates(states);
	}

	public TypedListModel<T> getStates() {
		return this.states;
	}

	public void setStates(TypedListModel<T> states) {
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

	public T getState() {
		return this.state;
	}

	public void setState(T state) {
		if (this.state == state)
			return;
		if (this.state != null && this.state.equals(state))
			return;
		int index = this.indexOf(state);
		if (index < 0) {
			throw new IllegalArgumentException("State provided is not in the list of possible states");
		}
		StateChangeEvent<T> event = new StateChangeEvent<T>(this, this.getState(), state);
		this.stateChangeDispatcher.dispatch(event);
		this.selectedIndex = index;
		this.state = state;
		this.changeDispatcher.dispatch(event);
	}

	public void clearSelection() {
		this.setState(null);
	}

	protected int indexOf(T testState) {
		for (int i = 0; i < this.getStates().getSize(); i++) {
			T element = this.getStates().getElementAt(i);
			if (element == testState || (element != null && element.equals(testState)))
				return i;
		}
		return -1;
	}

	protected boolean contains(T testState) {
		return this.indexOf(testState) >= 0;
	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	public boolean isSelected() {
		return this.contains(this.getState());
	}

	public void setSelectedIndex(int index) {
		if (index < 0) {
			this.setState(null);
			return;
		}
		this.setState(this.getStates().getElementAt(index));
	}

	private final SimpleDispatcher<ChangeEvent, ChangeListener> changeDispatcher = new SimpleDispatcher<ChangeEvent, ChangeListener>() {

		public void dispatch(ChangeEvent value, ChangeListener listener) {
			listener.stateChanged(value);
		}

	};

	private final SimpleDispatcher<StateChangeEvent<T>, StateChangeListener<T>> stateChangeDispatcher = new SimpleDispatcher<StateChangeEvent<T>, StateChangeListener<T>>() {

		public void dispatch(StateChangeEvent<T> value, StateChangeListener<T> listener) {
			listener.stateChanging(value);
		}

	};

	public void addChangeListener(ChangeListener listener) {
		this.changeDispatcher.addListener(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		this.changeDispatcher.removeListener(listener);
	}

	public void addStateChangeListener(StateChangeListener<T> listener) {
		this.stateChangeDispatcher.addListener(listener);
		this.changeDispatcher.addListener(listener);
	}

	public void removeStateChangeListener(StateChangeListener<T> listener) {
		this.stateChangeDispatcher.removeListener(listener);
		this.changeDispatcher.removeListener(listener);
	}

}
