package com.bluespot.logging.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.bluespot.ide.AbstractPerspective;
import com.bluespot.ide.PerspectiveAction;
import com.bluespot.swing.BeanView;
import com.bluespot.swing.Dialogs;
import com.bluespot.swing.TabView;
import com.bluespot.swing.Dialogs.CancelledException;
import com.bluespot.swing.list.ProxiedListModel;
import com.bluespot.tree.Tree;

public class LoggingPerspective extends AbstractPerspective {

	// TODO Implement hotspots - simple links to treepaths.
	// TODO Implement filters - logs that activate and deactivate based on
	// certain criteria
	// TODO How should we handle threads?

	protected final ProxiedListModel<Logger> loggers = new ProxiedListModel<Logger>();
	private final JTabbedPane logPanel = new JTabbedPane();

	public LoggingPerspective() {
		super("Logs");
		new TabView<JComponent, Logger>(this.logPanel, this.loggers) {

			@Override
			public JComponent createComponent(Logger logger) {
				try {
					final BeanView<LogRecord> recordView = new BeanView<LogRecord>(LogRecord.class);

					final JTree logTree = LogViews.viewLogAsTree(logger);

					JSplitPane container = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
					container.setResizeWeight(.9);

					container.setLeftComponent(new JScrollPane(logTree));
					container.setRightComponent(recordView);

					logTree.addTreeSelectionListener(new TreeSelectionListener() {

						public void valueChanged(TreeSelectionEvent e) {
							Tree<?> node = (Tree<?>) logTree.getLastSelectedPathComponent();
							recordView.setValue(node != null ? (LogRecord) node.getValue() : null);
						}

					});

					container.setName(logger.getName());

					return container;
				} catch (IntrospectionException e) {
					throw new UnsupportedOperationException(e);
				}

			}

		};
	}

	public LoggingPerspective(String... logs) {
		this();
		for (String log : logs) {
			this.addLogger(log);
		}
	}

	public JComponent getComponent() {
		return this.logPanel;
	}

	@Override
	public void populateMenuBar(JMenuBar menuBar) {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		fileMenu.add(new ExitAction());
		menuBar.add(fileMenu);

		JMenu loggingMenu = new JMenu("Logging");
		loggingMenu.setMnemonic(KeyEvent.VK_L);

		loggingMenu.add(new WatchPackageAction());
		loggingMenu.add(new UnwatchPackageAction());
		menuBar.add(loggingMenu);
	}

	public void addLogger(Logger logger) {
		this.addLogger(logger, logger.getLevel());
	}

	public void addLogger(Logger logger, Level level) {
		logger.setLevel(level);
		this.loggers.add(logger);
	}

	public void addLogger(String loggerName) {
		this.loggers.add(Logger.getLogger(loggerName));
	}

	public void addLogger(String loggerName, Level level) {
		this.addLogger(Logger.getLogger(loggerName), level);
	}

	public List<Logger> getLoggers() {
		return this.loggers;
	}

	public void removeLogger(String loggerName) {
		this.loggers.remove(Logger.getLogger(loggerName));
	}

	public void removeLogger(Logger logger) {
		this.loggers.remove(logger);
	}

	public abstract static class LoggingPerspectiveAction extends PerspectiveAction {

		public LoggingPerspectiveAction(String name) {
			super(name);
		}

		@Override
		public LoggingPerspective getPerspective() {
			return (LoggingPerspective) super.getPerspective();
		}

	}

	public static class UnwatchPackageAction extends LoggingPerspectiveAction {

		public UnwatchPackageAction() {
			super("Unwatch Package");
			this.putValue(Action.SHORT_DESCRIPTION, "Stops logging messages from the given package");
			this.putValue(Action.ACTION_COMMAND_KEY, "unwatchPackage");
			this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		}

		public void actionPerformed(ActionEvent e) {
			try {
				List<String> packages = new ArrayList<String>();
				for (Logger logger : this.getPerspective().getLoggers()) {
					packages.add(logger.getName());
				}
				this.getPerspective()
						.removeLogger(
								Dialogs.getSelection("Choose the package that you want to unwatch:", packages,
										"Unwatch Logger"));
			} catch (CancelledException e1) {
				return;
			}
		}

	}

	public static class WatchPackageAction extends LoggingPerspectiveAction {

		public WatchPackageAction() {
			super("Watch Package");
			this.putValue(Action.SHORT_DESCRIPTION, "Logs messages from the given package");
			this.putValue(Action.ACTION_COMMAND_KEY, "watchPackage");
			this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		}

		public void actionPerformed(ActionEvent e) {
			try {
				this.getPerspective().addLogger(Dialogs.getString("Insert the package name:"));
			} catch (CancelledException e1) {
				return;
			}
		}

	}
}
