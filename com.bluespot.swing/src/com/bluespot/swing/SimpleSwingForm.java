package com.bluespot.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SimpleSwingForm {

    public static final int FORM_LABEL_COLUMN = 0;
    public static final int FORM_FIELD_COLUMN = 1;

    private int currentRow = -1;

    private JComponent component;
    private JPanel buttonPanel;
    private JPanel formPanel;

    public SimpleSwingForm() {
        this(new JPanel());
    }
    
    public SimpleSwingForm(JComponent component) {
        this.component = component;
        this.component.setLayout(new BorderLayout());
        
        this.formPanel = new JPanel(new GridBagLayout());
        this.component.add(this.formPanel, BorderLayout.PAGE_START);
        
        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.buttonPanel.setBorder(BorderFactory.createEmptyBorder(17, 0, 0, 0));
        this.component.add(this.buttonPanel, BorderLayout.PAGE_END);
    }
    
    protected void moveToNextRow() {
        this.currentRow++;
    }

    protected int getCurrentRow() {
        if (this.currentRow < 0) {
            // This would occur when moveToNextRow() hasn't been called first.
            this.currentRow = 0;
        }
        return this.currentRow;
    }
    
    public JButton addButton(String buttonName) {
        return this.addButton(new JButton(buttonName));
    }

    public JButton addButton(JButton button) {
        this.addToButtonRow(button);
        return button;
    }
    
    public JButton addInlineButton(JButton button) {
        this.moveToNextRow();
        this.addToForm(button, this.createFieldConstraints());
        return button;
    }

    public JTextField addTextField(String textFieldLabel) {
        return this.addTextField(textFieldLabel, new JTextField());
    }

    public JTextField addTextField(String textFieldLabel, JTextField textField) {
        return this.addTextField(new JLabel(textFieldLabel), textField);
    }

    public JTextField addTextField(JLabel textFieldLabel, JTextField textField) {
        this.moveToNextRow();

        textFieldLabel.setLabelFor(textField);
        
        this.addAsLabel(textFieldLabel);
        this.addAsField(textField);

        return textField;
    }

    protected void addAsLabel(JComponent labelComponent) {
        this.addToForm(labelComponent, this.createLabelConstraints());
    }

    protected void addAsField(JComponent fieldComponent) {
        this.addToForm(fieldComponent, this.createFieldConstraints());
    }

    protected GridBagConstraints createLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = SimpleSwingForm.FORM_LABEL_COLUMN;
        constraints.gridy = this.getCurrentRow();
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;

        return constraints;
    }

    public static final int ROW_GAP = 11;
    public static final int LABEL_TO_FIELD_GAP = 12;

    protected GridBagConstraints createFieldConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = SimpleSwingForm.FORM_FIELD_COLUMN;
        constraints.gridy = this.getCurrentRow();
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.BOTH;

        return constraints;
    }
    
    protected void addToForm(JComponent componentToAdd, GridBagConstraints constraints) {
        this.formPanel.add(componentToAdd, constraints);
    }
    
    protected void addToButtonRow(JComponent componentToAdd) {
        this.buttonPanel.add(componentToAdd);
    }

    public Component build() {
        return this.component;
    }

}
