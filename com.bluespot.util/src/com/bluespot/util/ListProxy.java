package com.bluespot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListProxy<E> implements List<E> {
	private List<E> list;

	public ListProxy(final List<E> list) {
		this.setSourceList(list);
	}

	public boolean add(final E e) {
		return this.list.add(e);
	}

	public void add(final int index, final E element) {
		this.list.add(index, element);
	}

	public boolean addAll(final Collection<? extends E> c) {
		return this.list.addAll(c);
	}

	public boolean addAll(final int index, final Collection<? extends E> c) {
		return this.list.addAll(index, c);
	}

	public void clear() {
		this.list.clear();
	}

	public boolean contains(final Object o) {
		return this.list.contains(o);
	}

	public boolean containsAll(final Collection<?> c) {
		return this.list.containsAll(c);
	}

	@Override
	public boolean equals(final Object other) {
		return this.list.equals(other);
	}

	public E get(final int index) {
		return this.list.get(index);
	}

	public List<E> getSourceList() {
		return this.list;
	}

	public int indexOf(final Object o) {
		return this.list.indexOf(o);
	}

	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	public Iterator<E> iterator() {
		return this.list.iterator();
	}

	public int lastIndexOf(final Object o) {
		return this.list.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return this.list.listIterator();
	}

	public ListIterator<E> listIterator(final int index) {
		return this.list.listIterator(index);
	}

	public E remove(final int index) {
		return this.list.remove(index);
	}

	public boolean remove(final Object o) {
		return this.list.remove(o);
	}

	public boolean removeAll(final Collection<?> c) {
		return this.list.removeAll(c);
	}

	public boolean retainAll(final Collection<?> c) {
		return this.list.retainAll(c);
	}

	public E set(final int index, final E element) {
		return this.list.set(index, element);
	}

	public void setSourceList(final List<E> list) {
		this.list = list;
	}

	public int size() {
		return this.list.size();
	}

	public List<E> subList(final int fromIndex, final int toIndex) {
		return this.list.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return this.list.toArray();
	}

	public <T> T[] toArray(final T[] a) {
		return this.list.toArray(a);
	}

}
