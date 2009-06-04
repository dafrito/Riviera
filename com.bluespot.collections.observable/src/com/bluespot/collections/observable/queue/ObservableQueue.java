package com.bluespot.collections.observable.queue;

import java.util.Queue;

import com.bluespot.collections.proxies.QueueProxy;

public class ObservableQueue<E> extends QueueProxy<E> {

	private final Queue<E> sourceQueue;

	public ObservableQueue(final Queue<E> sourceQueue) {
		if (sourceQueue == null) {
			throw new NullPointerException("sourceQueue cannot be null");
		}
		this.sourceQueue = sourceQueue;
	}

	/**
	 * Adds the specified listener to listen for events from this queue
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addQueueListener(final QueueListener<E> listener) {

	}

	/**
	 * Removes the specified listener from this queue
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeQueueListener(final QueueListener<E> listener) {

	}

	@Override
	protected Queue<E> getSourceQueue() {
		return this.sourceQueue;
	}

}
