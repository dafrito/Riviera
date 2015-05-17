package collections.observable.state;

import javax.swing.event.ChangeEvent;

/**
 * A no-op {@link StateChangeListener} implementation.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of state in the observed model
 */
public class StateChangeAdapter<T> implements StateChangeListener<T> {

	@Override
	public void stateChanged(final ChangeEvent e) {
		// No-op implementation
	}

	@Override
	public void stateChanging(final StateChangeEvent<T> e) {
		// No-op implementation
	}

}
