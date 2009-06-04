package com.bluespot.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

import com.bluespot.reflection.MutableCallStack;
import com.bluespot.reflection.Reflection;

/**
 * Publishes LogRecords to a tree based on each record's source method.
 * <p>
 * This class bridges LogRecord output into a tree. The tree will reflect the
 * call stack of the LogRecord intuitively. This handler also understands
 * exiting(), entering(), and throwing() LogRecords and will use them as hints
 * to update itself.
 * <p>
 * Note that this class provides only a "best effort" at the call structure of
 * these records. Certain situations may provide insufficient information as to
 * the state of the call stack, such as recursion and iteration.
 * <p>
 * You can correct these shortcomings by deliberate use of entering() or
 * exiting() within iterated methods. You can also place a comment in the body
 * of the loop, which will let the handler know the iterated method has
 * returned.
 * 
 * @author Aaron Faanes
 */
public class CallStackHandler extends Handler {

	private final MutableCallStack callStack = new MutableCallStack();

	@Override
	public void close() throws SecurityException {
		// Do nothing: This may be interpreted as clearing the tree
	}

	@Override
	public void flush() {
		// Do nothing: This may be interpreted as clearing the call stack
	}

	@Override
	public void publish(final LogRecord record) {
		if (!this.isLoggable(record)) {
			return;
		}
		if (this.handleExplicitCallStackOperation(record)) {
			return;
		}
		final MutableCallStack.Frame frame = this.getFrameFromRecord(record);
		if (frame.equals(this.callStack.getMostRecentFrame())) {
			this.treeWalker.append(record);
			return;
		}
		final MutableCallStack recordCallStack = Reflection.getCurrentCallStack();
		while (this.callStack.hasFrames()) {
			if (recordCallStack.hasFrame(this.callStack.getMostRecentFrame())) {
				break;
			}
			this.removeMostRecentFrame();
		}
		if (frame.equals(this.callStack.getMostRecentFrame())) {
			this.treeWalker.append(record);
		} else {
			this.addFrame(record);
		}
	}

	private MutableCallStack.Frame getFrameFromRecord(final LogRecord record) {
		return new MutableCallStack.Frame(record.getSourceClassName(), record.getSourceMethodName());
	}

	private boolean isExplicitExit(final LogRecord record) {
		final String msg = record.getMessage();
		if (CallStackHandler.returningPattern.matcher(msg).matches()) {
			return true;
		}
		if (CallStackHandler.throwingPattern.matcher(msg).matches()) {
			return true;
		}
		return false;
	}

	private boolean isExplicitEntry(final LogRecord record) {
		final String msg = record.getMessage();
		return CallStackHandler.enteringPattern.matcher(msg).matches();
	}

	/**
	 * Handles a log record that specifies that a method has been explicitly
	 * entered. In this case, the handler's call stack will unroll until its
	 * most recent frame is contained in the record's call stack.
	 * 
	 * 
	 * @param record
	 */
	private void handleExplicitEntry(final LogRecord record) {
		final MutableCallStack recordCallStack = Reflection.getCurrentCallStack();
		recordCallStack.removeMostRecentFrame();
		while (this.callStack.hasFrames() && !recordCallStack.hasFrame(this.callStack.getMostRecentFrame())) {
			this.removeMostRecentFrame();
		}
		this.addFrame(record);
	}

	private void handleExplicitExit(final LogRecord record) {
		final MutableCallStack.Frame frame = this.getFrameFromRecord(record);
		if (!this.callStack.hasFrame(frame)) {
			return false;
		}
		while (!this.callStack.getMostRecentFrame().equals(frame)) {
			this.removeMostRecentFrame();
		}
		this.treeWalker.append(record);
		this.removeMostRecentFrame();
	}

	/**
	 * @param record
	 *            The record to handle
	 * @return True if the record was handled; if so, no further work is needed
	 *         to be done.
	 */
	private boolean handleExplicitCallStackOperation(final LogRecord record) {
		if (this.isExplicitEntry(record)) {
			this.handleExplicitEntry(record);
			return true;
		}
		if (this.isExplicitExit(record)) {
			final MutableCallStack.Frame frame = this.getFrameFromRecord(record);
			if (!this.callStack.hasFrame(frame)) {
				return false;
			}
			while (!this.callStack.getMostRecentFrame().equals(frame)) {
				this.removeMostRecentFrame();
			}
			this.treeWalker.append(record);
			this.removeMostRecentFrame();
			return true;
		}
		return false;
	}

	private MutableCallStack.Frame removeMostRecentFrame() {
		final MutableCallStack.Frame poppedFrame = this.callStack.removeMostRecentFrame();
		this.treeWalker.leave();
		return poppedFrame;
	}

	private void addFrame(final LogRecord record) {
		this.callStack.addFrame(this.getFrameFromRecord(record));
		this.treeWalker.appendAndEnter(record);
	}

	static {
		MutableCallStack.ignorePackage(CallStackHandler.class.getPackage());
	}

	public static final Pattern enteringPattern = Pattern.compile("^ENTRY(\\{\\d+\\})?$");

	public static final Pattern returningPattern = Pattern.compile("^RETURN(\\{\\d+\\})?$");

	public static final Pattern throwingPattern = Pattern.compile("^THROW$");

}
