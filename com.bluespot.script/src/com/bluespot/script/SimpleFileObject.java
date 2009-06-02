package com.bluespot.script;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import javax.tools.FileObject;

public class SimpleFileObject implements FileObject {

	private final File file;

	public SimpleFileObject(final File file) {
		this.file = file;
		if (this.file == null) {
			throw new NullPointerException("File cannot be null");
		}
	}

	public boolean delete() {
		return this.getFile().delete();
	}

	public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
		throw new UnsupportedOperationException();
	}

	public File getFile() {
		return this.file;
	}

	public long getLastModified() {
		return this.getFile().lastModified();
	}

	public String getName() {
		return this.getFile().getName();
	}

	public InputStream openInputStream() throws IOException {
		return new FileInputStream(this.getFile());
	}

	public OutputStream openOutputStream() throws IOException {
		return new FileOutputStream(this.getFile());
	}

	public Reader openReader(final boolean ignoreEncodingErrors) throws IOException {
		return new FileReader(this.getFile());
	}

	public Writer openWriter() throws IOException {
		return new FileWriter(this.getFile());
	}

	public URI toUri() {
		return this.getFile().toURI();
	}

}
