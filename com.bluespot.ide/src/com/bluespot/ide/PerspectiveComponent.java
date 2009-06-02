package com.bluespot.ide;

import javax.swing.event.ChangeEvent;

import com.bluespot.swing.state.StateChangeEvent;
import com.bluespot.swing.state.StateChangeListener;

public class PerspectiveComponent {

	private final PerspectiveManager manager;

	public PerspectiveComponent(final PerspectiveManager manager) {
		this.manager = manager;
		this.manager.getStateModel().addStateChangeListener(new StateChangeListener<Perspective>() {

			public void stateChanged(final ChangeEvent e) {
				final Perspective perspective = manager.getCurrentPerspective();
				if (perspective != null) {
					PerspectiveComponent.this.enterPerspective(perspective);
				}
			}

			public void stateChanging(final StateChangeEvent<Perspective> e) {
				final Perspective perspective = manager.getCurrentPerspective();
				if (perspective != null) {
					PerspectiveComponent.this.exitPerspective(perspective);
				}
			}

		});
	}

	public PerspectiveManager getManager() {
		return this.manager;
	}

	/**
	 * @param perspective
	 *            The new perspective
	 */
	protected void enterPerspective(final Perspective perspective) {
		// No-op implementation
	}

	/**
	 * @param perspective
	 *            The old perspective
	 */
	protected void exitPerspective(final Perspective perspective) {
		// No-op implementation
	}
}
