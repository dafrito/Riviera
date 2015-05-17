package collections.observable.state;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Represents a skeletal implementation of a state model.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of state this model contains
 */
public abstract class AbstractStateModel<T> implements StateModel<T> {

	/**
	 * The currently selected index. Will be {@code -1} if no state is selected.
	 */
	protected int selectedIndex = -1;

	/**
	 * The currently selected state.
	 */
	protected T state;

	private final List<ChangeListener> changeListeners = new CopyOnWriteArrayList<ChangeListener>();

	private final List<StateChangeListener<T>> stateChangeListeners = new CopyOnWriteArrayList<StateChangeListener<T>>();

	@Override
	public T getState() {
		return this.state;
	}

	private boolean isEqualState(final T otherState) {
		return this.getState() != null ? this.getState().equals(otherState) : otherState == null;
	}

	@Override
	public boolean changeState(final T newState) {
		if (this.isEqualState(newState)) {
			// Changing to the same state, so silently return
			return false;
		}
		final int index = this.getStates().indexOf(newState);
		if (index == -1 && newState != null) {
			throw new IllegalArgumentException("State provided is not in the list of possible states");
		}
		final StateChangeEvent<T> event = new StateChangeEvent<T>(this, this.getState(), newState);
		this.fireStateChangingEvent(event);
		this.doStateChange(newState, index);
		this.fireStateChangedEvent(event);
		return true;
	}

	/**
	 * Actually perform a state change.
	 * 
	 * @param newState
	 *            the new state
	 * @param stateIndex
	 *            the index of the state
	 */
	protected void doStateChange(final T newState, final int stateIndex) {
		this.state = newState;
		this.selectedIndex = stateIndex;
	}

	@Override
	public void clearSelection() {
		this.changeState(null);
	}

	@Override
	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	@Override
	public void setSelectedIndex(final int index) {
		if (this.selectedIndex == index) {
			return;
		}
		this.doStateChange(this.getStates().get(index), index);
	}

	@Override
	public boolean isSelected() {
		return this.getStates().contains(this.getState());
	}

	@Override
	public void addChangeListener(final ChangeListener listener) {
		this.changeListeners.add(listener);
	}

	@Override
	public void removeChangeListener(final ChangeListener listener) {
		this.changeListeners.remove(listener);
	}

	@Override
	public void addStateChangeListener(final StateChangeListener<T> listener) {
		this.addChangeListener(listener);
		this.stateChangeListeners.add(listener);
	}

	@Override
	public void removeStateChangeListener(final StateChangeListener<T> listener) {
		this.removeChangeListener(listener);
		this.stateChangeListeners.remove(listener);
	}

	/**
	 * This method should be invoked immediately before the state is actually
	 * changed.
	 * 
	 * @param event
	 *            the event representing the state change
	 */
	protected void fireStateChangedEvent(final ChangeEvent event) {
		for (final ChangeListener listener : this.changeListeners) {
			listener.stateChanged(event);
		}
	}

	/**
	 * This method should be invoked immediately after the state has been
	 * changed.
	 * 
	 * @param event
	 *            the event representing the state change
	 */
	protected void fireStateChangingEvent(final StateChangeEvent<T> event) {
		for (final StateChangeListener<T> listener : this.stateChangeListeners) {
			listener.stateChanging(event);
		}
	}

}
