package com.bluespot.collections.observable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.bluespot.collections.observable.list.ProxiedListModel;
import com.bluespot.demonstration.AbstractDemonstration;

public class ListModelAdapterDemonstration extends AbstractDemonstration {

	protected JList list;

	protected List<String> stringList;

	@Override
	public void initializeFrame(final JFrame frame) {
		frame.setLayout(new BorderLayout());
		frame.setSize(400, 400);

		frame.getContentPane().add(new JScrollPane(this.constructList()), BorderLayout.CENTER);

		final JPanel panel = new JPanel();

		final JButton addButton = new JButton("Add");
		final JButton removeButton = new JButton("Remove");
		removeButton.setEnabled(false);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				final List<String> listModelAdapter = ListModelAdapterDemonstration.this.stringList;
				listModelAdapter.add("Hello, world! This is element " + listModelAdapter.size());
				removeButton.setEnabled(true);
			}
		});
		panel.add(addButton);

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				final List<String> listModelAdapter = ListModelAdapterDemonstration.this.stringList;
				listModelAdapter.remove(0);
				if (listModelAdapter.isEmpty()) {
					removeButton.setEnabled(false);
				}
			}
		});
		panel.add(removeButton);

		frame.getContentPane().add(panel, BorderLayout.SOUTH);
	}

	private JList constructList() {
		final ProxiedListModel<String> listModel = new ProxiedListModel<String>();
		this.list = new JList(listModel);
		this.stringList = listModel;
		return this.list;
	}

	public static void main(final String[] args) {
		// We really should throw this in a invokeLater, but doing so kills
		// our callstack. For a more realistic run, use SwingUtilities.
		new ListModelAdapterDemonstration().run();
	}

}
