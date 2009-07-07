package com.bluespot.swing;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/**
 * Assists in building a layout with {@link GroupLayout}.
 * 
 * @author Aaron Faanes
 * 
 */
public class GroupLayoutBuilder {

    private final GroupLayout layout;

    private final JComponent component;

    private final ParallelGroup fieldsGroup;
    private final ParallelGroup horizontalGroup;

    /**
     * The horizontal group that contains all of the field labels.
     */
    private final ParallelGroup labelsGroup;

    private final SequentialGroup verticalGroup;

    /**
     * Constructs a new {@link GroupLayoutBuilder} using a newly created
     * {@link JPanel}.
     */
    public GroupLayoutBuilder() {
        this(new JPanel());
    }

    /**
     * Constructs a builder for the specified component.
     * 
     * @param component
     *            the component that will be targeted with this builder
     * @throws NullPointerException
     *             if {@code component} is null
     */
    public GroupLayoutBuilder(final JComponent component) {
        if (component == null) {
            throw new NullPointerException("component is null");
        }
        this.component = component;
        this.layout = new GroupLayout(this.component);
        this.horizontalGroup = this.layout.createParallelGroup();
        this.verticalGroup = this.layout.createSequentialGroup();
        this.labelsGroup = this.layout.createParallelGroup();
        this.fieldsGroup = this.layout.createParallelGroup();

        final SequentialGroup horizontalGroups = this.layout.createSequentialGroup();
        horizontalGroups.addGroup(this.labelsGroup);
        horizontalGroups.addGroup(this.fieldsGroup);

        this.horizontalGroup.addGroup(horizontalGroups);
    }

    /**
     * Adds a {@link JLabel} that contains the specified text to this builder.
     * It will be laid out directly beneath any previous components.
     * 
     * @param labelText
     *            the label text
     * @return this created {@code JLabel} object
     * @see GroupLayoutBuilder#row(JComponent)
     */
    public JLabel label(final String labelText) {
        final JLabel label = new JLabel(labelText);
        this.row(label);
        return label;
    }

    /**
     * Adds the specified component that is laid out directly beneath the
     * previous component. Repeated calls to this method would yield behavior
     * similar to {@link BoxLayout}.
     * 
     * @param comp
     *            the added component
     * @return this builder object
     */
    public GroupLayoutBuilder row(final JComponent comp) {
        this.horizontalGroup.addComponent(comp);
        this.verticalGroup.addComponent(comp);
        return this;
    }

    /**
     * Creates a {@link JTextField} and adds it, along with a label that
     * contains the specified text, to this layout. This is a cover method for
     * {@link #field(String, JComponent)}.
     * 
     * @param labelName
     *            the label text
     * @return the created {@link JTextField} object
     */
    public JTextField field(final String labelName) {
        final JTextField textField = new JTextField();
        this.field(labelName, textField);
        return textField;
    }

    private boolean labelHasTrailingColon(final String label) {
        return label.matches("^.+:\\s*$");
    }

    /**
     * Adds a form field to this builder. This will lay out the specified
     * component and a label in a new row. The specified component will be to
     * the right of the label.
     * 
     * @param labelName
     *            the label text
     * @param child
     *            the added component
     * @return this builder object
     */
    public GroupLayoutBuilder field(final String labelName, final JComponent child) {
        String modifiedLabelText = labelName;
        if (!this.labelHasTrailingColon(modifiedLabelText)) {
            modifiedLabelText += ":";
        }
        final JLabel label = new JLabel(modifiedLabelText);
        label.setLabelFor(child);
        this.labelsGroup.addComponent(label);
        this.fieldsGroup.addComponent(child);

        final ParallelGroup fieldGroup = this.layout.createParallelGroup(Alignment.BASELINE);
        fieldGroup.addComponent(label);
        fieldGroup.addComponent(child);
        this.verticalGroup.addGroup(fieldGroup);

        return this;
    }

    /**
     * Creates and adds a {@link JButton} to this builder. The created button
     * will use the specified text as its label.
     * 
     * @param buttonText
     *            the text used in the created button
     * @return the created {@code JButton} object
     */
    public JButton button(final String buttonText) {
        final JButton button = new JButton(buttonText);

        this.fieldsGroup.addGroup(Alignment.TRAILING, this.layout.createSequentialGroup().addComponent(button));
        this.verticalGroup.addComponent(button);

        return button;
    }

    /**
     * Finishes any work on this builder and returns the laid-out component.
     * 
     * @return the constructed component
     */
    public JComponent build() {
        this.layout.setAutoCreateContainerGaps(true);
        this.layout.setAutoCreateGaps(true);

        this.layout.setHorizontalGroup(this.horizontalGroup);
        this.layout.setVerticalGroup(this.verticalGroup);

        this.component.setLayout(this.layout);
        return this.component;
    }

}
