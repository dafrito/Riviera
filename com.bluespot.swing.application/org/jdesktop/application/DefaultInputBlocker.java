/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.jdesktop.application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.RootPaneContainer;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

final class DefaultInputBlocker extends Task.InputBlocker {
    private static final Logger logger = Logger.getLogger(DefaultInputBlocker.class.getName());
    private JDialog modalDialog = null;

    DefaultInputBlocker(final Task task, final Task.BlockingScope scope, final Object target,
            final ApplicationAction action) {
        super(task, scope, target, action);
    }

    private void setActionTargetBlocked(final boolean f) {
        final javax.swing.Action action = (javax.swing.Action) this.getTarget();
        action.setEnabled(!f);
    }

    private void setComponentTargetBlocked(final boolean f) {
        final Component c = (Component) this.getTarget();
        c.setEnabled(!f);
        // Note: can't set the cursor on a disabled component
    }

    /*
     * Accumulates a list of all of the descendants of root whose name begins
     * with "BlockingDialog"
     */
    private void blockingDialogComponents(final Component root, final List<Component> rv) {
        final String rootName = root.getName();
        if ((rootName != null) && rootName.startsWith("BlockingDialog")) {
            rv.add(root);
        }
        if (root instanceof Container) {
            for (final Component child : ((Container) root).getComponents()) {
                this.blockingDialogComponents(child, rv);
            }
        }
    }

    private List<Component> blockingDialogComponents(final Component root) {
        final List<Component> rv = new ArrayList<Component>();
        this.blockingDialogComponents(root, rv);
        return rv;
    }

    /*
     * Inject resources from both the Task's ResourceMap and the
     * ApplicationAction's ResourceMap. We add the action's name prefix to all
     * of the components before the second step.
     */
    private void injectBlockingDialogComponents(final Component root) {
        final ResourceMap taskResourceMap = this.getTask().getResourceMap();
        if (taskResourceMap != null) {
            taskResourceMap.injectComponents(root);
        }
        final ApplicationAction action = this.getAction();
        if (action != null) {
            final ResourceMap actionResourceMap = action.getResourceMap();
            final String actionName = action.getName();
            for (final Component c : this.blockingDialogComponents(root)) {
                c.setName(actionName + "." + c.getName());
            }
            actionResourceMap.injectComponents(root);
        }
    }

