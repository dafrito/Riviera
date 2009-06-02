package com.bluespot.swing;

import java.awt.Component;

import javax.swing.JTabbedPane;
import javax.swing.ListModel;

public abstract class TabView<C extends Component, E> extends NamingRepeater<C, E> {

	private int unnamedCounter = 1;

	public TabView(final JTabbedPane parent, final ListModel model) {
		super(parent, model);
	}

	public JTabbedPane getTabbedPane() {
		return (JTabbedPane) this.getParent();
	}

	public void select(final C view) {
		final int index = Components.getIndexOf(this.getParent(), view);
		if (index < 0) {
			throw new IllegalArgumentException("View is not contained in this TabView");
		}
		this.getTabbedPane().setSelectedIndex(index);
	}

	@Override
	protected void nameChanged(final String name, final C childComponent, final int index, final E childValue) {
		TabView.this.getTabbedPane().setTitleAt(index,
				name != null && !name.trim().equals("") ? name : "Untitled " + this.unnamedCounter++);
	}

}
