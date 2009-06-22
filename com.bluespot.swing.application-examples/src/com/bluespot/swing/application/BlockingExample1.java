/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.Task.BlockingScope;
import org.jdesktop.application.Task.InputBlocker;

/**
 * A demo of the {@code @Action} <i>block</i> options for background task. It's
 * an example of three of the {@code Action.Block} types:
 * 
 * <pre>
 * &#064;Action(block = Task.BlockingScope.ACTION)  
 * public Task blockAction() { ... }
 * 
 * &#064;Action(block = Task.BlockingScope.COMPONENT) 
 * public Task blockComponent() { ... }
 * 
 * &#064;Action(block = Task.BlockingScope.WINDOW) 
 * public Task blockWindow() { ... }
 * 
 * &#064;Action(block = Task.BlockingScope.APPLICATION)
 * public Task blockApplication() { ... }
 * </pre>
 * 
 * The first {@code BlockingScope.ACTION} {@code @Action} disables the
 * corresponding {@code Action} while {@code blockAction} method runs. When you
 * press the blockAction button or toolbar-button or menu item you'll observe
 * that all of the components are disabled. The {@code BlockingScope.COMPONENT}
 * version only disables the component that triggered the action. The {@code
 * Block.WINDOW} method uses a custom {@link Task.InputBlocker inputBlocker} to
 * temporarily block input to the by making the window's glass pane visible. And
 * the {@code Task.BlockingScope.APPLICATION} version pops up a modal dialog for
 * the action's duration. The blocking dialog's title/message/icon are defined
 * by resources from the ResourceBundle named {@code BlockingExample1}:
 * 
 * <pre>
 * BlockingDialog.title = Blocking Application 
 * BlockingDialog.message = Please wait patiently ...
 * Action.BlockingDialog.icon = wait.png
 * </pre>
 * 
 * <p>
 * All of the actions in this example just sleep for about 2 seconds, while
 * periodically updating their Task's message/progress properties.
 * 
 * <p>
 * This class loads resources from the ResourceBundle called {@code
 * BlockingExample1}. It depends on the example {@code StatusBar} class.
 * 
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 * @see ApplicationContext
 * @see Application
 * @see Action
 * @see Task
 * @see TaskMonitor
 * @see StatusBar
 */

public class BlockingExample1 extends SingleFrameApplication {
    private static Logger logger = Logger.getLogger(LocalStorage.class.getName());
    private JFrame mainFrame = null;
    private StatusBar statusBar = null;
    private BusyIndicator busyIndicator = null;

    @Override
    protected void startup() {
        this.statusBar = new StatusBar(this, this.getContext().getTaskMonitor());
        this.busyIndicator = new BusyIndicator();
        this.mainFrame = this.getMainFrame();
        this.mainFrame.setJMenuBar(this.createMenuBar());
        this.mainFrame.add(this.createToolBar(), BorderLayout.NORTH);
        this.mainFrame.add(this.createMainPanel(), BorderLayout.CENTER);
        this.mainFrame.add(this.statusBar, BorderLayout.SOUTH);
        this.mainFrame.setGlassPane(this.busyIndicator);
        this.show(this.mainFrame);
    }

    private ActionMap actionMap() {
        return this.getContext().getActionMap();
    }

    private JMenu createMenu(final String menuName, final String[] actionNames) {
        final JMenu menu = new JMenu();
        menu.setName(menuName);
        for (final String actionName : actionNames) {
            final JMenuItem menuItem = new JMenuItem();
            menuItem.setAction(this.actionMap().get(actionName));
            menu.add(menuItem);
        }
        return menu;
    }

    private JMenuBar createMenuBar() {
        final String[] demoMenuActionNames = { "blockAction", "blockComponent", "blockApplication", "blockWindow",
                "quit" };
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(this.createMenu("demoMenu", demoMenuActionNames));
        return menuBar;
    }

    private JComponent createToolBar() {
        final String[] toolbarActionNames = { "blockAction", "blockComponent", "blockApplication", "blockWindow", };
        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for (final String actionName : toolbarActionNames) {
            final JButton button = new JButton();
            button.setRequestFocusEnabled(false);
            button.setAction(this.actionMap().get(actionName));
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setName(actionName + "ToolBarButton");
            toolBar.add(button);
        }
        return toolBar;
    }

