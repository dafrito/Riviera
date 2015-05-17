package collections.observable.state;

import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import collections.observable.list.ObservableList;

/**
 * A state model that observes a specified list for changes, attempting to have
 * a valid state as much as possible.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of state
 */
public class ObservableListStateModel<T> extends AbstractStateModel<T> {

	private final ObservableList<T> states;

	/**
	 * Constructs a new model using the specified list for available states
	 * 
	 * @param states
	 *            the list to use as a state list
	 */
	public ObservableListStateModel(final ObservableList<T> states) {
		this.states = states;
		this.states.addListDataListener(this.listDataListener);
	}

	private final ListDataListener listDataListener = new ListDataListener() {

		@Override
		public void contentsChanged(final ListDataEvent e) {
			ObservableListStateModel.this.contentsChanged();
		}

		@Override
		public void intervalAdded(final ListDataEvent e) {
			ObservableListStateModel.this.intervalAdded(e);
		}

		@Override
		public void intervalRemoved(final ListDataEvent e) {
			ObservableListStateModel.this.intervalRemoved(e);
		}

	};

	/**
	 * Fired whenever this model's list changes dramatically enough that an
	 * interval cannot sufficiently describe the change.
	 * <p>
	 * In this case, the state remains at the same index, but this isn't
	 * guaranteed to be the same element.
	 * 
	 * @see ListDataListener#contentsChanged(ListDataEvent)
	 */
	protected void contentsChanged() {
		if (!this.isSelected()) {
			return;
		}
		// Reset our selection by setting via the index
		this.setSelectedIndex(this.getSelectedIndex());
	}

	/**
	 * Fired whenever this model's list has added elements
	 * 
	 * @param e
	 *            the event
	 * @see ListDataListener#intervalAdded(ListDataEvent)
	 */
	protected void intervalAdded(final ListDataEvent e) {
		if (!this.isSelected()) {
			// Set the state to the first element added.
			this.setSelectedIndex(e.getIndex0());
			return;
		}
		if (this.selectedIndex >= e.getIndex0()) {
			/*
			 * If we're after the added elements, we'll need to adjust our index
			 * by the size of the change. Since getIndex1() is inclusive, we'll
			 * need to add 1 to get the true size; all events always have at
			 * least one affected element.
			 */
			this.selectedIndex += e.getIndex1() - e.getIndex0() + 1;
		}
	}

	/**
	 * Fired whenever this model's list has removed elements
	 * 
	 * @param e
	 *            the event
	 * @see ListDataListener#intervalRemoved(ListDataEvent)
	 */
	protected void intervalRemoved(final ListDataEvent e) {
		if (this.selectedIndex < e.getIndex0()) {
			/*
			 * Our index is before the changed values, so we're not affected by
			 * this removal.
			 */
			return;
		}
		if (this.selectedIndex > e.getIndex1()) {
			/*
			 * Our index is after the removal, so we need to adjust our index by
			 * the size of the removed elements. Since getIndex1() is inclusive,
			 * we'll need to add 1 to get the true size; "add"events always have
			 * at least one affected element.
			 */
			this.selectedIndex -= e.getIndex1() - e.getIndex0() + 1;
			return;
		}
		/*
		 * Our selection was removed in this event, so we need to find a new one
		 */
		if (e.getIndex0() == 0) {
			// All elements were removed, so clear the selection
			this.clearSelection();
			return;
		}
		this.setSelectedIndex(e.getIndex0());
	}

	@Override
	public List<T> getStates() {
		return this.states;
	}

}
