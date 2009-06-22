
/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.TaskMonitor;

/**
 * A StatusBar panel that tracks a TaskMonitor. Although one could certainly
 * create a more elaborate StatusBar class, this one is sufficient for the
 * examples that need one.
 * <p>
 * This class loads resources from the ResourceBundle called {@code
 * resources.StatusBar}.
 * 
 */
public class StatusBar extends JPanel implements PropertyChangeListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final Insets zeroInsets = new Insets(0, 0, 0, 0);
    private final JLabel messageLabel;
    private final JProgressBar progressBar;
    private final JLabel statusAnimationLabel;
    private final int messageTimeout;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private final int busyAnimationRate;
    private int busyIconIndex = 0;

    /**
     * Constructs a panel that displays messages/progress/state properties of
     * the {@code taskMonitor's} foreground task.
     * 
     * @param taskMonitor
     *            the {@code TaskMonitor} whose {@code PropertyChangeEvents}
     *            {@code this StatusBar} will track.
     */
    public StatusBar(final Application app, final TaskMonitor taskMonitor) {
        super(new GridBagLayout());
        this.setBorder(new EmptyBorder(2, 0, 6, 0)); // top, left, bottom, right
        this.messageLabel = new JLabel();
        this.progressBar = new JProgressBar(0, 100);
        this.statusAnimationLabel = new JLabel();

        final ResourceMap resourceMap = app.getContext().getResourceMap(StatusBar.class);
        this.messageTimeout = resourceMap.getInteger("messageTimeout");
        this.messageTimer = new Timer(this.messageTimeout, new ClearOldMessage());
        this.messageTimer.setRepeats(false);
        this.busyAnimationRate = resourceMap.getInteger("busyAnimationRate");
        this.idleIcon = resourceMap.getIcon("idleIcon");
        for (int i = 0; i < this.busyIcons.length; i++) {
            this.busyIcons[i] = resourceMap.getIcon("busyIcons[" + i + "]");
        }
        this.busyIconTimer = new Timer(this.busyAnimationRate, new UpdateBusyIcon());
        this.progressBar.setEnabled(false);
        this.statusAnimationLabel.setIcon(this.idleIcon);

        final GridBagConstraints c = new GridBagConstraints();
        this.initGridBagConstraints(c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        this.add(new JSeparator(), c);

        this.initGridBagConstraints(c);
        c.insets = new Insets(6, 6, 0, 3); // top, left, bottom, right;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.messageLabel, c);

        this.initGridBagConstraints(c);
        c.insets = new Insets(6, 3, 0, 3); // top, left, bottom, right;
        this.add(this.progressBar, c);

        this.initGridBagConstraints(c);
        c.insets = new Insets(6, 3, 0, 6); // top, left, bottom, right;
        this.add(this.statusAnimationLabel, c);

        taskMonitor.addPropertyChangeListener(this);
    }

    public void setMessage(final String s) {
        this.messageLabel.setText((s == null) ? "" : s);
        this.messageTimer.restart();
    }

    private void initGridBagConstraints(final GridBagConstraints c) {
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = this.zeroInsets;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
    }

    private class ClearOldMessage implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            StatusBar.this.messageLabel.setText("");
        }
    }

    private class UpdateBusyIcon implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            StatusBar.this.busyIconIndex = (StatusBar.this.busyIconIndex + 1) % StatusBar.this.busyIcons.length;
            StatusBar.this.statusAnimationLabel.setIcon(StatusBar.this.busyIcons[StatusBar.this.busyIconIndex]);
        }
    }

    public void showBusyAnimation() {
        if (!this.busyIconTimer.isRunning()) {
            this.statusAnimationLabel.setIcon(this.busyIcons[0]);
            this.busyIconIndex = 0;
            this.busyIconTimer.start();
        }
    }

    public void stopBusyAnimation() {
        this.busyIconTimer.stop();
        this.statusAnimationLabel.setIcon(this.idleIcon);
    }

    /**
     * The TaskMonitor (constructor arg) tracks a "foreground" task; this method
     * is called each time a foreground task property changes.
     */
    public void propertyChange(final PropertyChangeEvent e) {
        final String propertyName = e.getPropertyName();
        if ("started".equals(propertyName)) {
            this.showBusyAnimation();
            this.progressBar.setEnabled(true);
            this.progressBar.setIndeterminate(true);
        } else if ("done".equals(propertyName)) {
            this.stopBusyAnimation();
            this.progressBar.setIndeterminate(false);
            this.progressBar.setEnabled(false);
            this.progressBar.setValue(0);
        } else if ("message".equals(propertyName)) {
            final String text = (String) (e.getNewValue());
            this.setMessage(text);
        } else if ("progress".equals(propertyName)) {
            final int value = (Integer) (e.getNewValue());
            this.progressBar.setEnabled(true);
            this.progressBar.setIndeterminate(false);
            this.progressBar.setValue(value);
        }
    }
}
