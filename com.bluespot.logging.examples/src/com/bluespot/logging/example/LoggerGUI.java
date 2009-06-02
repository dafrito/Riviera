package com.bluespot.logging.example;

import java.awt.event.MouseEvent;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.event.MouseInputAdapter;

import com.bluespot.demonstration.AbstractDemonstration;
import com.bluespot.demonstration.Runner;
import com.bluespot.logging.ListHandler;
import com.bluespot.swing.list.ProxiedListModel;

public class LoggerGUI extends AbstractDemonstration {

	protected Logger logger = Logger.getLogger("com.dafrito");

	public static void main(String[] args) {
		Runner.run(new LoggerGUI(), true);
	}

	private ListModel createLoggingListModel(Logger associatedLogger) {
		ProxiedListModel<LogRecord> listModel = new ProxiedListModel<LogRecord>();
		Handler handler = new ListHandler(listModel);
		associatedLogger.addHandler(handler);
		return listModel;
	}

	@Override
	public void initializeFrame(JFrame frame) {
		frame.setSize(400, 400);

		JSplitPane splitPane = new JSplitPane();
		frame.setContentPane(splitPane);

		JList list = new JList(this.createLoggingListModel(this.logger));

		splitPane.setLeftComponent(new JScrollPane(list));

		JPanel panel = new JPanel();
		panel.addMouseListener(new MouseInputAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				LoggerGUI.this.logger.info(event.toString());
			}

		});
		splitPane.setRightComponent(panel);
	}

}
