/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * A demo that shows the use of SingleFrameApplication secondary windows.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class SingleFrameExample6 extends SingleFrameApplication {
    private final List<Window> windows = new ArrayList<Window>(Collections.nCopies(3, (Window) null));

    // Lazily create an element of the windows List and return it
    private Window getWindow(final int n, final Class<? extends Window> windowClass) {
        Window window = this.windows.get(n);
        if (window == null) {
            try {
                window = windowClass.newInstance();
            } catch (final Exception e) {
                throw new Error("HCTB", e);
            }
            final JLabel label = new JLabel();
            final JButton button = new JButton();
            button.setAction(this.getAction("hideWindow"));
            window.setName("window" + n);
            label.setName("label" + n);
            button.setName("button" + n);
            window.add(label, BorderLayout.CENTER);
            window.add(button, BorderLayout.SOUTH);
            this.windows.set(n, window);
        }
        return window;
    }

    @Action
    public void hideWindow(final ActionEvent e) {
        if (e.getSource() instanceof Component) {
            final Component source = (Component) e.getSource();
            final Window window = SwingUtilities.getWindowAncestor(source);
            if (window != null) {
                window.setVisible(false);
            }
        }
    }

    @Action
    public void showWindow0() {
        this.show((JFrame) this.getWindow(0, JFrame.class));
    }

    @Action
    public void showWindow1() {
        this.show((JDialog) this.getWindow(1, JDialog.class));
    }

    @Action
    public void showWindow2() {
        this.show((JDialog) this.getWindow(2, JDialog.class));
    }

    @Action
    public void disposeSecondaryWindows() {
        for (int i = 0; i < this.windows.size(); i++) {
            final Window window = this.windows.get(i);
            if (window != null) {
                this.windows.set(i, null);
                window.dispose();
            }
        }
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
        final JMenuBar menuBar = new JMenuBar();
        final String[] viewMenuActionNames = { "showWindow0", "showWindow1", "showWindow2", "disposeSecondaryWindows",
                "---", "quit" };
        menuBar.add(this.createMenu("viewMenu", viewMenuActionNames));
        return menuBar;
    }

    @Override
    protected void startup() {
        this.getMainFrame().setJMenuBar(this.createMenuBar());
        final JLabel label = new JLabel();
        label.setName("mainLabel");
        this.show(label);
    }

    public static void main(final String[] args) {
        Application.launch(SingleFrameExample6.class, args);
    }
}
