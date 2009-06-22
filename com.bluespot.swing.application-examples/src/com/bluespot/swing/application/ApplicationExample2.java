
/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 * A "Hello World" application with a standard resource bundle.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class ApplicationExample2 extends Application {
    JFrame mainFrame = null;

    @Override
    protected void startup() {
        final JLabel label = new JLabel("[label.text resource]", SwingConstants.CENTER);
        label.setName("label");
        this.mainFrame = new JFrame();
        this.mainFrame.setName("mainFrame");
        this.mainFrame.add(label, BorderLayout.CENTER);
        this.mainFrame.addWindowListener(new MainFrameListener());
        this.mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final ResourceMap resourceMap = this.getContext().getResourceMap(this.getClass());
        resourceMap.injectComponents(this.mainFrame);
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
            ApplicationExample2.this.exit();
        }
    }

    public static void main(final String[] args) {
        Application.launch(ApplicationExample2.class, args);
    }
}
