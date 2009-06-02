package com.bluespot.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class LockableList<T> implements Iterable<T> {

    private Deque<T> list = new ArrayDeque<T>();
    private Deque<T> queuedRemovals = new ArrayDeque<T>();
    private Deque<T> queuedAdditions = new ArrayDeque<T>();

    private boolean locked = false;

    public void add(T value) {
        if (this.list.contains(value))
            return;
        if (this.isLocked())
            this.queuedAdditions.add(value);
        else
            this.list.add(value);
    }

    public void remove(T value) {
        if (!this.list.contains(value))
            return;
        if (this.isLocked())
            this.queuedRemovals.remove(value);
        else
            this.list.remove(value);
    }

    public int size() {
        return this.list.size() + this.queuedAdditions.size() - this.queuedRemovals.size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
        this.flush();
    }

    protected void flush() {
        for (T listener : this.queuedRemovals)
            this.remove(listener);
        this.queuedRemovals.clear();
        for (T listener : this.queuedAdditions)
            this.add(listener);
        this.queuedAdditions.clear();
    }

    public Iterator<T> iterator() {
        return this.list.iterator();
    }

}
