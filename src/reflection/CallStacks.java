package reflection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * A collection of methods assisting with determining the state of the
 * callstack.
 * 
 * @author Aaron Faanes
 * 
 */
public final class CallStacks {

	/**
	 * Represents the null frame. This is a frame that is used when one cannot
	 * otherwise be created or derived.
	 */
	public static CallStackFrame EMPTY_FRAME = new CallStackFrame("", "");

	private CallStacks() {
		// Suppresses default constructor, ensuring non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * Converts the specified array of {@link StackTraceElement} objects to a
	 * deque of {@link CallStackFrame} objects.
	 * 
	 * @param stackElements
	 *            the array to convert
	 * @return a deque that is logically equivalent to the specified array
	 * @see Throwable#getStackTrace()
	 */
	public static Deque<CallStackFrame> asDeque(final StackTraceElement[] stackElements) {
		final Deque<CallStackFrame> callStack = new ArrayDeque<CallStackFrame>();
		for (final StackTraceElement frame : stackElements) {
			callStack.addLast(CallStacks.toCallStackFrame(frame));
		}
		return callStack;
	}

	/**
	 * Returns the current call stack, as determined by
	 * {@link Throwable#getStackTrace()}.
	 * 
	 * @return the current call stack
	 */
	public static Deque<CallStackFrame> getCurrentCallStack() {
		return CallStacks.asDeque((new Throwable()).getStackTrace());
	}

	/**
	 * Returns the current call stack frame. This will use the current call
	 * stack and return the first frame that is not ignored.
	 * 
	 * @return the current call stack frame
	 */
	public static CallStackFrame getCurrentFrame() {
		final Deque<CallStackFrame> callStack = CallStacks.getCurrentCallStack();
		for (final CallStackFrame callStackFrame : callStack) {
			if (!CallStacks.isIgnored(callStackFrame)) {
				return callStackFrame;
			}
		}
		return CallStacks.EMPTY_FRAME;
	}

	/**
	 * Converts the specified {@link StackTraceElement} to a
	 * {@link CallStackFrame}.
	 * 
	 * @param stackElement
	 *            the {@code StackTraceElement} to convert.
	 * @return a {@code CallStackFrame} that represents the specified stack
	 *         trace element.
	 */
	public static CallStackFrame toCallStackFrame(final StackTraceElement stackElement) {
		return new CallStackFrame(stackElement.getClassName(), stackElement.getMethodName());
	}

	private static final Set<String> ignoredMethodNames = new HashSet<String>();

	private static final Set<Package> ignoredPackages = new HashSet<Package>();

	static {
		// Ignore reflection and logging packages by default
		CallStacks.ignorePackage(CallStacks.class.getPackage());
		CallStacks.ignorePackage(Package.getPackage("java.util.logging"));
	}

	/**
	 * Ignores the specified method by name. Any frame that originates from any
	 * method with the specified name will not be added to any call stack.
	 * 
	 * @param name
	 *            the method to ignore
	 */
	public static void ignoreMethodName(final String name) {
		CallStacks.ignoredMethodNames.add(name);
	}

	/**
	 * Ignores the specified package. Any frame that originates from the
	 * specified package will not be added to any call stack.
	 * 
	 * @param ignoredPackage
	 *            the package to ignore
	 */
	public static void ignorePackage(final Package ignoredPackage) {
		CallStacks.ignoredPackages.add(ignoredPackage);
	}

	/**
	 * Returns whether the specified frame is ignored. A frame can be ignored by
	 * method name or by package.
	 * 
	 * @param frame
	 *            the frame to check
	 * @return {@code true} if the frame should be ignored
	 * @see CallStacks#ignoreMethodName(String)
	 * @see CallStacks#ignorePackage(Package)
	 */
	public static boolean isIgnored(final CallStackFrame frame) {
		if (CallStacks.ignoredPackages.contains(frame.getPackage())) {
			return true;
		}
		return CallStacks.ignoredMethodNames.contains(frame.getMethodName());
	}
}
