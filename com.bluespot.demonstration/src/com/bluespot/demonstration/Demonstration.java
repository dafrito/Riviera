package com.bluespot.demonstration;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.bluespot.swing.Components;

/**
 * A simple utility that runs {@code Runnable}s and set ups the look and feel.
 * 
 * @author Aaron Faanes
 * 
 */
public abstract class Demonstration implements Runnable {

    public void run() {

        final JFrame frame = new JFrame(this.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        frame.setContentPane(panel);

        this.initializeFrame(frame);

        Components.center(frame);
        frame.setVisible(true);

    }

    /**
     * Returns the title used for this demonstration
     * 
     * @return the title used for this demonstration
     */
    protected String getTitle() {
        return this.getClass().getSimpleName();
    }

    /**
     * Create and populate the specified frame with your demonstration.
     * 
     * @param frame
     *            Root frame to populate with whatever you're demonstrating.
     */
    protected abstract void initializeFrame(final JFrame frame);

    /**
     * Runs the specified runnable on the EDT. The class provided must have a
     * zero-argument constructor.
     * 
     * @param klass
     *            the class from which a runnable is created
     */
    public static void launch(final Class<? extends Runnable> klass) {
        if (!Components.LookAndFeel.NIMBUS.activate()) {
            // Activate system look-and-feel if Nimbus is unavailable
            Components.LookAndFeel.SYSTEM.activate();
        }
        SwingUtilities.invokeLater(new Runnable() {

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

        });
    }
}
