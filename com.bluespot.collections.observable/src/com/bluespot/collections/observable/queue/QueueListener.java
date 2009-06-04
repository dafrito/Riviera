package com.bluespot.collections.observable.queue;

/**
 * A listener for call stacks.
 * 
 * @author Aaron Faanes
 * @see CallStack
 */
public interface QueueListener<E> {

	/**
	 * Invoked when the call stack has entered the specified frame.
	 * 
	 * @param frame
	 *            the frame that was entered
	 */
	public void enterFrame(E frame);

	/**
	 * Invoked when the call stack has left the specified frame.
	 * 
	 * @param frame
	 *            the frame that was left
	 */
	public void leaveFrame(E frame);
}
