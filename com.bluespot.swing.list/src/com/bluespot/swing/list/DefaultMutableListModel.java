package com.bluespot.swing.list;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;


public class DefaultMutableListModel<T> implements MutableListModel<T> {
    
    private final DefaultListModel model;

    public DefaultMutableListModel() {
        this.model = new DefaultListModel();
    }

    @Override
    public boolean add(T e) {
        this.model.addElement(e);
        return true;
    }

    @Override
    public void add(int index, T element) {
        this.model.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for(T element : c) {
            this.model.addElement(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        int i = index;
        for(T element : c) {
            this.model.add(i++, element);
        }
        return true;
    }

    @Override
    public void clear() {
        this.model.clear();
    }

    @Override
    public boolean contains(Object o) {
        return this.model.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object e : c) {
            if(!this.model.contains(e))
                return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        return (T)this.model.getElementAt(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.model.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return this.model.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>)Arrays.asList(this.model.toArray()).iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
       return this.model.lastIndexOf(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator<T> listIterator() {
        return (ListIterator<T>)Arrays.asList(this.model.toArray()).iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator<T> listIterator(int index) {
        return (ListIterator<T>)Arrays.asList(this.model.toArray()).listIterator(index);
    }

    @Override
    public boolean remove(Object o) {
        return this.model.removeElement(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T remove(int index) {
        return (T)this.model.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for(Object e : c) {
            changed = this.model.removeElement(e) || changed;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for(int i = 0; i < this.model.size(); i++) {
            Object e = this.get(i);
            if(!c.contains(e)) {
                this.remove(i--);
                changed = true;
            }
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T set(int index, T element) {
        return (T)this.model.set(index, element);
    }

    @Override
    public int size() {
        return this.model.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return (List<T>)Arrays.asList(this.model.toArray()).subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return this.model.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return Arrays.asList(this.model.toArray()).toArray(a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getElementAt(int index) {
        return (T)this.model.getElementAt(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        this.model.addListDataListener(l);
    }

    @Override
    public int getSize() {
        return this.model.getSize();
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        this.model.removeListDataListener(l);
    }

}
