package com.bluespot.swing;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;

public class GroupLayoutBuilder {

	private final JComponent component;
	private final Group fieldGroup;
	private Group horizontalGroup;
	private final Group labelGroup;
	private final GroupLayout layout;
	private Group verticalGroup;

	public GroupLayoutBuilder(final JComponent component) {
		this.component = component;
		this.layout = new GroupLayout(this.component);
		this.horizontalGroup = this.layout.createParallelGroup();
		this.verticalGroup = this.layout.createSequentialGroup();
		this.labelGroup = this.layout.createParallelGroup();
		this.fieldGroup = this.layout.createParallelGroup();

		this.horizontalGroup.addGroup(this.layout.createSequentialGroup().addGroup(this.labelGroup).addGroup(
				this.fieldGroup));
	}

	public void add(final JComponent comp) {
		this.horizontalGroup.addComponent(comp);
		this.verticalGroup.addComponent(comp);
	}

	public void add(final String string) {
		this.add(new JLabel(string));
	}

	public void addLabeledPair(final String labelName, final JComponent comp) {
		final JLabel label = new JLabel(labelName);
		this.labelGroup.addComponent(label);
		this.fieldGroup.addComponent(comp);
		this.verticalGroup.addGroup(this.layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(label)
				.addComponent(comp));
	}

	public void build() {
		this.layout.setAutoCreateContainerGaps(true);
		this.layout.setAutoCreateGaps(true);

		this.layout.setHorizontalGroup(this.horizontalGroup);
		this.layout.setVerticalGroup(this.verticalGroup);

		this.component.setLayout(this.layout);
	}

	public Group getHorizontalGroup() {
		return this.horizontalGroup;
	}

	public Group getVerticalGroup() {
		return this.verticalGroup;
	}

	public void setHorizontalGroup(final Group horizontalGroup) {
		this.horizontalGroup = horizontalGroup;
	}

	public void setVerticalGroup(final Group verticalGroup) {
		this.verticalGroup = verticalGroup;
	}

}
