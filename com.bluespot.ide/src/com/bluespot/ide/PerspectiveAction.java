package com.bluespot.ide;

import javax.swing.AbstractAction;

public abstract class PerspectiveAction extends AbstractAction {

	public PerspectiveAction(final String name) {
		super(name);
	}

	public Perspective getPerspective() {
		return PerspectiveManager.getCurrentManager().getCurrentPerspective();
	}
}
