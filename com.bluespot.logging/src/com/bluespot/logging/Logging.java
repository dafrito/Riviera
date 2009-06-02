package com.bluespot.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.bluespot.reflection.CallStack;
import com.bluespot.reflection.Reflection;
import com.bluespot.reflection.CallStack.Frame;

/**
 * Utility methods for convenient logging with java.util.logging
 * <p>
 * This is a suite of common logging methods. Generally speaking, you can do
 * logFoo(String message) where Foo is the intended log level.
 * <p>
 * Each logFoo has two forms: <code>logFoo(String message)</code> and
 * <code>logFoo(String message, Object...params)</code>. The second form allows
 * you to easily specify parameters, typically to use in String.format calls.
 * <p>
 * A user would typically import this class statically.
 * 
 * @author Aaron Faanes
 */
public final class Logging {

	private Logging() {
		throw new UnsupportedOperationException("Instantiation not allowed");
	}

	static {
		CallStack.ignorePackage(Logging.class.getPackage());
	}

	public static void log(final String message) {
		Logging.logInfo(message);
	}

	public static void log(final String message, final Object... args) {
		Logging.logInfo(message, args);
	}

	public static void logConfig(final String message) {
		Logging.logRecord(new LogRecord(Level.CONFIG, message));
	}

	public static void logConfig(final String message, final Object... args) {
		Logging.logRecord(new LogRecord(Level.CONFIG, message), args);
	}

	// -------------------------------------------------------------------------
	//
	// Call-Stack Related Messages
	//
	// -------------------------------------------------------------------------

	public static void logEntry() {
		Logging.logRecord(new LogRecord(Level.FINER, "ENTRY"));
	}

	public static void logEntry(final Object param) {
		Logging.logRecord(new LogRecord(Level.FINER, "ENTRY"), param);
	}

	public static void logEntry(final Object... params) {
		Logging.logRecord(new LogRecord(Level.FINER, "ENTRY"), params);
	}

	public static void logException(final Throwable thrown) {
		final LogRecord record = new LogRecord(Level.SEVERE, thrown.getMessage());
		record.setThrown(thrown);
		Logging.logRecord(record);
	}

	public static void logExit() {
		Logging.logRecord(new LogRecord(Level.FINER, "RETURN"));
	}

	public static void logExit(final Object returnValue) {
		Logging.logRecord(new LogRecord(Level.FINER, "RETURN"), returnValue);
	}

	public static void logFine(final String message) {
		Logging.logRecord(new LogRecord(Level.FINE, message));
	}

	public static void logFine(final String message, final Object... args) {
		Logging.logRecord(new LogRecord(Level.FINE, message), args);
	}

	public static void logFiner(final String message) {
		Logging.logRecord(new LogRecord(Level.FINER, message));
	}

	// -------------------------------------------------------------------------
	//
	// Standard Log-level messages
	//
	// -------------------------------------------------------------------------

	public static void logFiner(final String message, final Object... args) {
		Logging.logRecord(new LogRecord(Level.FINER, message), args);
	}

	public static void logFinest(final String message) {
		Logging.logRecord(new LogRecord(Level.FINEST, message));
	}

	public static void logFinest(final String message, final Object... args) {
		Logging.logRecord(new LogRecord(Level.FINEST, message), args);
	}

	public static void logInfo(final String message) {
		Logging.logRecord(new LogRecord(Level.INFO, message));
	}

	public static void logInfo(final String message, final Object... args) {
		Logging.logRecord(new LogRecord(Level.INFO, message), args);
	}

	public static void logRecord(final LogRecord record) {
		final Frame frame = Reflection.getCurrentFrame();
		record.setSourceClassName(frame.getClassName());
		record.setSourceMethodName(frame.getMethodName());
		Logger.getLogger(frame.getPackageName()).log(record);
	}

	public static void logRecord(final LogRecord record, final Object param) {
		record.setParameters(new Object[] { param });
		Logging.logRecord(record);
	}

	public static void logRecord(final LogRecord record, final Object[] params) {
		record.setParameters(params);
		Logging.logRecord(record);
	}

	public static void logRecord(final LogRecord record, final Throwable thrown) {
		record.setThrown(thrown);
		Logging.logRecord(record);
	}

	public static void logReturn() {
		Logging.logExit();
	}

	public static void logReturn(final Object returnValue) {
		Logging.logExit(returnValue);
	}

	public static void logSevere(final String message) {
		Logging.logRecord(new LogRecord(Level.SEVERE, message));
	}

	public static void logSevere(final String message, final Object... args) {
		Logging.logRecord(new LogRecord(Level.SEVERE, message), args);
	}

	public static void logThrown(final Throwable thrown) {
		Logging.logRecord(new LogRecord(Level.FINER, "THROW"), thrown);
	}

	public static void logWarning(final String message) {
		Logging.logRecord(new LogRecord(Level.WARNING, message));
	}

	public static void logWarning(final String message, final Object... args) {
		Logging.logRecord(new LogRecord(Level.WARNING, message), args);
	}

}
