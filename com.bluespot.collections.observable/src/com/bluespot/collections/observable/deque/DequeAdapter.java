package com.bluespot.collections.observable.deque;

public class DequeAdapter<E> implements DequeListener<E> {

	public void dequeChanged() {
		// Do nothing
	}

	public void firstElementAdded(final E oldFirstElement) {
		// Do nothing
	}

	public void firstElementRemoved(final E oldFirstElement) {
		// Do nothing
	}

	public void lastElementAdded(final E oldLastElement) {
		// Do nothing
	}

	public void lastElementRemoved(final E oldLastElement) {
		// Do nothing
	}

}
