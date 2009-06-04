package com.bluespot.collections.observable.list;

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
	public void add(final int index, final T element) {
		this.model.add(index, element);
	}

	@Override
	public boolean add(final T e) {
		this.model.addElement(e);
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends T> c) {
		for (final T element : c) {
			this.model.addElement(element);
		}
		return true;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends T> c) {
		int i = index;
		for (final T element : c) {
			this.model.add(i++, element);
		}
		return true;
	}

	@Override
	public void addListDataListener(final ListDataListener l) {
		this.model.addListDataListener(l);
	}

	@Override
	public void clear() {
		this.model.clear();
	}

	@Override
	public boolean contains(final Object o) {
		return this.model.contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		for (final Object e : c) {
			if (!this.model.contains(e)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(final int index) {
		return (T) this.model.getElementAt(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getElementAt(final int index) {
		return (T) this.model.getElementAt(index);
	}

	@Override
	public int getSize() {
		return this.model.getSize();
	}

	@Override
	public int indexOf(final Object o) {
		return this.model.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return this.model.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) Arrays.asList(this.model.toArray()).iterator();
	}

	@Override
	public int lastIndexOf(final Object o) {
		return this.model.lastIndexOf(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListIterator<T> listIterator() {
		return (ListIterator<T>) Arrays.asList(this.model.toArray()).iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListIterator<T> listIterator(final int index) {
		return (ListIterator<T>) Arrays.asList(this.model.toArray()).listIterator(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T remove(final int index) {
		return (T) this.model.remove(index);
	}

	@Override
	public boolean remove(final Object o) {
		return this.model.removeElement(o);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		boolean changed = false;
		for (final Object e : c) {
			changed = this.model.removeElement(e) || changed;
		}
		return changed;
	}

	@Override
	public void removeListDataListener(final ListDataListener l) {
		this.model.removeListDataListener(l);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		boolean changed = false;
		for (int i = 0; i < this.model.size(); i++) {
			final Object e = this.get(i);
			if (!c.contains(e)) {
				this.remove(i--);
				changed = true;
			}
		}
		return changed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T set(final int index, final T element) {
		return (T) this.model.set(index, element);
	}

	@Override
	public int size() {
		return this.model.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> subList(final int fromIndex, final int toIndex) {
		return (List<T>) Arrays.asList(this.model.toArray()).subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return this.model.toArray();
	}

	@Override
	public <E> E[] toArray(final E[] a) {
		return Arrays.asList(this.model.toArray()).toArray(a);
	}

}
