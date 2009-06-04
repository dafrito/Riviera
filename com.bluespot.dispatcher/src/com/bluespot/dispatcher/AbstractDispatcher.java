package com.bluespot.dispatcher;

import com.bluespot.list.LockableList;

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

	public void addListener(final L listener) {
		this.listeners.add(listener);
	}

	public boolean hasListeners() {
		return !this.listeners.isEmpty();
	}

	public void removeListener(final L listener) {
		this.listeners.remove(listener);
	}

	protected void dispatch(final Dispatchable<E, L> dispatchable, final E value) {
		this.listeners.lock();
		try {
			for (final L listener : this.listeners) {
				dispatchable.dispatch(value, listener);
			}
		} finally {
			this.listeners.unlock();
		}
	}

}
