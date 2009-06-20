package com.bluespot.demonstration;

import javax.swing.SwingUtilities;

import com.bluespot.swing.Components;

/**
 * A simple utility that runs {@code Runnable}s and set ups the look and feel.
 * 
 * @author Aaron Faanes
 * 
 */
public class Demonstrations {

    /**
     * Runs the specified runnable on the EDT. The class provided must have a
     * zero-argument constructor.
     * 
     * @param klass
     *            the class from which a runnable is created
     */
    public static void run(final Class<? extends Runnable> klass) {
        Demonstrations.run(new Runnable() {

            @Override
            public void run() {
                try {
                    klass.newInstance().run();
                } catch (final InstantiationException e) {
                    e.printStackTrace();
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }, true);
    }

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
        if (!Components.LookAndFeel.NIMBUS.activate()) {
            Components.LookAndFeel.SYSTEM.activate();
        }
        if (invokeLater) {
            SwingUtilities.invokeLater(r);
        } else {
            r.run();
        }
    }
}
