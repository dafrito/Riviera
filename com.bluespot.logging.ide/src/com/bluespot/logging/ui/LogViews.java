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

	public static String asString(final LogRecord record) {
		if (record == null) {
			return "<null>";
		}

		if (record.getParameters() != null) {
			try {
				return String.format(record.getMessage(), record.getParameters());
			} catch (final IllegalFormatException e) {
				// Fall-through
			}
		}
		return record.getMessage();
	}

	public static JTree viewLogAsTree(final Logger logger) {
		final ProxiedTreeModel<LogRecord> treeModel = new ProxiedTreeModel<LogRecord>();
		logger.addHandler(new CallStackHandler(treeModel.getRoot()));
		final JTree tree = new JTree(treeModel);

		tree.setCellRenderer(new DefaultTreeCellRenderer() {

			@Override
			public Component getTreeCellRendererComponent(final JTree parentTree, final Object value,
					final boolean sel, final boolean expanded, final boolean leaf, final int row,
					final boolean cellHasFocus) {
				super.getTreeCellRendererComponent(parentTree, value, sel, expanded, leaf, row, cellHasFocus);
				final LogRecord record = (LogRecord) ((Tree<?>) value).getValue();
				this.setText(LogViews.asString(record));
				return this;
			}

		});

		tree.setRootVisible(false);

		return tree;
	}

}