    private JComponent createMainPanel() {
        final JButton actionButton = new JButton(this.actionMap().get("blockAction"));
        final JButton componentButton = new JButton(this.actionMap().get("blockComponent"));
        final JButton applicationButton = new JButton(this.actionMap().get("blockApplication"));
        final JButton windowButton = new JButton(this.actionMap().get("blockWindow"));
        final JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 50));
        panel1.add(actionButton);
        panel1.add(componentButton);
        panel1.add(applicationButton);
        panel1.add(windowButton);
        final JPanel panel2 = new JPanel(new BorderLayout());
        panel2.add(new JSeparator(), BorderLayout.NORTH);
        panel2.add(panel1, BorderLayout.CENTER);
        panel2.setBorder(new EmptyBorder(0, 2, 0, 2)); // top, left, bottom,
        // right
        return panel2;
    }

    /*
     * Progress is interdeterminate for the first 150ms, then run for another
     * 7500ms, marking progress every 150ms.
     */
    private class DoNothingTask extends Task<Void, Void> {
        DoNothingTask() {
            super(BlockingExample1.this);
            this.setUserCanCancel(true);
        }

        @Override
        protected Void doInBackground() throws InterruptedException {
            for (int i = 0; i < 50; i++) {
                this.setMessage("Working... [" + i + "]");
                Thread.sleep(150L);
                this.setProgress(i, 0, 49);
            }
            Thread.sleep(150L);
            return null;
        }

        @Override
        protected void succeeded(final Void ignored) {
            this.setMessage("Done");
        }

        @Override
        protected void cancelled() {
            this.setMessage("Canceled");
        }

    }

    @Action(block = BlockingScope.ACTION)
    public Task blockAction() {
        return new DoNothingTask();
    }

    @Action(block = BlockingScope.COMPONENT)
    public Task blockComponent() {
        return new DoNothingTask();
    }

    @Action(block = BlockingScope.WINDOW)
    public Task blockWindow() {
        return new DoNothingTask();
    }

    @Action(block = BlockingScope.APPLICATION)
    public Task blockApplication() {
        final Task task = new DoNothingTask();
        task.setInputBlocker(new BusyIndicatorInputBlocker(task));
        return task;
    }

    public static void main(final String[] args) {
        Application.launch(BlockingExample1.class, args);
    }

    /*
     * This component is intended to be used as a GlassPane. It's start method
     * makes this component visible, consumes mouse and keyboard input, and
     * displays a spinning activity indicator animation. The stop method makes
     * the component not visible. The code for rendering the animation was
     * lifted from org.jdesktop.swingx.painter.BusyPainter. I've made some
     * simplifications to keep the example small.
     */
    private static class BusyIndicator extends JComponent implements ActionListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private int frame = -1; // animation frame index
        private final int nBars = 8;
        private final float barWidth = 6;
        private final float outerRadius = 28;
        private final float innerRadius = 12;
        private final int trailLength = 4;
        private final float barGray = 200f; // shade of gray, 0-255
        private final Timer timer = new Timer(65, this); // 65ms = animation

        // rate

        BusyIndicator() {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            final MouseInputListener blockMouseEvents = new MouseInputAdapter() {
            };
            this.addMouseMotionListener(blockMouseEvents);
            this.addMouseListener(blockMouseEvents);
            final InputVerifier retainFocusWhileVisible = new InputVerifier() {
                @Override
                public boolean verify(final JComponent c) {
                    return !c.isVisible();
                }
            };
            this.setInputVerifier(retainFocusWhileVisible);
        }

        public void actionPerformed(final ActionEvent ignored) {
            this.frame += 1;
            this.repaint();
        }

        void start() {
            this.setVisible(true);
            this.requestFocusInWindow();
            this.timer.start();
        }

        void stop() {
            this.setVisible(false);
            this.timer.stop();
        }

        @Override
        protected void paintComponent(final Graphics g) {
            final RoundRectangle2D bar = new RoundRectangle2D.Float(this.innerRadius, -this.barWidth / 2,
                    this.outerRadius, this.barWidth, this.barWidth, this.barWidth);
            // x, y, width, height, arc width,arc height
            final double angle = Math.PI * 2.0 / this.nBars; // between bars
            final Graphics2D g2d = (Graphics2D) g;
            g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = 0; i < this.nBars; i++) {
                // compute bar i's color based on the frame index
                Color barColor = new Color((int) this.barGray, (int) this.barGray, (int) this.barGray);
                if (this.frame != -1) {
                    for (int t = 0; t < this.trailLength; t++) {
                        if (i == ((this.frame - t + this.nBars) % this.nBars)) {
                            final float tlf = this.trailLength;
                            final float pct = 1.0f - ((tlf - t) / tlf);
                            final int gray = (int) ((this.barGray - (pct * this.barGray)) + 0.5f);
                            barColor = new Color(gray, gray, gray);
                        }
                    }
                }
                // draw the bar
                g2d.setColor(barColor);
                g2d.fill(bar);
                g2d.rotate(angle);
            }
        }
    }

    private class BusyIndicatorInputBlocker extends InputBlocker {
        BusyIndicatorInputBlocker(final Task task) {
            super(task, Task.BlockingScope.WINDOW, BlockingExample1.this.busyIndicator);
        }

        @Override
        protected void block() {
            BlockingExample1.this.busyIndicator.start();
        }

        @Override
        protected void unblock() {
            BlockingExample1.this.busyIndicator.stop();
        }
    }
}
