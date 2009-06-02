package com.bluespot.dispatcher;

public class StatefulDispatcher<E, L> extends AbstractDispatcher<E, L> {
    
    @Override
    public void dispatch(Dispatchable<E,L> dispatchable, E value) {
        super.dispatch(dispatchable, value);
    }
    
}
