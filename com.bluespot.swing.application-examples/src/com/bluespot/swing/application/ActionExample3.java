/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

/**
 * The {@code enabledProperty} {@code @Action} annotation parameter.
 * <p>
 * This example is nearly identical to {@link ActionExample1 ActionExample1}.
 * We've added a parameter to the {@code @Action} annotation for the {@code
 * clearTitle} action:
 * 
 * <pre>
 * &#064;Action(enabledProperty = &quot;clearEnabled&quot;)
 * public void clearTitle() {
 *     appFrame.setTitle(textField.getText());
 *     setClearEnabled(true);
 * }
 * </pre>
 * 
 * The annotation parameter names a bound property from the same class. When the
 * {@code clearEnabled} property is set to false, as it is after the window's
 * title has been cleared, the {@code clearTitle} {@code Action} is disabled.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class ActionExample3 extends Application {
    private JFrame appFrame = null;
    private JTextField textField = null;
    private boolean clearEnabled = false;

    @Action
    public void setTitle() {
        this.appFrame.setTitle(this.textField.getText());
        this.setClearEnabled(true);
    }

    @Action(enabledProperty = "clearEnabled")
    public void clearTitle() {
        this.appFrame.setTitle("");
        this.setClearEnabled(false);
    }

    public boolean isClearEnabled() {
        return this.clearEnabled;
    }

    public void setClearEnabled(final boolean clearEnabled) {
        final boolean oldValue = this.clearEnabled;
        this.clearEnabled = clearEnabled;
        this.firePropertyChange("clearEnabled", oldValue, this.clearEnabled);
    }

    @Override
    protected void startup() {
        this.appFrame = new JFrame("");
        this.textField = new JTextField("<Enter the window title here>");
        this.textField.setFont(new Font("LucidSans", Font.PLAIN, 32));
        final JButton clearTitleButton = new JButton("Set Window Title");
        final JButton setTitleButton = new JButton("Clear Window Title");
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(setTitleButton);
        buttonPanel.add(clearTitleButton);
        this.appFrame.add(this.textField, BorderLayout.CENTER);
        this.appFrame.add(buttonPanel, BorderLayout.SOUTH);

        /*
         * Lookup up the Actions for this class/object in the
         * ApplicationContext, and bind them to the GUI controls.
         */
        final ActionMap actionMap = this.getContext().getActionMap();
        setTitleButton.setAction(actionMap.get("setTitle"));
        this.textField.setAction(actionMap.get("setTitle"));
        clearTitleButton.setAction(actionMap.get("clearTitle"));

        this.appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.appFrame.pack();
        this.appFrame.setLocationRelativeTo(null);
        this.appFrame.setVisible(true);
    }

    public static void main(final String[] args) {
        Application.launch(ActionExample3.class, args);
    }
}
