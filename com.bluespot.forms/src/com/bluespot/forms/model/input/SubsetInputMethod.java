package com.bluespot.forms.model.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

public class SubsetInputMethod<E> extends InputMethod<List<E>> {

    protected final ListSelectionModel selectionModel;

    private List<E> list;

    public SubsetInputMethod() {
        this(new ArrayList<E>());
    }
    
    public SubsetInputMethod(List<E> list) {
        this(list, new DefaultListSelectionModel());
    }
    
    public SubsetInputMethod(List<E> list, ListSelectionModel selectionModel) {
        this.list = list;
        this.selectionModel = selectionModel;
    }

    public List<E> getList() {
        return this.list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
    
    public ListSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    public void selectRange(int lowEnd, int highEnd) {
        this.getSelectionModel().addSelectionInterval(lowEnd, highEnd);
    }

    public void unselect(int index) {
        this.getSelectionModel().removeSelectionInterval(index, index);
    }

    public void unselect(E value) {
        this.unselect(this.getList().indexOf(value));
    }

    public void select(int index) {
        this.getSelectionModel().addSelectionInterval(index, index);
    }

    public void select(E value) {
        this.select(this.getList().indexOf(value));
    }

    public void unselectRange(int lowEnd, int highEnd) {
        this.getSelectionModel().removeSelectionInterval(lowEnd, highEnd);
    }
    
    public void clearSelection() {
        this.getSelectionModel().clearSelection();
    }

    @Override
    public List<E> getValue() {
        ListSelectionModel model = this.getSelectionModel();
        if(!model.isSelectionEmpty())
            return Collections.emptyList();
        List<E> selected = new ArrayList<E>();
        for(int i = model.getMinSelectionIndex(); i <= model.getMaxSelectionIndex(); i++) {
            if(model.isSelectedIndex(i))
                selected.add(this.getList().get(i));
        }
        return Collections.unmodifiableList(selected);
    }

}
