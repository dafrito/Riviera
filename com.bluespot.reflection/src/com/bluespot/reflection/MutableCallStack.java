package com.bluespot.reflection;

import java.awt.Frame;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Represents a call stack. This class, and its entry class {@link Frame},
 * provide some convenient functionality regarding call stack construction.
 * <p>
 * This class is intended to work in concert with the {@link Reflection}
 * library.
 * 
 * @author Aaron Faanes
 * @see Throwable#getStackTrace()
 */
public class MutableCallStack implements CallStack {

	/**
	 * Represents a null frame. Since Frames
	 */
	public static final MutableCallStack.Frame EMPTY_FRAME = new Frame("", "");

	private static final Set<String> ignoredMethodNames = new HashSet<String>();

	private static final Set<Package> ignoredPackages = new HashSet<Package>();

	private final Deque<MutableCallStack.Frame> callStack = new ArrayDeque<MutableCallStack.Frame>();

	/**
	 * Adds a frame to the end of this stack. Frames should be added in a
	 * least-recent to most-recent order.
	 * <p>
	 * If the frame is ignored, the frame will not be added.
	 * 
	 * @param frame
	 *            the frame to add
	 * @see MutableCallStack#isIgnored(Frame)
	 */
	public void addFrame(final MutableCallStack.Frame frame) {
		if (!MutableCallStack.isIgnored(frame)) {
			this.callStack.push(frame);
		}
	}

	/**
	 * Returns whether this call stack contains the specified frame.
	 * 
	 * @param frame
	 *            the requested frame
	 * @return {@code true} if this call stack contains the specified frame,
	 *         otherwise {@code false}
	 */
	public boolean hasFrame(final MutableCallStack.Frame frame) {
		return this.callStack.contains(frame);
	}

	/**
	 * @return the number of frames in this call stack
	 */
	public int getFrameCount() {
		return this.callStack.size();
	}

	/**
	 * Returns the most recent frame in this call stack. If there are no frames
	 * present, this will return {@code null}.
	 * 
	 * @return the most recent frame in this call stack
	 */
	public MutableCallStack.Frame getMostRecentFrame() {
		return this.callStack.peekLast();
	}

	/**
	 * Returns whether this call stack has any frames.
	 * 
	 * @return {@code true} if this call stack has any frames, otherwise {@code
	 *         false}
	 */
	public boolean hasFrames() {
		return !this.callStack.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<Frame> iterator() {
		return this.callStack.iterator();
	}

	/**
	 * Removes and returns the most recent frame in this call stack.
	 * 
	 * @throws NoSuchElementException
	 *             if there are no remaining frames
	 * @return the most recent frame
	 * @see #hasFrames()
	 */
	public MutableCallStack.Frame removeMostRecentFrame() {
		return this.callStack.removeFirst();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(String.format("CallStack (%d frame(s))%n", this.getFrameCount()));
		return builder.toString();
	}

}