/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * A simple demo of the @Action annotation.
 * <p>
 * This example only defines two @Actions explicitly: open and close. The open
 * action allows the user to choose a file and load it into the textPane, and
 * close just replaces the textPane's contents with the value of the
 * "defaultText" resource. The example inherits
 * 
 * @Actions named cut/copy/paste/delete and quit from the Application class. All
 *          of the actions are exposed in the menus and/or toolbar.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class SingleFrameExample4 extends SingleFrameApplication {
    private static Logger logger = Logger.getLogger(SingleFrameExample4.class.getName());
    private JEditorPane textPane;

    /**
     * Load the specified file into the textPane or popup an error dialog if
     * something goes wrong. The file that's loaded can't be saved, so there's
     * no harm in experimenting with the cut/copy/paste/delete editing actions.
     */
    @Action
    public void open() {
        final JFileChooser chooser = new JFileChooser();
        final int option = chooser.showOpenDialog(this.getMainFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            try {
                this.textPane.setPage(file.toURI().toURL());
            } catch (final MalformedURLException e) {
                // shouldn't happen unless the JRE fails
                SingleFrameExample4.logger.log(Level.WARNING, "File.toURI().toURL() failed", e);
            } catch (final IOException e) {
                this.showErrorDialog("can't open \"" + file + "\"", e);
            }
        }
    }

    /**
     * Replace the contents of the textPane with the value of the "defaultText"
     * resource.
     */
    @Action
    public void close() {
        final String defaultText = this.getContext().getResourceMap().getString("defaultText");
        this.textPane.setText(defaultText);
    }

    private void showErrorDialog(String message, final Exception e) {
        final String title = "Error";
        final int type = JOptionPane.ERROR_MESSAGE;
        message = "Error: " + message;
        JOptionPane.showMessageDialog(this.getMainFrame(), message, title, type);
    }

    private javax.swing.Action getAction(final String actionName) {
        return this.getContext().getActionMap().get(actionName);
    }

    private JMenu createMenu(final String menuName, final String[] actionNames) {
        final JMenu menu = new JMenu();
        menu.setName(menuName);
        for (final String actionName : actionNames) {
            if (actionName.equals("---")) {
                menu.add(new JSeparator());
            } else {
                final JMenuItem menuItem = new JMenuItem();
                menuItem.setAction(this.getAction(actionName));
                menuItem.setIcon(null);
                menu.add(menuItem);
            }
        }
        return menu;
    }

    private JMenuBar createMenuBar() {
        final String[] fileMenuActionNames = { "open", "close", "---", "quit" };
        final String[] editMenuActionNames = { "cut", "copy", "paste", "delete" };
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(this.createMenu("fileMenu", fileMenuActionNames));
        menuBar.add(this.createMenu("editMenu", editMenuActionNames));
        return menuBar;
    }

    private JComponent createToolBar() {
        final String[] toolbarActionNames = { "cut", "copy", "paste" };
        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for (final String actionName : toolbarActionNames) {
            final JButton button = new JButton();
            button.setAction(this.getAction(actionName));
            button.setFocusable(false);
            toolBar.add(button);
        }
        return toolBar;
    }

    private JComponent createMainPanel() {
        this.textPane = new JTextPane();
        this.textPane.setName("textPane");
        final JPanel panel = new JPanel(new BorderLayout());
        final JScrollPane scrollPane = new JScrollPane(this.textPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(this.createToolBar(), BorderLayout.NORTH);
        panel.setBorder(new EmptyBorder(0, 2, 2, 2)); // top, left, bottom,
                                                      // right
        return panel;
    }

    @Override
    protected void startup() {
        this.getMainFrame().setJMenuBar(this.createMenuBar());
        this.show(this.createMainPanel());
    }

    public static void main(final String[] args) {
        Application.launch(SingleFrameExample4.class, args);
    }
}
