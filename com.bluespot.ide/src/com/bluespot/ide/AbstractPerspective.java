package com.bluespot.ide;

import javax.swing.JMenuBar;

public abstract class AbstractPerspective implements Perspective {

	private final String name;

	public AbstractPerspective(final String name) {
		this.name = name;
	}

	public void close() {
		// No-op implementation
	}

	public String getName() {
		return this.name;
	}

	public boolean isReadyForClose() {
		return true;
	}

	public void populateMenuBar(final JMenuBar menuBar) {
		// No-op implementation
	}

}
