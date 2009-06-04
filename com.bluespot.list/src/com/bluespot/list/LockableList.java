package com.bluespot.list;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class LockableList<T> implements Iterable<T> {

	private final Deque<T> list = new ArrayDeque<T>();
	private boolean locked = false;
	private final Deque<T> queuedAdditions = new ArrayDeque<T>();

	private final Deque<T> queuedRemovals = new ArrayDeque<T>();

	public void add(final T value) {
		if (this.list.contains(value)) {
			return;
		}
		if (this.isLocked()) {
			this.queuedAdditions.add(value);
		} else {
			this.list.add(value);
		}
	}

	public boolean isEmpty() {
		return this.size() == 0;
	}

	public boolean isLocked() {
		return this.locked;
	}

	public Iterator<T> iterator() {
		return this.list.iterator();
	}

	public void lock() {
		this.locked = true;
	}

	public void remove(final T value) {
		if (!this.list.contains(value)) {
			return;
		}
		if (this.isLocked()) {
			this.queuedRemovals.remove(value);
		} else {
			this.list.remove(value);
		}
	}

	public int size() {
		return this.list.size() + this.queuedAdditions.size() - this.queuedRemovals.size();
	}

	public void unlock() {
		this.locked = false;
		this.flush();
	}

	protected void flush() {
		for (final T listener : this.queuedRemovals) {
			this.remove(listener);
		}
		this.queuedRemovals.clear();
		for (final T listener : this.queuedAdditions) {
			this.add(listener);
		}
		this.queuedAdditions.clear();
	}

}
