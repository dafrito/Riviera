package com.bluespot.logic.adapters;

import java.io.File;

/**
 * An adapter that gets the filename of the given file.
 * 
 * @author Aaron Faanes
 */
public final class FileNameAdapter implements Adapter<File, String> {

    private FileNameAdapter() {
        // Suppress default constructor
    }

    @Override
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

    private static final FileNameAdapter INSTANCE = new FileNameAdapter();

    /**
     * Returns the only {@link FileNameAdapter} object.
     * 
     * @return the only filename adapter
     */
    public static FileNameAdapter getInstance() {
        return FileNameAdapter.INSTANCE;
    }
}
