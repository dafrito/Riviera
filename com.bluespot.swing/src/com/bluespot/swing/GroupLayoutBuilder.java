package com.bluespot.swing;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;

/**
 * Assists in building a layout with {@link GroupLayout}. The idiom here is to
 * build iteratively.
 * 
 * @author Aaron Faanes
 * 
 */
public class GroupLayoutBuilder {

	private final JComponent component;
	private final Group fieldGroup;
	private final Group horizontalGroup;
	private final Group labelGroup;
	private final GroupLayout layout;
	private final Group verticalGroup;

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
		this.labelGroup = this.layout.createParallelGroup();
		this.fieldGroup = this.layout.createParallelGroup();

		this.horizontalGroup.addGroup(this.layout.createSequentialGroup().addGroup(this.labelGroup).addGroup(
				this.fieldGroup));
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
	public GroupLayoutBuilder add(final JComponent comp) {
		this.horizontalGroup.addComponent(comp);
		this.verticalGroup.addComponent(comp);
		return this;
	}

	/**
	 * Adds a {@link JLabel} that contains the specified text to this builder.
	 * It will be laid out directly beneath any previous components.
	 * 
	 * @param string
	 *            the label text
	 * @return this builder object
	 * @see GroupLayoutBuilder#add(JComponent)
	 */
	public GroupLayoutBuilder add(final String string) {
		this.add(new JLabel(string));
		return this;
	}

	/**
	 * Adds a form field to this builder. It will be directly beneath any
	 * previous components.
	 * 
	 * @param labelName
	 *            the label text
	 * @param comp
	 *            the added component
	 * @return this builder object
	 */
	public GroupLayoutBuilder addLabeledPair(final String labelName, final JComponent comp) {
		final JLabel label = new JLabel(labelName);
		this.labelGroup.addComponent(label);
		this.fieldGroup.addComponent(comp);
		this.verticalGroup.addGroup(this.layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(label)
				.addComponent(comp));
		return this;
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
