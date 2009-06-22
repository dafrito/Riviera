
/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.SingleFrameApplication;

/**
 * A simple demonstration of the {@code @Action(selectedProperty)} annotation
 * parameter. The {@code selectedProperty} parameter names a bound boolean
 * property whose value is kept in sync with the value of the corresponding
 * ApplicationAction's {@code selectedProperty}, which in turn mirrors the value
 * of JToggleButtons that have been configured with that ApplicationAction.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class SelectedPropertyExample extends SingleFrameApplication {
    private static final String SELECTED_KEY = "SwingSelectedKey";
    private boolean selected = false;
    JCheckBox checkBox = null;
    JButton button = null;
    JRadioButton radioButton = null;
    JTextArea textArea = null;

    @Override
    protected void startup() {
        final ActionMap actionMap = this.getContext().getActionMap();
        this.radioButton = new JRadioButton(actionMap.get("toggleAction"));
        this.checkBox = new JCheckBox(actionMap.get("toggleAction"));
        this.button = new JButton(actionMap.get("buttonAction"));
        this.textArea = new JTextArea();
        this.textArea.setName("textArea");
        this.radioButton.setName("radioButton");
        final JPanel controlPanel = new JPanel();
        controlPanel.add(this.radioButton);
        controlPanel.add(this.checkBox);
        controlPanel.add(this.button);
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(this.textArea), BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        this.show(mainPanel);
    }

    @Action
    public void buttonAction() {
        this.setSelected(!this.isSelected());
    }

    @Action(selectedProperty = "selected")
    public void toggleAction() {
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(final boolean selected) {
        final boolean oldValue = this.selected;
        this.selected = selected;
        this.firePropertyChange("selected", oldValue, this.selected);
        final ApplicationAction cba = (ApplicationAction) this.checkBox.getAction();
        final String msg = String.format("%s.setSelected(%s)\n", this.getClass().getName(), this.selected)
                + String.format("checkBox.getAction().isSelected() %s\n", cba.isSelected())
                + String.format("checkBox.isSelected() %s\n", this.checkBox.isSelected())
                + String.format("radioButton.isSelected() %s\n", this.radioButton.isSelected());
        this.textArea.append(msg + "\n");
    }

    public static void main(final String[] args) {
        Application.launch(SelectedPropertyExample.class, args);
    }
}
