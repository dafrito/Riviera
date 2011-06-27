package com.bluespot.examples.collections.observable;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.demonstration.BorderLayoutDemonstration;
import com.bluespot.demonstration.Demonstration;

/**
 * A very simple demo showing an {@link ObservableList} acting as a list model.
 * 
 * @author Aaron Faanes
 * 
 */
public final class ObservableListDemonstration extends BorderLayoutDemonstration {
	private final ObservableList<String> strings = new ObservableList<String>();

	private final JList list = new JList(this.strings);

	private final JButton addButton = new JButton(new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			strings.add("Hello, world! This is element " + strings.size());
			removeButton.setEnabled(true);
			if (list.getSelectedIndex() == -1) {
				list.setSelectedIndex(0);
			}
		}
	});

	private final JButton removeButton = new JButton(new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			assert !strings.isEmpty();
			int index = list.getSelectedIndex();
			assert index != -1;
			list.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (list.getSelectedIndex() == -1 && !strings.isEmpty()) {
						list.setSelectedIndex(0);
					}
				}
			});
			strings.remove(index);
			removeButton.setEnabled(!strings.isEmpty());
		}
	});

	@Override
	protected void preInitialize(final JFrame frame) {
		super.preInitialize(frame);
		frame.setPreferredSize(new Dimension(400, 400));
	}

	@Override
	protected JComponent newCenterPane() {
		return new JScrollPane(this.list);
	}

	@Override
	protected JComponent newSouthPane() {
		JPanel buttons = new JPanel();

		this.addButton.setText("Add");
		buttons.add(this.addButton);

		this.removeButton.setText("Remove");
		this.removeButton.setEnabled(false);
		buttons.add(this.removeButton);

		return buttons;
	}

	/**
	 * Creates a new {@link ObservableListDemonstration} using the
	 * {@link Demonstration} framework.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Demonstration.launch(ObservableListDemonstration.class);
	}

}
