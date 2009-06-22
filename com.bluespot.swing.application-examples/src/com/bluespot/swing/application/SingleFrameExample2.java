/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import javax.swing.JLabel;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class SingleFrameExample2 extends SingleFrameApplication {
    @Override
    protected void startup() {
        final JLabel label = new JLabel();
        label.setName("label");
        this.show(label);
    }

    public static void main(final String[] args) {
        Application.launch(SingleFrameExample2.class, args);
    }
}
