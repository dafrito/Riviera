package collections.observable.state;

import javax.swing.event.ChangeEvent;

/**
 * An event that is dispatched whenever a {@link StateModel} object's state has
 * changed.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of state in this event's source {@code StateModel}
 * @see StateChangeListener
 */
public class StateChangeEvent<T> extends ChangeEvent {

	private static final long serialVersionUID = 3280366246760970006L;

	private final T newState;
	private final T oldState;
	private final StateModel<T> stateModel;

	/**
	 * Constructs a new event describing the transition from the specified old
	 * state to the specified new state.
	 * 
	 * @param stateModel
	 *            the source of this event
	 * @param oldState
	 *            the old state
	 * @param newState
	 *            the new state that has been changed to
	 */
	public StateChangeEvent(final StateModel<T> stateModel, final T oldState, final T newState) {
		super(stateModel);
		this.stateModel = stateModel;
		this.oldState = oldState;
		this.newState = newState;
	}

	/**
	 * Returns the new state of this event's {@code StateModel}.
	 * 
	 * @return the new state
	 */
	public T getNewState() {
		return this.newState;
	}

	/**
	 * Returns the last state of this event's {@code StateModel}.
	 * 
	 * @return the last state
	 */
	public T getOldState() {
		return this.oldState;
	}

	/**
	 * Returns the source of this event
	 * 
	 * @return the source {@link StateModel} object
	 */
	public StateModel<T> getStateModel() {
		return this.stateModel;
	}

}
