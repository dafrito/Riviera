package com.bluespot.dispatcher;

public interface Dispatchable<E, L> {
    public void dispatch(E value, L listener);
}
