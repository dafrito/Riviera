package com.bluespot.dispatcher;

import com.bluespot.util.LockableList;

/**
 * Dispatches some value E to all interested parties of type L. The
 * implementation of this dispatch is left to subclasses through void dispatch(E
 * value, L listener). If you're interested in dispatching a result in different
 * ways, use StatefulDispatcher instead of this class.
 * 
 * @author Aaron Faanes
 * @param <E>
 *            The type that is dispatcher
 * @param <L>
 *            The type of the interested party
 * @see StatefulDispatcher
 */
public abstract class AbstractDispatcher<E, L> {

	LockableList<L> listeners = new LockableList<L>();

	public void addListener(L listener) {
		this.listeners.add(listener);
	}

	public void removeListener(L listener) {
		this.listeners.remove(listener);
	}

	public boolean hasListeners() {
		return !this.listeners.isEmpty();
	}

	protected void dispatch(Dispatchable<E, L> dispatchable, E value) {
		this.listeners.lock();
		try {
			for (L listener : this.listeners) {
				dispatchable.dispatch(value, listener);
			}
		} finally {
			this.listeners.unlock();
		}
	}

}