    /*
     * Creates a dialog whose visuals are initialized from the following Task
     * resources: BlockingDialog.title BlockingDialog.optionPane.icon
     * BlockingDialog.optionPane.message BlockingDialog.cancelButton.text
     * BlockingDialog.cancelButton.icon BlockingDialog.progressBar.stringPainted
     * 
     * If the Task has an Action then use the actionName as a prefix and look up
     * the resources again, in the action's ResourceMap (that's the @Action's
     * ApplicationActionMap ResourceMap really): actionName.BlockingDialog.title
     * actionName.BlockingDialog.optionPane.icon
     * actionName.BlockingDialog.optionPane.message
     * actionName.BlockingDialog.cancelButton.text
     * actionName.BlockingDialog.cancelButton.icon
     * actionName.BlockingDialog.progressBar.stringPainted
     */
    private JDialog createBlockingDialog() {
        final JOptionPane optionPane = new JOptionPane();
        /*
         * If the task can be canceled, then add the cancel button. Otherwise
         * clear the default OK button.
         */
        if (this.getTask().getUserCanCancel()) {
            final JButton cancelButton = new JButton();
            cancelButton.setName("BlockingDialog.cancelButton");
            final ActionListener doCancelTask = new ActionListener() {
                public void actionPerformed(final ActionEvent ignore) {
                    DefaultInputBlocker.this.getTask().cancel(true);
                }
            };
            cancelButton.addActionListener(doCancelTask);
            optionPane.setOptions(new Object[] { cancelButton });
        } else {
            optionPane.setOptions(new Object[] {}); // no OK button
        }
        /*
         * Create the JDialog. If the task can be canceled, then map closing the
         * dialog window to canceling the task.
         */
        final Component dialogOwner = (Component) this.getTarget();
        final String taskTitle = this.getTask().getTitle();
        final String dialogTitle = (taskTitle == null) ? "BlockingDialog" : taskTitle;
        final JDialog dialog = optionPane.createDialog(dialogOwner, dialogTitle);
        dialog.setModal(true);
        dialog.setName("BlockingDialog");
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final WindowListener dialogCloseListener = new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (DefaultInputBlocker.this.getTask().getUserCanCancel()) {
                    DefaultInputBlocker.this.getTask().cancel(true);
                    dialog.setVisible(false);
                }
            }
        };
        dialog.addWindowListener(dialogCloseListener);
        optionPane.setName("BlockingDialog.optionPane");
        this.injectBlockingDialogComponents(dialog);
        /*
         * Reset the JOptionPane's message property after injecting an initial
         * value for the message string.
         */
        this.recreateOptionPaneMessage(optionPane);
        dialog.pack();
        return dialog;
    }

    /*
     * Replace the default message panel with one that where the message text
     * can be selected and that includes a status bar for task progress. We
     * inject resources here because the JOptionPane#setMessage() doesn't add
     * the panel to the JOptionPane immediately.
     */
    private void recreateOptionPaneMessage(final JOptionPane optionPane) {
        final Object message = optionPane.getMessage();
        if (message instanceof String) {
            final Font font = optionPane.getFont();
            final JTextArea textArea = new JTextArea((String) message);
            textArea.setFont(font);
            final int lh = textArea.getFontMetrics(font).getHeight();
            final Insets margin = new Insets(0, 0, lh, 24); // top left bottom
                                                            // right
            textArea.setMargin(margin);
            textArea.setEditable(false);
            textArea.setWrapStyleWord(true);
            textArea.setBackground(optionPane.getBackground());
            final JPanel panel = new JPanel(new BorderLayout());
            panel.add(textArea, BorderLayout.CENTER);
            final JProgressBar progressBar = new JProgressBar();
            progressBar.setName("BlockingDialog.progressBar");
            progressBar.setIndeterminate(true);
            final PropertyChangeListener taskPCL = new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent e) {
                    if ("progress".equals(e.getPropertyName())) {
                        progressBar.setIndeterminate(false);
                        progressBar.setValue((Integer) e.getNewValue());
                        DefaultInputBlocker.this.updateStatusBarString(progressBar);
                    } else if ("message".equals(e.getPropertyName())) {
                        textArea.setText((String) e.getNewValue());
                    }
                }
            };
            this.getTask().addPropertyChangeListener(taskPCL);
            panel.add(progressBar, BorderLayout.SOUTH);
            this.injectBlockingDialogComponents(panel);
            optionPane.setMessage(panel);
        }
    }

    private void updateStatusBarString(final JProgressBar progressBar) {
        if (!progressBar.isStringPainted()) {
            return;
        }
        /*
         * The initial value of the progressBar string is the format. We save
         * the format string in a client property. The format String will be
         * applied four values (see below). The default format String is in
         * resources/Application.properties, it's:
         * "%02d:%02d, %02d:%02d remaining"
         */
        final String key = "progressBarStringFormat";
        if (progressBar.getClientProperty(key) == null) {
            progressBar.putClientProperty(key, progressBar.getString());
        }
        final String fmt = (String) progressBar.getClientProperty(key);
        if (progressBar.getValue() <= 0) {
            progressBar.setString("");
        } else if (fmt == null) {
            progressBar.setString(null);
        } else {
            final double pctComplete = progressBar.getValue() / 100.0;
            final long durSeconds = this.getTask().getExecutionDuration(TimeUnit.SECONDS);
            final long durMinutes = durSeconds / 60;
            final long remSeconds = (long) (0.5 + (durSeconds / pctComplete)) - durSeconds;
            final long remMinutes = remSeconds / 60;
            final String s = String.format(fmt, durMinutes, durSeconds - (durMinutes * 60), remMinutes, remSeconds
                    - (remMinutes * 60));
            progressBar.setString(s);
        }

    }

    private void showBusyGlassPane(final boolean f) {
        RootPaneContainer rpc = null;
        Component root = (Component) this.getTarget();
        while (root != null) {
            if (root instanceof RootPaneContainer) {
                rpc = (RootPaneContainer) root;
                break;
            }
            root = root.getParent();
        }
        if (rpc != null) {
            if (f) {
                final JMenuBar menuBar = rpc.getRootPane().getJMenuBar();
                if (menuBar != null) {
                    menuBar.putClientProperty(this, menuBar.isEnabled());
                    menuBar.setEnabled(false);
                }
                final JComponent glassPane = new BusyGlassPane();
                final InputVerifier retainFocusWhileVisible = new InputVerifier() {
                    @Override
                    public boolean verify(final JComponent c) {
                        return !c.isVisible();
                    }
                };
                glassPane.setInputVerifier(retainFocusWhileVisible);
                final Component oldGlassPane = rpc.getGlassPane();
                rpc.getRootPane().putClientProperty(this, oldGlassPane);
                rpc.setGlassPane(glassPane);
                glassPane.setVisible(true);
                glassPane.revalidate();
            } else {
                final JMenuBar menuBar = rpc.getRootPane().getJMenuBar();
                if (menuBar != null) {
                    final boolean enabled = (Boolean) menuBar.getClientProperty(this);
                    menuBar.putClientProperty(this, null);
                    menuBar.setEnabled(enabled);
                }
                final Component oldGlassPane = (Component) rpc.getRootPane().getClientProperty(this);
                rpc.getRootPane().putClientProperty(this, null);
                if (!oldGlassPane.isVisible()) {
                    rpc.getGlassPane().setVisible(false);
                }
                rpc.setGlassPane(oldGlassPane); // sets oldGlassPane.visible
            }
        }
    }

    /*
     * Note: unfortunately, the busy cursor is reset when the modal dialog is
     * shown.
     */
    private static class BusyGlassPane extends JPanel {
        /**
         * 
         */
        private static final long serialVersionUID = -4686799649120729788L;

        BusyGlassPane() {
            super(null, false);
            this.setVisible(false);
            this.setOpaque(false);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            final MouseInputListener blockMouseEvents = new MouseInputAdapter() {
            };
            this.addMouseMotionListener(blockMouseEvents);
            this.addMouseListener(blockMouseEvents);
        }
    }

    /*
     * If an action was specified then return the value of the
     * actionName.BlockingDialogTimer.delay resource from the action's
     * resourceMap. Otherwise return the value of the BlockingDialogTimer.delay
     * resource from the Task's ResourceMap. The latter's default in defined in
     * resources/Application.properties.
     */
    private int blockingDialogDelay() {
        Integer delay = null;
        final String key = "BlockingDialogTimer.delay";
        final ApplicationAction action = this.getAction();
        if (action != null) {
            final ResourceMap actionResourceMap = action.getResourceMap();
            final String actionName = action.getName();
            delay = actionResourceMap.getInteger(actionName + "." + key);
        }
        final ResourceMap taskResourceMap = this.getTask().getResourceMap();
        if ((delay == null) && (taskResourceMap != null)) {
            delay = taskResourceMap.getInteger(key);
        }
        return (delay == null) ? 0 : delay.intValue();
    }

    private void showBlockingDialog(final boolean f) {
        if (f) {
            if (this.modalDialog != null) {
                final String msg = String.format("unexpected InputBlocker state [%s] %s", f, this);
                DefaultInputBlocker.logger.warning(msg);
                this.modalDialog.dispose();
            }
            this.modalDialog = this.createBlockingDialog();
            final ActionListener showModalDialog = new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    if (DefaultInputBlocker.this.modalDialog != null) { // already
                                                                        // dismissed
                        DefaultInputBlocker.this.modalDialog.setVisible(true);
                    }
                }
            };
            final Timer showModalDialogTimer = new Timer(this.blockingDialogDelay(), showModalDialog);
            showModalDialogTimer.setRepeats(false);
            showModalDialogTimer.start();
        } else {
            if (this.modalDialog != null) {
                this.modalDialog.dispose();
                this.modalDialog = null;
            } else {
                final String msg = String.format("unexpected InputBlocker state [%s] %s", f, this);
                DefaultInputBlocker.logger.warning(msg);
            }
        }
    }

    @Override
    protected void block() {
        switch (this.getScope()) {
        case ACTION:
            this.setActionTargetBlocked(true);
            break;
        case COMPONENT:
            this.setComponentTargetBlocked(true);
            break;
        case WINDOW:
        case APPLICATION:
            this.showBusyGlassPane(true);
            this.showBlockingDialog(true);
            break;
        }
    }

    @Override
    protected void unblock() {
        switch (this.getScope()) {
        case ACTION:
            this.setActionTargetBlocked(false);
            break;
        case COMPONENT:
            this.setComponentTargetBlocked(false);
            break;
        case WINDOW:
        case APPLICATION:
            this.showBusyGlassPane(false);
            this.showBlockingDialog(false);
            break;
        }
    }
}
