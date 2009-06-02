package com.bluespot.dispatcher;

public class StatefulDispatcher<E, L> extends AbstractDispatcher<E, L> {

	@Override
	public void dispatch(final Dispatchable<E, L> dispatchable, final E value) {
		super.dispatch(dispatchable, value);
	}

}
