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

	@Override
	public void initializeFrame(final JFrame frame) {
		frame.setSize(400, 400);

		final JSplitPane splitPane = new JSplitPane();
		frame.setContentPane(splitPane);

		final JList list = new JList(this.createLoggingListModel(this.logger));

		splitPane.setLeftComponent(new JScrollPane(list));

		final JPanel panel = new JPanel();
		panel.addMouseListener(new MouseInputAdapter() {

			@Override
			public void mouseClicked(final MouseEvent event) {
				LoggerGUI.this.logger.info(event.toString());
			}

		});
		splitPane.setRightComponent(panel);
	}

	private ListModel createLoggingListModel(final Logger associatedLogger) {
		final ProxiedListModel<LogRecord> listModel = new ProxiedListModel<LogRecord>();
		final Handler handler = new ListHandler(listModel);
		associatedLogger.addHandler(handler);
		return listModel;
	}

	public static void main(final String[] args) {
		Runner.run(new LoggerGUI(), true);
	}

}
