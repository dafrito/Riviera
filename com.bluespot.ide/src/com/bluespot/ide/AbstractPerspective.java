package com.bluespot.ide;

import javax.swing.JMenuBar;

public abstract class AbstractPerspective implements Perspective {

    private final String name;

    public AbstractPerspective(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void populateMenuBar(JMenuBar menuBar) {
        // No-op implementation
    }

    public void close() {
        // No-op implementation
    }

    public boolean isReadyForClose() {
        return true;
    }

}
