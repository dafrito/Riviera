package com.bluespot.reflection;

import java.awt.Frame;
import java.util.HashSet;
import java.util.Set;

public final class Reflection {

	private Reflection() {
		// Suppresses default constructor, ensuring non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	public static MutableCallStack asCallStack(final StackTraceElement[] stackElements) {
		final MutableCallStack callStack = new MutableCallStack();
		for (int i = stackElements.length - 1; i >= 0; i--) {
			callStack.addFrame(Reflection.toCallStackFrame(stackElements[i]));
		}
		return callStack;
	}

	public static MutableCallStack getCurrentCallStack() {
		return Reflection.asCallStack((new Throwable()).getStackTrace());
	}

	public static Frame getCurrentFrame() {
		final StackTraceElement[] stackElements = (new Throwable()).getStackTrace();
		for (final StackTraceElement stackElement : stackElements) {
			final Frame frame = Reflection.toCallStackFrame(stackElement);
			if (!MutableCallStack.isIgnored(frame)) {
				return frame;
			}
		}
		return MutableCallStack.EMPTY_FRAME;
	}

	public static Frame toCallStackFrame(final StackTraceElement stackElement) {
		return new Frame(stackElement.getClassName(), stackElement.getMethodName());
	}

	private static final Set<String> ignoredMethodNames = new HashSet<String>();

	private static final Set<Package> ignoredPackages = new HashSet<Package>();

	static {
		// Ignore reflection and logging packages by default
		Reflection.ignorePackage(Reflection.class.getPackage());
		Reflection.ignorePackage(Package.getPackage("java.util.logging"));
	}

	/**
	 * Ignores the specified method by name. Any frame that originates from any
	 * method with the specified name will not be added to any call stack.
	 * 
	 * @param name
	 *            the method to ignore
	 */
	public static void ignoreMethodName(final String name) {
		Reflection.ignoredMethodNames.add(name);
	}

	/**
	 * Ignores the specified package. Any frame that originates from the
	 * specified package will not be added to any call stack.
	 * 
	 * @param ignoredPackage
	 *            the package to ignore
	 */
	public static void ignorePackage(final Package ignoredPackage) {
		Reflection.ignoredPackages.add(ignoredPackage);
	}

	/**
	 * Returns whether the specified frame is ignored. A frame can be ignored by
	 * method name or by package.
	 * 
	 * @param frame
	 *            the frame to check
	 * @return {@code true} if the frame should be ignored
	 * @see CallStack#ignoreMethodName(String)
	 * @see CallStack#ignorePackage(Package)
	 */
	public static boolean isIgnored(final CallStackFrame frame) {
		if (Reflection.ignoredPackages.contains(frame.getPackage())) {
			return true;
		}
		return Reflection.ignoredMethodNames.contains(frame.getMethodName());
	}
}
