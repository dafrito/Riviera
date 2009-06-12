package com.bluespot.demonstration;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.bluespot.swing.Components;

/**
 * A scaffold for construction simple {@link JFrame} components to demonstrate
 * functionality.
 * 
 * @author Aaron Faanes
 * 
 */
public abstract class AbstractDemonstration implements Runnable {

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

}
