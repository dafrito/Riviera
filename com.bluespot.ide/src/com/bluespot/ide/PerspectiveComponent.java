package com.bluespot.ide;

import javax.swing.event.ChangeEvent;

import com.bluespot.swing.state.StateChangeEvent;
import com.bluespot.swing.state.StateChangeListener;

public class PerspectiveComponent {

	private final PerspectiveManager manager;

	public PerspectiveComponent(final PerspectiveManager manager) {
		this.manager = manager;
		this.manager.getStateModel().addStateChangeListener(new StateChangeListener<Perspective>() {

			public void stateChanging(StateChangeEvent<Perspective> e) {
				Perspective perspective = manager.getCurrentPerspective();
				if (perspective != null) {
					PerspectiveComponent.this.exitPerspective(perspective);
				}
			}

			public void stateChanged(ChangeEvent e) {
				Perspective perspective = manager.getCurrentPerspective();
				if (perspective != null) {
					PerspectiveComponent.this.enterPerspective(perspective);
				}
			}

		});
	}

	public PerspectiveManager getManager() {
		return this.manager;
	}

	/**
	 * @param perspective
	 *            The old perspective
	 */
	protected void exitPerspective(Perspective perspective) {
		// No-op implementation
	}

	/**
	 * @param perspective
	 *            The new perspective
	 */
	protected void enterPerspective(Perspective perspective) {
		// No-op implementation
	}
}
