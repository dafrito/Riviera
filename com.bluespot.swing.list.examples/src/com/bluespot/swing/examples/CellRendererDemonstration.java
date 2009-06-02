package com.bluespot.swing.examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.bluespot.demonstration.AbstractDemonstration;
import com.bluespot.demonstration.Runner;
import com.bluespot.swing.list.ProxiedListModel;

public class CellRendererDemonstration extends AbstractDemonstration {

	public static void main(String[] args) {
		Runner.run(new CellRendererDemonstration(), true);
	}

	protected JList list;

	protected List<String> stringList;
	protected static int counter;

	public class MyCellRenderer extends JLabel implements ListCellRenderer {

		public Component getListCellRendererComponent(JList sourceList, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			this.setOpaque(true);
			this.setText(String.valueOf(value.toString().length()));
			if (isSelected) {
				this.setForeground(Color.WHITE);
				this.setBackground(Color.BLUE);
				this.setText(value.toString());
			} else {
				this.setForeground(Color.BLACK);
				this.setBackground(Color.WHITE);
			}
			return this;
		}

	}

	private JList constructList() {
		ProxiedListModel<String> listModelAdapter = new ProxiedListModel<String>();
		this.list = new JList(listModelAdapter);
		this.stringList = listModelAdapter;

		this.list.setCellRenderer(new MyCellRenderer());

		this.populateList(this.stringList);

		return this.list;
	}

	private void populateList(List<String> targetList) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] charArray = alphabet.toCharArray();
		String[] stringArray = new String[26];
		int index = 0;
		for (char c : charArray)
			stringArray[index++] = String.valueOf(c);
		Collections.addAll(targetList, stringArray);
	}

	@Override
	public void initializeFrame(JFrame frame) {
		frame.setLayout(new BorderLayout());
		frame.setSize(400, 400);
		frame.getContentPane().add(new JScrollPane(this.constructList()), BorderLayout.CENTER);

		JPanel panel = new JPanel();

		final JButton addButton = new JButton("Add");
		final JButton removeButton = new JButton("Remove");
		removeButton.setEnabled(false);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<String> listModelAdapter = CellRendererDemonstration.this.stringList;
				listModelAdapter.add("Hello, world! This is element " + listModelAdapter.size());
				removeButton.setEnabled(true);
			}
		});
		panel.add(addButton);

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<String> listModelAdapter = CellRendererDemonstration.this.stringList;
				listModelAdapter.remove(0);
				if (listModelAdapter.isEmpty())
					removeButton.setEnabled(false);
			}
		});
		panel.add(removeButton);

		frame.getContentPane().add(panel, BorderLayout.SOUTH);
	}

}