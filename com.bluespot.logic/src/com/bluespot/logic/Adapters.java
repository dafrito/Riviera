package com.bluespot.logic;

import javax.swing.JComponent;

import com.bluespot.logic.adapters.ChildFileAdapter;
import com.bluespot.logic.adapters.ComponentNameAdapter;
import com.bluespot.logic.adapters.FileNameAdapter;

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

	/**
	 * Returns a {@link ComponentNameAdapter} that returns the name of a given
	 * {@link JComponent}.
	 * 
	 * @return a {@link ComponentNameAdapter} object
	 */
	public static ComponentNameAdapter componentName() {
		return ComponentNameAdapter.getInstance();
	}

	/**
	 * Returns a {@link FileNameAdapter} that gets the filename of the specified
	 * file.
	 * 
	 * @return a {@code FileNameAdapter} object
	 */
	public static FileNameAdapter fileName() {
		return FileNameAdapter.getInstance();
	}

	/**
	 * Returns a {@link ChildFileAdapter} that will return a child file of the
	 * given file.
	 * 
	 * @param childFileName
	 *            the name of the child, relative to the given file
	 * @return a {@code ChildFileAdapter} object
	 */
	public static ChildFileAdapter childFile(final String childFileName) {
		return new ChildFileAdapter(childFileName);
	}
}
