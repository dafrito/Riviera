package com.bluespot.demonstration;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Runner {
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
