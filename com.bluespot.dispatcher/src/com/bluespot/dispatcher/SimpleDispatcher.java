package com.bluespot.dispatcher;

public abstract class SimpleDispatcher<E, L> extends AbstractDispatcher<E, L> implements Dispatchable<E, L> {

	public void dispatch(final E value) {
		this.dispatch(this, value);
	}

}
