/**
 * 
 */
package com.bluespot.logic.iterators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterate over a buffered reader's lines. {@code IOException}s that occur
 * during iteration will be wrapped using {@link UnderlyingIOException}.
 * 
 * @author Aaron Faanes
 * @see BufferedReader#readLine()
 */
public class LineIterator implements Iterator<String> {

	/**
	 * Returns a new {@link Iterable} that provides each line from a specified
	 * file.
	 * 
	 * @param file
	 *            the file that will be iterated
	 * @return an {@link Iterable} that provides each line from the specified
	 *         file
	 * @throws com.bluespot.logic.iterators.UnderlyingIOException
	 *             if an {@link java.io.IOException} occurs during iteration
	 * @see com.bluespot.logic.iterators.LineIterator
	 */
	public static Iterable<String> iterable(final File file) {
		if (file == null) {
			throw new NullPointerException("file must not be null");
		}
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return new LineIterator(file);
			}
		};
	}

	private final BufferedReader reader;

	private String line;
	private boolean started = false;

	/**
	 * Construct a line iterator that reads from the specified string.
	 * 
	 * @param content
	 *            the underlying content of this iterator
	 */
	public LineIterator(String content) {
		if (content == null) {
			throw new NullPointerException("string must not be null");
		}
		this.reader = new BufferedReader(new StringReader(content));
	}

	/**
	 * Construct a line iterator that reads from the specified file.
	 * 
	 * @param file
	 *            the file that is read
	 * @throws UnderlyingIOException
	 *             if an {@link IOException} occurs
	 */
	public LineIterator(File file) {
		if (file == null) {
			throw new NullPointerException("file must not be null");
		}
		try {
			this.reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new UnderlyingIOException(e);
		}

	}

	public LineIterator(BufferedReader reader) {
		if (reader == null) {
			throw new NullPointerException("reader must not be null");
		}
		this.reader = reader;
	}

	private void stageLine() throws UnderlyingIOException {
		this.started = true;
		try {
			this.line = this.reader.readLine();
			if (this.line == null) {
				this.reader.close();
			}
		} catch (IOException ioe) {
			throw new UnderlyingIOException(ioe);
		}
	}

	@Override
	public boolean hasNext() throws UnderlyingIOException {
		if (!this.started) {
			this.stageLine();
		}
		return this.line != null;
	}

	@Override
	public String next() throws UnderlyingIOException {
		if (!this.started) {
			this.stageLine();
		}
		if (this.line == null) {
			throw new NoSuchElementException("No more elements are available");
		}
		String rv = this.line;
		this.stageLine();
		return rv;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Removal is not supported");
	}
}
