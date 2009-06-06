package com.bluespot.demonstration;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * A simple utility that runs {@code Runnable}s and set ups the look and feel.
 * 
 * @author Aaron Faanes
 * 
 */
public class Runner {

	/**
	 * Runs the specified runnable, optionally on the EDT.
	 * <p>
	 * This will also set the PLAF. Its order of preference will be Nimbus, the
	 * system's LAF, and finally the default.
	 * 
	 * @param r
	 *            the runnable to run
	 * @param invokeLater
	 *            whether the runnable should be run on the EDT. If so,
	 *            {@link SwingUtilities#invokeLater(Runnable)} will be used.
	 *            Otherwise, the runnable will be run immediately.
	 */
	public static void run(final Runnable r, final boolean invokeLater) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (final Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (final Exception e1) {
				// Don't care; swallow and continue.
				e1.printStackTrace();
			}
		}
		if (invokeLater) {
			SwingUtilities.invokeLater(r);
		} else {
			r.run();
		}
	}
}
