package com.bluespot.reflection;

import java.util.Deque;

import com.bluespot.collections.observable.deque.DequeListener;
import com.bluespot.collections.observable.deque.ObservableDeque;

public class CallStack {
	private final ObservableDeque<CallStackFrame> callStack = new ObservableDeque<CallStackFrame>();

	public void update(final Deque<CallStackFrame> updatedCallStack) {

	}

	public void addDequeListener(final DequeListener<CallStackFrame> listener) {
		this.callStack.addDequeListener(listener);
	}

	public void removeDequeListener(final DequeListener<CallStackFrame> listener) {
		this.callStack.removeDequeListener(listener);
	}
}