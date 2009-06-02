package com.bluespot.logging.ui;

import java.awt.Component;
import java.util.IllegalFormatException;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.bluespot.logging.CallStackHandler;
import com.bluespot.swing.tree.ProxiedTreeModel;
import com.bluespot.tree.Tree;

public final class LogViews {
	private LogViews() {
		throw new UnsupportedOperationException("Instantiation not allowed");
	}

	public static String asString(LogRecord record) {
		if (record == null)
			return "<null>";

		if (record.getParameters() != null) {
			try {
				return String.format(record.getMessage(), record.getParameters());
			} catch (IllegalFormatException e) {
				// Fall-through
			}
		}
		return record.getMessage();
	}

	public static JTree viewLogAsTree(Logger logger) {
		ProxiedTreeModel<LogRecord> treeModel = new ProxiedTreeModel<LogRecord>();
		logger.addHandler(new CallStackHandler(treeModel.getRoot()));
		JTree tree = new JTree(treeModel);

		tree.setCellRenderer(new DefaultTreeCellRenderer() {

			@Override
			public Component getTreeCellRendererComponent(JTree parentTree, Object value, boolean sel,
					boolean expanded, boolean leaf, int row, boolean cellHasFocus) {
				super.getTreeCellRendererComponent(parentTree, value, sel, expanded, leaf, row, cellHasFocus);
				LogRecord record = (LogRecord) ((Tree<?>) value).getValue();
				this.setText(asString(record));
				return this;
			}

		});

		tree.setRootVisible(false);

		return tree;
	}

}
