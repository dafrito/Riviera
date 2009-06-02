package com.bluespot.swing;

import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class ViewStack {

    private SingleSelectionModel model;
    private final Container container;
    private final CardLayout layout;

    private final ChangeListener listener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            ViewStack.this.refresh();
        }

    };

    public ViewStack(JComponent container) {
        this(container, null);
    }
    
    public ViewStack(JComponent container, SingleSelectionModel model) {
        this.container = container;
        this.layout = new CardLayout();
        this.container.setLayout(this.layout);
        this.setModel(model);
    }

    public Container getParent() {
        return this.container;
    }

    public void setModel(SingleSelectionModel model) {
        if(this.model != null) {
            this.model.removeChangeListener(this.listener);
        }
        this.model = model;
        if(this.model != null) {
            this.model.addChangeListener(this.listener);
        }
        this.refresh();
    }

    public SingleSelectionModel getModel() {
        return this.model;
    }

    protected void refresh() {
        int index = this.getModel().getSelectedIndex();
        if(index < 0)
            return;
        this.layout.show(this.container, this.getName(index));
    }

    protected abstract String getName(int index);

}
