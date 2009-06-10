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

/**
 * Provides a way to rapidly construct a Swing form.
 * 
 * @author Aaron Faanes
 */
public class SimpleSwingForm {

	/**
	 * The column that contains all of the form's fields
	 */
	private static final int COLUMN_FORM_FIELD = 1;

	/**
	 * The column that contains all of the form's labels
	 */
	public static final int COLUMN_FORM_LABEL = 0;

	/**
	 * Specifies the size of the horizontal gap in between fields and labels.
	 */
	public static final int LABEL_TO_FIELD_GAP = 12;

	/**
	 * Specifies the size of the vertical gap in between a form's rows
	 */
	public static final int ROW_GAP = 11;

	private final JPanel buttonPanel;

	private final JComponent component;

	private int currentRow = -1;

	private final JPanel formPanel;

	/**
	 * Creates a new form using an empty component.
	 */
	public SimpleSwingForm() {
		this(new JPanel());
	}

	/**
	 * Creates a new form that populates the specified component. The specified
	 * component's layout will be reset and it will be populated with elements.
	 * As a consequence, you should not modify the component once you have
	 * passed it to this builder.
	 * 
	 * @param component
	 *            the component to populate
	 */
	public SimpleSwingForm(final JComponent component) {
		this.component = component;
		this.component.setLayout(new BorderLayout());

		this.formPanel = new JPanel(new GridBagLayout());
		this.component.add(this.formPanel, BorderLayout.PAGE_START);

		this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		this.buttonPanel.setBorder(BorderFactory.createEmptyBorder(17, 0, 0, 0));
		this.component.add(this.buttonPanel, BorderLayout.PAGE_END);
	}

	/**
	 * Adds the specified button to this form.
	 * 
	 * @param button
	 *            the button to add
	 */
	public void addButton(final JButton button) {
		this.addToButtonRow(button);
	}

	/**
	 * Adds a button with the specified label to this form.
	 * 
	 * @param buttonText
	 *            the text used for the button
	 * @return the created {@link JButton} object
	 * 
	 * @see JButton#JButton(String)
	 */
	public JButton addButton(final String buttonText) {
		final JButton button = new JButton(buttonText);
		this.addButton(button);
		return button;
	}

	/**
	 * Adds the specified button to this form. The button will be placed on the
	 * next row of this form.
	 * 
	 * @param button
	 *            the button to add
	 */
	public void addInlineButton(final JButton button) {
		this.moveToNextRow();
		this.addToForm(button, this.createFieldConstraints());
	}

	/**
	 * Adds the specified field and label to this form.
	 * 
	 * @param textFieldLabel
	 *            the label used for the specified field
	 * @param textField
	 *            the field to add
	 */
	public void addTextField(final JLabel textFieldLabel, final JTextField textField) {
		this.moveToNextRow();

		textFieldLabel.setLabelFor(textField);

		this.addAsLabel(textFieldLabel);
		this.addAsField(textField);
	}

	/**
	 * Adds a {@link JTextField} to this form with the specified label.
	 * 
	 * @param textFieldLabel
	 *            the label of the field
	 * @return the created {@code JTextField} object
	 * @see #addTextField(JLabel, JTextField)
	 */
	public JTextField addTextField(final String textFieldLabel) {
		final JTextField field = new JTextField();
		this.addTextField(textFieldLabel, field);
		return field;
	}

	/**
	 * Adds the specified field and label to this form.
	 * 
	 * @param textFieldLabel
	 *            the text of the label used for the specified field
	 * @param textField
	 *            the field to add
	 * @see #addTextField(JLabel, JTextField)
	 */
	public void addTextField(final String textFieldLabel, final JTextField textField) {
		this.addTextField(new JLabel(textFieldLabel), textField);
	}

	/**
	 * Builds this form.
	 * 
	 * @return the constructed form
	 */
	public Component build() {
		return this.component;
	}

	private void addAsField(final JComponent fieldComponent) {
		this.addToForm(fieldComponent, this.createFieldConstraints());
	}

	private void addAsLabel(final JComponent labelComponent) {
		this.addToForm(labelComponent, this.createLabelConstraints());
	}

	private void addToButtonRow(final JComponent componentToAdd) {
		this.buttonPanel.add(componentToAdd);
	}

	private void addToForm(final JComponent componentToAdd, final GridBagConstraints constraints) {
		this.formPanel.add(componentToAdd, constraints);
	}

	private GridBagConstraints createFieldConstraints() {
		final GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = SimpleSwingForm.COLUMN_FORM_FIELD;
		constraints.gridy = this.getCurrentRow();
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;

		return constraints;
	}

	private GridBagConstraints createLabelConstraints() {
		final GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = SimpleSwingForm.COLUMN_FORM_LABEL;
		constraints.gridy = this.getCurrentRow();
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;

		return constraints;
	}

	private int getCurrentRow() {
		if (this.currentRow < 0) {
			// This would occur when moveToNextRow() hasn't been called first.
			this.currentRow = 0;
		}
		return this.currentRow;
	}

	private void moveToNextRow() {
		this.currentRow++;
	}

}
