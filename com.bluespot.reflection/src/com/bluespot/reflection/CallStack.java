package com.bluespot.reflection;


/**
 * Represents a call stack. This class, and its entry class
 * {@link CallStackFrame}, provide some convenient functionality regarding call
 * stack construction.
 * <p>
 * This class is intended to work in concert with the {@link Reflection}
 * library.
 * 
 * @author Aaron Faanes
 * @see Throwable#getStackTrace()
 */
public interface CallStack extends Iterable<CallStackFrame> {

	/**
	 * Adds the specified listener to listen for events from this call stack
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addCallStackListener(CallStackListener listener);

	/**
	 * Removes the specified listener from this call stack
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeCallStackListener(CallStackListener listener);

	/**
	 * Returns whether this call stack contains the specified frame.
	 * 
	 * @param frame
	 *            the requested frame
	 * @return {@code true} if this call stack contains the specified frame,
	 *         otherwise {@code false}
	 */
	public boolean hasFrame(final CallStackFrame frame);

	/**
	 * @return the number of frames in this call stack
	 */
	public int getFrameCount();

	/**
	 * Returns the most recent frame in this call stack. If there are no frames
	 * present, this will return {@code null}.
	 * 
	 * @return the most recent frame in this call stack
	 */
	public CallStackFrame getMostRecentFrame();

	/**
	 * Returns whether this call stack has any frames.
	 * 
	 * @return {@code true} if this call stack has any frames, otherwise {@code
	 *         false}
	 */
	public boolean hasFrames();

}