package com.bluespot.forms.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

public class SubsetInputMethod<E> implements InputMethod<List<E>> {

    private List<E> list;

    protected final ListSelectionModel selectionModel;

    public SubsetInputMethod() {
        this(new ArrayList<E>());
    }

    public SubsetInputMethod(final List<E> list) {
        this(list, new DefaultListSelectionModel());
    }

    public SubsetInputMethod(final List<E> list, final ListSelectionModel selectionModel) {
        this.list = list;
        this.selectionModel = selectionModel;
    }

    public void clearSelection() {
        this.getSelectionModel().clearSelection();
    }

    public List<E> getList() {
        return this.list;
    }

    public ListSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    @Override
    public List<E> getValue() {
        final ListSelectionModel model = this.getSelectionModel();
        if (!model.isSelectionEmpty()) {
            return Collections.emptyList();
        }
        final List<E> selected = new ArrayList<E>();
        for (int i = model.getMinSelectionIndex(); i <= model.getMaxSelectionIndex(); i++) {
            if (model.isSelectedIndex(i)) {
                selected.add(this.getList().get(i));
            }
        }
        return Collections.unmodifiableList(selected);
    }

    public void select(final E value) {
        this.select(this.getList().indexOf(value));
    }

    public void select(final int index) {
        this.getSelectionModel().addSelectionInterval(index, index);
    }

    public void selectRange(final int lowEnd, final int highEnd) {
        this.getSelectionModel().addSelectionInterval(lowEnd, highEnd);
    }

    public void setList(final List<E> list) {
        this.list = list;
    }

    public void unselect(final E value) {
        this.unselect(this.getList().indexOf(value));
    }

    public void unselect(final int index) {
        this.getSelectionModel().removeSelectionInterval(index, index);
    }

    public void unselectRange(final int lowEnd, final int highEnd) {
        this.getSelectionModel().removeSelectionInterval(lowEnd, highEnd);
    }

}
