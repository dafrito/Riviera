
/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;

/**
 * Demonstrates creating a Task that runs periodically. The Tasks's {@code
 * process} method runs on the EDT, every period milliseconds.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class PeriodicTaskExample extends SingleFrameApplication {
    JLabel label = null;

    class MyPeriodicTask extends Task<Void, Void> {
        private final long period;

        MyPeriodicTask(final long period) {
            super(PeriodicTaskExample.this);
            this.period = period;
        }

        @Override
        public Void doInBackground() throws InterruptedException {
            while (!this.isCancelled()) {
                Thread.sleep(this.period);
                this.publish((Void) null);
            }
            return null;
        }

        @Override
        public void process(final List<Void> ignored) {
            final long dt = this.getExecutionDuration(TimeUnit.MILLISECONDS);
            PeriodicTaskExample.this.label.setText("Elapsed time: " + dt);
        }
    }

    @Override
    protected void startup() {
        this.label = new JLabel("Ready...");
        this.show(this.label);
    }

    @Override
    protected void ready() {
        this.getContext().getTaskService().execute(new MyPeriodicTask(500L));
    }

    public static void main(final String[] args) {
        Application.launch(PeriodicTaskExample.class, args);
    }
}
