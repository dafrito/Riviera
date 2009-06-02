package com.bluespot.ide.editor;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bluespot.swing.Dialogs;
import com.bluespot.swing.Dialogs.CancelledException;

public class Editor extends JTextPane {

	// TODO Implement undo/redo functionality
	// TODO Implement cut/copy/paste functionality

	private boolean dirty;

	private File file;

	protected DocumentListener documentListener = new DocumentListener() {

		public void changedUpdate(final DocumentEvent e) {
			Editor.this.dirty();
		}

		public void insertUpdate(final DocumentEvent e) {
			Editor.this.dirty();
		}

		public void removeUpdate(final DocumentEvent e) {
			Editor.this.dirty();
		}

	};

	public Editor() {
		this(null);
	}

	public Editor(final File file) {
		this.setFile(file);
		this.addPropertyChangeListener("document", new PropertyChangeListener() {

			public void propertyChange(final PropertyChangeEvent evt) {
				Editor.this.getDocument().addDocumentListener(Editor.this.documentListener);
			}

		});
		this.setFont(new Font("consolas", Font.PLAIN, 14));
	}

	public void dirty() {
		this.dirty = true;
	}

	public File getFile() {
		return this.file;
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public void open() throws IOException {
		this.open(this.getFile());
	}

	public void open(final File openedFile) throws IOException {
		this.setFile(openedFile);
		final Reader reader = new FileReader(openedFile);
		try {
			this.setDocument(this.getEditorKit().createDefaultDocument());
			this.read(reader, openedFile);
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					Editor.this.clearDirty();
				}

			});
		} finally {
			reader.close();
		}
	}

	public void save() throws IOException, CancelledException {
		if (this.getFile() != null && this.isDirty() == false) {
			return;
		}
		this.save(this.getFile());
	}

	public void save(File destinationFile) throws IOException, CancelledException {
		if (destinationFile == null) {
			destinationFile = Dialogs.saveFile();
		}
		this.setFile(destinationFile);
		final Writer writer = new FileWriter(this.getFile());
		try {
			this.write(writer);
			this.clearDirty();
		} finally {
			writer.close();
		}

	}

	public void setFile(final File file) {
		this.dirty();
		this.file = file;
		if (this.file != null) {
			this.setName(this.file.getName());
		}
	}

	public boolean trySave() {
		try {
			this.save();
			return true;
		} catch (final IOException ex) {
			Dialogs.error("The file could not be saved. (Error: " + ex.getLocalizedMessage() + ")",
					"File I/O Exception");
		} catch (final CancelledException e) {
			// Do nothing, fall-through.
		}
		return false;
	}

	protected void clearDirty() {
		this.dirty = false;
	}

	public static Editor openFile(final File file) throws IOException {
		final Editor editor = new Editor(file);
		editor.open();
		return editor;
	}
}
