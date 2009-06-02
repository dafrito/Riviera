package com.bluespot.demonstration;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AbstractDemonstration implements Runnable {

	public void run() {

		final JFrame frame = new JFrame(this.getFrameName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		frame.setContentPane(panel);

		this.initializeFrame(frame);

		frame.setVisible(true);

	}

	protected String getFrameName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * @param frame
	 *            Root frame to populate with whatever you're demonstrating.
	 */
	protected void initializeFrame(final JFrame frame) {
		// Do nothing; override this behavior in subclasses.
	}

}
