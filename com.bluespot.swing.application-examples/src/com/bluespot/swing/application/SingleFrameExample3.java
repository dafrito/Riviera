/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * A SingleFrameApplication example with an exitListener.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class SingleFrameExample3 extends SingleFrameApplication {
    @Override
    protected void startup() {
        final ExitListener maybeExit = new ExitListener() {
            public boolean canExit(final EventObject e) {
                final Object source = (e != null) ? e.getSource() : null;
                final Component owner = (source instanceof Component) ? (Component) source : null;
                final int option = JOptionPane.showConfirmDialog(owner, "Really Exit?");
                return option == JOptionPane.YES_OPTION;
            }

            public void willExit(final EventObject e) {
            }
        };
        this.addExitListener(maybeExit);
        final JButton button = new JButton();
        button.setName("button");
        button.setAction(new AbstractAction() {
            /**
         * 
         */
            private static final long serialVersionUID = 1L;

            public void actionPerformed(final ActionEvent e) {
                SingleFrameExample3.this.exit();
            }
        });
        this.show(button);
    }

    public static void main(final String[] args) {
        Application.launch(SingleFrameExample3.class, args);
    }
}
