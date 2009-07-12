package com.bluespot.logic;

import java.awt.Component;
import java.io.File;

import javax.swing.text.JTextComponent;

import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.adapters.ChildFileAdapter;
import com.bluespot.logic.adapters.HandledAdapter;

/**
 * A library of common {@link Adapter} factory methods. These methods are not
 * guaranteed to return unique instances; in fact, many return only one instance
 * for all invocations. Unless otherwise specified, {@code null} arguments will
 * cause {@link NullPointerException}'s to be thrown.
 * <p>
 * Like {@link Predicates}, we intentionally omit the concrete implementation of
 * these classes. If you need to work with concrete classes, instantiate them
 * directly. Not having them in the signatures here frees this library class to
 * use different adapters in the future.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Adapters {

    private Adapters() {
        // Suppress default constructor to ensure non-instantiability
        throw new AssertionError("Instantiation not allowed");
    }

    /**
     * An {@link Adapter} that blindly returns the given value. This is a no-op
     * or null adapter in that it performs no work whatsoever on the given
     * object.
     * 
     * @see #noop()
     */
    private static final Adapter<Object, Object> ADAPTER_NOOP = new Adapter<Object, Object>() {

        public Object adapt(final Object source) {
            return source;
        }

    };

    /**
     * Returns an adapter that does no adaptation. All values are returned
     * as-is. Useful when you're required to have an adapter.
     * 
     * @param <T>
     *            the type of value
     * @return an adapter that does nothing but return the given value
     */
    @SuppressWarnings("unchecked")
    public static <T> Adapter<T, T> noop() {
        /*
         * This cast is safe since our adapter doesn't actually use the type
         * provided, nor does it perform any work or casting on the given
         * object.
         */
        return (Adapter<T, T>) ADAPTER_NOOP;
    }

    /**
     * An {@link Adapter} that converts an object to a string using
     * {@link Object#toString()}.
     * 
     * @see #stringValue()
     */
    private static final Adapter<Object, String> ADAPTER_TO_STRING = new Adapter<Object, String>() {

        public String adapt(final Object source) {
            if (source == null) {
                return null;
            }
            return source.toString();
        }

        @Override
        public String toString() {
            return "string value";
        }

    };

    /**
     * Returns an adapter that converts a given object to a string using
     * {@link Object#toString()}. If the given object is null, null is returned
     * as the converted value.
     * 
     * @return an adapter that converts objects to strings
     */
    public static Adapter<Object, String> stringValue() {
        return ADAPTER_TO_STRING;
    }

    private static final Adapter<Component, String> ADAPTER_COMPONENT_NAME = new Adapter<Component, String>() {

        public String adapt(final Component source) {
            if (source == null) {
                return null;
            }
            return source.getName();
        }

        @Override
        public String toString() {
            return "component name";
        }
    };

    /**
     * Returns an adapter that converts a given {@link Component} object to a
     * string using {@link Component#getName()}. If the given component is null,
     * null is returned as the converted value.
     * 
     * @return an adapter that converts {@code Component} objects to strings
     *         representing the name of the component
     */
    public static Adapter<Component, String> componentName() {
        return ADAPTER_COMPONENT_NAME;
    }

    private static final Adapter<File, String> ADAPTER_FILE_NAME = new Adapter<File, String>() {

        public String adapt(final File source) {
            if (source == null) {
                return null;
            }
            return source.getName();
        }

        @Override
        public String toString() {
            return "file name";
        }
    };

    /**
     * Returns an adapter that converts a given {@link File} object to a string
     * using {@link File#getName()}. If the given file is null, null is returned
     * as the converted value.
     * 
     * @return an adapter that converts {@code File} objects to strings
     *         representing the filename
     */
    public static Adapter<File, String> fileName() {
        return ADAPTER_FILE_NAME;
    }

    /**
     * Returns an adapter that will return a child file of the given file. If
     * the given file is null, null is returned as the converted value.
     * 
     * @param childFileName
     *            the name of the child, relative to the given file
     * @return an adapter that retrieves a child file
     */
    public static Adapter<File, File> childFile(final String childFileName) {
        return new ChildFileAdapter(childFileName);
    }

    /**
     * Returns any text contained inside the specified {@link JTextComponent}.
     * 
     * @author Aaron Faanes
     * 
     * @see #componentText()
     * 
     */
    private static final Adapter<JTextComponent, String> ADAPTER_TEXT_COMPONENT_TO_STRING = new Adapter<JTextComponent, String>() {

        public final String adapt(final JTextComponent source) {
            if (source == null) {
                return null;
            }
            if (source.getDocument() == null) {
                return null;
            }
            return source.getText();
        }

    };

    /**
     * Returns an {@link Adapter} that returns any text contained inside a given
     * {@link JTextComponent}, according to {@link JTextComponent#getText()}. If
     * a {@code JTextComponent} has no document, {@code null} is returned. This
     * differs from the {@code getText()} implementation which throws a {@code
     * NullPointerException} if no document is present. At some point, we may
     * return a {@link HandledAdapter} here to manage null documents, but it
     * seems more of a programming error than a recoverable condition, so it is
     * omitted.
     * 
     * @author Aaron Faanes
     * @return an {@code Adapter} that converts {@link JTextComponent} objects
     *         to {@code String} values using {@link JTextComponent#getText()}.
     * 
     */
    public static final Adapter<JTextComponent, String> componentText() {
        return ADAPTER_TEXT_COMPONENT_TO_STRING;
    }

}
