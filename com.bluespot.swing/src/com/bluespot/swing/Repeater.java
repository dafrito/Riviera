package com.bluespot.swing;

import java.awt.Component;
import java.awt.Container;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Synchronizes the children of a container with a ListModel.
 * <p>
 * As elements are added to the ListModel, each element is represented as a
 * Component rendered by this Repeater. The Repeater's <tt>createComponent</tt>
 * method must return a unique component per element. In other words, this is
 * not like a CellRenderer.
 * 
 * @author Aaron Faanes
 * @param <C> The type of the children this view will create
 * @param <E> The type that the listModel contains
 */
public abstract class Repeater<C extends Component, E> {

    private final Container parent;
    private final ListModel listModel;

    /**
     * Creates a Repeater that will populate the given parent with data
     * from the given ListModel.
     * <p>
     * Notice that all children in the parent will be removed.
     * 
     * @param parent The parent container to populate
     * @param model The model that will provide the data to populate the parent
     */
    public Repeater(Container parent, ListModel model) {
        this.listModel = model;
        this.listModel.addListDataListener(this.listener);
        this.parent = parent;
        this.refresh();
    }

    public Container getParent() {
        return this.parent;
    }

    public ListModel getModel() {
        return this.listModel;
    }

    /**
     * Returns a component that represents the given data.
     * 
     * @param data The data that the component should represent
     * @return The component that represents the data.
     */
    public abstract C createComponent(E data);
    
    /**
     * Adds the given component to our parent.
     * 
     * @param childComponent The component to be added
     * @param index The index where the component should be added
     * @param childValue The data that the component represents
     */
    protected void addComponent(C childComponent, int index, E childValue) {
        this.getParent().add(childComponent, childValue);
    }

    /**
     * Removes the given component from our parent.
     * 
     * @param childComponent The component that is being removed
     * @param index The index of the component
     */
    protected void removeComponent(C childComponent, int index) {
        this.getParent().remove(index);
    }

    public final void refresh() {
        Component[] components = this.getParent().getComponents();
        for(int i = components.length; components.length > 0 && i >= 0; i--) {
            this.removeComponent(this.getComponentAt(i), i);
        }
        for(int i = 0; i < this.listModel.getSize(); i++) {
            C component = this.createComponent(this.getElementAt(i));
            this.addComponent(component, i, this.getElementAt(i));
        }
    }

    @SuppressWarnings("unchecked")
    public E getElementAt(int index) {
        return (E)this.getModel().getElementAt(index);
    }
    

    @SuppressWarnings("unchecked")
    public C getComponentAt(int index) {
        return (C)this.getParent().getComponent(index);
    }

    /**
     * The listener that we attach to our ListModel. We have it enclosed in our
     * class so that this view can't be used by multiple ListModels.
     */
    private ListDataListener listener = new ListDataListener() {

        public void contentsChanged(ListDataEvent e) {
            this.intervalRemoved(e);
            this.intervalAdded(e);
        }

        public void intervalAdded(ListDataEvent e) {
            for(int index = e.getIndex0(); index <= e.getIndex1(); index++) {
                E childValue = Repeater.this.getElementAt(index);
                C childComponent = Repeater.this.createComponent(childValue);
                Repeater.this.addComponent(childComponent, index, childValue);
            }
        }

        public void intervalRemoved(ListDataEvent e) {
            for(int index = e.getIndex1(); index >= e.getIndex0(); index--) {
                C childComponent = Repeater.this.getComponentAt(index);
                Repeater.this.removeComponent(childComponent, index);
            }
        }

    };

}
