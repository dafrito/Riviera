/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.jdesktop.application.Application;

/**
 * A "Hello World" application. A simpler way to write an application like this
 * would be to use the {@code SingleFrameApplication} base class.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class ApplicationExample1 extends Application {
    JFrame mainFrame = null;

    @Override
    protected void startup() {
        final JLabel label = new JLabel("Hello World", SwingConstants.CENTER);
        label.setFont(new Font("LucidaSans", Font.PLAIN, 32));
        this.mainFrame = new JFrame(" Hello World ");
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
            ApplicationExample1.this.exit();
        }
    }

    public static void main(final String[] args) {
        Application.launch(ApplicationExample1.class, args);
    }
}
