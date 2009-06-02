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

    static {
        CallStack.ignorePackage(Logging.class.getPackage());
    }

    private Logging() {
        throw new UnsupportedOperationException("Instantiation not allowed");
    }

    public static void logRecord(LogRecord record) {
        Frame frame = Reflection.getCurrentFrame();
        record.setSourceClassName(frame.getClassName());
        record.setSourceMethodName(frame.getMethodName());
        Logger.getLogger(frame.getPackageName()).log(record);
    }

    public static void logRecord(LogRecord record, Object[] params) {
        record.setParameters(params);
        logRecord(record);
    }

    public static void logRecord(LogRecord record, Object param) {
        record.setParameters(new Object[] { param });
        logRecord(record);
    }

    public static void logRecord(LogRecord record, Throwable thrown) {
        record.setThrown(thrown);
        logRecord(record);
    }

    // -------------------------------------------------------------------------
    //
    // Call-Stack Related Messages
    //
    // -------------------------------------------------------------------------

    public static void logEntry() {
        logRecord(new LogRecord(Level.FINER, "ENTRY"));
    }

    public static void logEntry(Object param) {
        logRecord(new LogRecord(Level.FINER, "ENTRY"), param);
    }

    public static void logEntry(Object... params) {
        logRecord(new LogRecord(Level.FINER, "ENTRY"), params);
    }

    public static void logExit() {
        logRecord(new LogRecord(Level.FINER, "RETURN"));
    }

    public static void logExit(Object returnValue) {
        logRecord(new LogRecord(Level.FINER, "RETURN"), returnValue);
    }

    public static void logReturn() {
        logExit();
    }

    public static void logReturn(Object returnValue) {
        logExit(returnValue);
    }

    public static void logThrown(Throwable thrown) {
        logRecord(new LogRecord(Level.FINER, "THROW"), thrown);
    }
    
    public static void logException(Throwable thrown) {
        LogRecord record = new LogRecord(Level.SEVERE, thrown.getMessage());
        record.setThrown(thrown);
        logRecord(record);
    }

    // -------------------------------------------------------------------------
    //
    // Standard Log-level messages
    //
    // -------------------------------------------------------------------------

    public static void log(String message) {
        logInfo(message);
    }

    public static void log(String message, Object... args) {
        logInfo(message, args);
    }

    public static void logSevere(String message) {
        logRecord(new LogRecord(Level.SEVERE, message));
    }

    public static void logSevere(String message, Object... args) {
        logRecord(new LogRecord(Level.SEVERE, message), args);
    }

    public static void logWarning(String message) {
        logRecord(new LogRecord(Level.WARNING, message));
    }

    public static void logWarning(String message, Object... args) {
        logRecord(new LogRecord(Level.WARNING, message), args);
    }

    public static void logInfo(String message) {
        logRecord(new LogRecord(Level.INFO, message));
    }

    public static void logInfo(String message, Object... args) {
        logRecord(new LogRecord(Level.INFO, message), args);
    }

    public static void logConfig(String message) {
        logRecord(new LogRecord(Level.CONFIG, message));
    }

    public static void logConfig(String message, Object... args) {
        logRecord(new LogRecord(Level.CONFIG, message), args);
    }

    public static void logFine(String message) {
        logRecord(new LogRecord(Level.FINE, message));
    }

    public static void logFine(String message, Object... args) {
        logRecord(new LogRecord(Level.FINE, message), args);
    }

    public static void logFiner(String message) {
        logRecord(new LogRecord(Level.FINER, message));
    }

    public static void logFiner(String message, Object... args) {
        logRecord(new LogRecord(Level.FINER, message), args);
    }

    public static void logFinest(String message) {
        logRecord(new LogRecord(Level.FINEST, message));
    }

    public static void logFinest(String message, Object... args) {
        logRecord(new LogRecord(Level.FINEST, message), args);
    }

}
