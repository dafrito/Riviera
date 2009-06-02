package com.bluespot.swing;

import java.awt.Component;

import javax.swing.JTabbedPane;
import javax.swing.ListModel;


public abstract class TabView<C extends Component, E> extends NamingRepeater<C, E> {

    private int unnamedCounter = 1;

    public TabView(JTabbedPane parent, ListModel model) {
        super(parent, model);
    }

    @Override
    protected void nameChanged(String name, C childComponent, int index, E childValue) {
        TabView.this.getTabbedPane().setTitleAt(
            index,
            name != null && !name.trim().equals("") ? name : "Untitled " + this.unnamedCounter++);
    }

    public JTabbedPane getTabbedPane() {
        return (JTabbedPane)this.getParent();
    }

    public void select(C view) {
        int index = Components.getIndexOf(this.getParent(), view);
        if(index < 0)
            throw new IllegalArgumentException("View is not contained in this TabView");
        this.getTabbedPane().setSelectedIndex(index);
    }

}
