package com.bluespot.demonstration;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AbstractDemonstration implements Runnable {
    
    protected String getFrameName() {
        return this.getClass().getSimpleName();
    }

    /**
     * @param frame Root frame to populate with whatever you're demonstrating.
     */
    protected void initializeFrame(JFrame frame) {
        // Do nothing; override this behavior in subclasses.
    }
    
    public void run() {

        JFrame frame = new JFrame(this.getFrameName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        frame.setContentPane(panel);
        
        this.initializeFrame(frame);

        frame.setVisible(true);

    }

}
