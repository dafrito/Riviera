package com.bluespot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListProxy<E> implements List<E> {
    private List<E> list;

    public ListProxy(List<E> list) {
        this.setSourceList(list);
    }

    public List<E> getSourceList() {
        return this.list;
    }

    public void setSourceList(List<E> list) {
        this.list = list;
    }

    public boolean add(E e) {
        return this.list.add(e);
    }

    public void add(int index, E element) {
        this.list.add(index, element);
    }

    public boolean addAll(Collection<? extends E> c) {
        return this.list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return this.list.addAll(index, c);
    }

    public void clear() {
        this.list.clear();
    }

    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.list.containsAll(c);
    }

    public E get(int index) {
        return this.list.get(index);
    }

    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public Iterator<E> iterator() {
        return this.list.iterator();
    }

    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return this.list.listIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return this.list.listIterator(index);
    }

    public boolean remove(Object o) {
        return this.list.remove(o);
    }

    public E remove(int index) {
        return this.list.remove(index);
    }

    public boolean removeAll(Collection<?> c) {
        return this.list.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.list.retainAll(c);
    }

    public E set(int index, E element) {
        return this.list.set(index, element);
    }

    public int size() {
        return this.list.size();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.list.toArray(a);
    }

    @Override
    public boolean equals(Object other) {
        return this.list.equals(other);
    }

}
