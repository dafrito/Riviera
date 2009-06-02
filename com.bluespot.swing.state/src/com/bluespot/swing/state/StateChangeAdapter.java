package com.bluespot.swing.state;


import javax.swing.event.ChangeEvent;

public class StateChangeAdapter<T> implements StateChangeListener<T> {

    public void stateChanging(StateChangeEvent<T> e) {
        // No-op implementation
    }

    public void stateChanged(ChangeEvent e) {
        // No-op implementation
    }

}
