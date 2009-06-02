package com.bluespot.swing;

import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class ViewStack {

	private final Container container;
	private final CardLayout layout;
	private final ChangeListener listener = new ChangeListener() {

		public void stateChanged(final ChangeEvent e) {
			ViewStack.this.refresh();
		}

	};

	private SingleSelectionModel model;

	public ViewStack(final JComponent container) {
		this(container, null);
	}

	public ViewStack(final JComponent container, final SingleSelectionModel model) {
		this.container = container;
		this.layout = new CardLayout();
		this.container.setLayout(this.layout);
		this.setModel(model);
	}

	public SingleSelectionModel getModel() {
		return this.model;
	}

	public Container getParent() {
		return this.container;
	}

	public void setModel(final SingleSelectionModel model) {
		if (this.model != null) {
			this.model.removeChangeListener(this.listener);
		}
		this.model = model;
		if (this.model != null) {
			this.model.addChangeListener(this.listener);
		}
		this.refresh();
	}

	protected abstract String getName(int index);

	protected void refresh() {
		final int index = this.getModel().getSelectedIndex();
		if (index < 0) {
			return;
		}
		this.layout.show(this.container, this.getName(index));
	}

}
