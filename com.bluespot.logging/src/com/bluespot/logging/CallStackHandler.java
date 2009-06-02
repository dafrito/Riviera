package com.bluespot.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bluespot.reflection.CallStack;
import com.bluespot.reflection.Reflection;
import com.bluespot.tree.Tree;
import com.bluespot.tree.TreeWalker;

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

	private final CallStack callStack = new CallStack();

	private final TreeWalker<? super LogRecord> treeWalker;

	public CallStackHandler(final Tree<? super LogRecord> tree) {
		this(tree.walker());
	}

	public CallStackHandler(final TreeWalker<? super LogRecord> tree) {
		this.treeWalker = tree;
	}

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
		final CallStack.Frame frame = this.getFrameFromRecord(record);
		if (frame.equals(this.callStack.getCurrentFrame())) {
			this.treeWalker.append(record);
			return;
		}
		final CallStack recordCallStack = Reflection.getCurrentCallStack();
		while (!this.callStack.isEmpty()) {
			if (recordCallStack.contains(this.callStack.getCurrentFrame())) {
				break;
			}
			this.pop();
		}
		if (frame.equals(this.callStack.getCurrentFrame())) {
			this.treeWalker.append(record);
		} else {
			this.push(record);
		}
	}

	private CallStack.Frame getFrameFromRecord(final LogRecord record) {
		return new CallStack.Frame(record.getSourceClassName(), record.getSourceMethodName());
	}

	/**
	 * @param record
	 *            The record to handle
	 * @return True if the record was handled; if so, no further work is needed
	 *         to be done.
	 */
	private boolean handleExplicitCallStackOperation(final LogRecord record) {
		final String msg = record.getMessage();
		final Matcher enteringMatcher = CallStackHandler.enteringPattern.matcher(msg);
		if (enteringMatcher.matches()) {
			final CallStack recordCallStack = Reflection.getCurrentCallStack();
			recordCallStack.pop();
			while (!this.callStack.isEmpty() && !recordCallStack.contains(this.callStack.getCurrentFrame())) {
				this.pop();
			}
			this.push(record);
			return true;
		}
		final Matcher returningMatcher = CallStackHandler.returningPattern.matcher(msg);
		if (returningMatcher.matches() || CallStackHandler.throwingPattern.matcher(msg).matches()) {
			final CallStack.Frame frame = this.getFrameFromRecord(record);
			if (!this.callStack.contains(frame)) {
				return false;
			}
			while (!this.callStack.getCurrentFrame().equals(frame)) {
				this.pop();
			}
			this.treeWalker.append(record);
			this.pop();
			return true;
		}
		return false;
	}

	private CallStack.Frame pop() {
		final CallStack.Frame poppedFrame = this.callStack.pop();
		this.treeWalker.leave();
		return poppedFrame;
	}

	private void push(final LogRecord record) {
		this.callStack.push(this.getFrameFromRecord(record));
		this.treeWalker.appendAndEnter(record);
	}

	static {
		CallStack.ignorePackage(CallStackHandler.class.getPackage());
	}

	public static final Pattern enteringPattern = Pattern.compile("^ENTRY(\\{\\d+\\})?$");

	public static final Pattern returningPattern = Pattern.compile("^RETURN(\\{\\d+\\})?$");

	public static final Pattern throwingPattern = Pattern.compile("^THROW$");

}
