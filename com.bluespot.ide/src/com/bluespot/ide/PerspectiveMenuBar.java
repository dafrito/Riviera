package com.bluespot.ide;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PerspectiveMenuBar extends PerspectiveComponent {

	private final JMenuBar menuBar = new JMenuBar();

	public PerspectiveMenuBar(final PerspectiveManager manager) {
		super(manager);
		this.getManager().getPerspectives().addListDataListener(new ListDataListener() {
			public void contentsChanged(final ListDataEvent e) {
				PerspectiveMenuBar.this.refresh();
			}

			public void intervalAdded(final ListDataEvent e) {
				PerspectiveMenuBar.this.refresh();
			}

			public void intervalRemoved(final ListDataEvent e) {
				PerspectiveMenuBar.this.refresh();
			}
		});
		this.refresh();
	}

	public JMenuBar getMenuBar() {
		return this.menuBar;
	}

	public void populateMenuBar() {
		this.menuBar.add(Box.createHorizontalGlue());
		final ButtonGroup group = new ButtonGroup();
		for (final Perspective perspective : this.getManager().getPerspectives()) {
			final AbstractButton button = this.createMenuItem(perspective);
			group.add(button);
			this.menuBar.add(button);
			this.menuBar.add(Box.createRigidArea(new Dimension(5, 0)));
		}
	}

	public void refresh() {
		this.getMenuBar().removeAll();
		if (this.getManager().getCurrentPerspective() != null) {
			this.getManager().getCurrentPerspective().populateMenuBar(this.getMenuBar());
		}
		this.populateMenuBar();
		this.getMenuBar().repaint();
	}

	protected AbstractButton createMenuItem(final Perspective perspective) {
		final JRadioButton menuItem = new JRadioButton(perspective.getName());
		if (perspective.equals(this.getManager().getCurrentPerspective())) {
			menuItem.setSelected(true);
		}
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				PerspectiveMenuBar.this.getManager().showPerspective(perspective);
			}
		});
		return menuItem;
	}

	@Override
	protected void enterPerspective(final Perspective perspective) {
		this.refresh();
	}

}
