/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Application;

/**
 * Demonstrate the use of an ExitListener.
 * <p>
 * This class adds an {@code Application.ExitListener} that asks the user to
 * confirm exiting the application. The ExitListener is defined like this:
 * 
 * <pre>
 * lass MaybeExit implements Application.ExitListener {
 *    public boolean canExit(EventObject e) {
 *        Object source = (e != null) ? e.getSource() : null;
 *        Component owner = (source instanceof Component) ? (Component)source : null;
 *        int option = JOptionPane.showConfirmDialog(owner, &quot;Really Exit?&quot;);
 *        return option == JOptionPane.YES_OPTION;
 *    }
 *    public void willExit(EventObject e) { }
 * 
 * </pre>
 * 
 * When the user attempts to close the window,
 * {@link Application#exit(EventObject) Application.exit} is called by JFrame's
 * WindowListener. The {@code exit} method checks the {@code
 * ExitListener.canExit} methods and aborts the attempt to exit if any of them
 * return false.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class ExitExample1 extends Application {
    JFrame mainFrame = null;

    @Override
    protected void startup() {
        this.addExitListener(new MaybeExit());
        final JLabel label = new JLabel(" Close the Window to Exit ", SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(100, 100, 100, 100));
        this.mainFrame = new JFrame("ExitExample1");
        this.mainFrame.add(label, BorderLayout.CENTER);
        this.mainFrame.addWindowListener(new MainFrameListener());
        this.mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.mainFrame.pack();
        this.mainFrame.setLocationRelativeTo(null); // center the window
        this.mainFrame.setVisible(true);
    }

    @Override
    protected void shutdown() {
        this.mainFrame.setVisible(false);
    }

    private class MainFrameListener extends WindowAdapter {
        @Override
        public void windowClosing(final WindowEvent e) {
            ExitExample1.this.exit(e);
        }
    }

    private class MaybeExit implements Application.ExitListener {
        public boolean canExit(final EventObject e) {
            final Object source = (e != null) ? e.getSource() : null;
            final Component owner = (source instanceof Component) ? (Component) source : null;
            final int option = JOptionPane.showConfirmDialog(owner, "Really Exit?");
            return option == JOptionPane.YES_OPTION;
        }

        public void willExit(final EventObject e) {
            // cleanup
        }
    }

    public static void main(final String[] args) {
        Application.launch(ExitExample1.class, args);
    }
}
