package com.bluespot.logic;

import java.awt.Component;
import java.io.File;

import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.adapters.ChildFileAdapter;

/**
 * A suite of common adapters.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Adapters {

    private Adapters() {
        // Suppress default constructor to ensure non-instantiability
        throw new AssertionError();
    }

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
     * Returns a {@link ChildFileAdapter} that will return a child file of the
     * given file. If the given file is null, null is returned as the converted
     * value.
     * 
     * @param childFileName
     *            the name of the child, relative to the given file
     * @return a new {@code ChildFileAdapter} object
     */
    public static ChildFileAdapter childFile(final String childFileName) {
        return new ChildFileAdapter(childFileName);
    }
}
