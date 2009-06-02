package com.bluespot.ide;

import javax.swing.JComponent;


public class SimplePerspective extends AbstractPerspective {
    private final JComponent component;

    public SimplePerspective(String name, JComponent component) {
        super(name);
        this.component = component;
    }

    public JComponent getComponent() {
        return this.component;
    }

}
