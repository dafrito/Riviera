/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

/**
 * This is a very simple example of a reusable {@code @Actions} class. The code
 * defines a JComponent subclass called BaseScenePanel that defines two
 * 
 * @Actions: create and remove, that add/remove an icon from the scene panel.
 *           These actions are added to a right button popup menu for the
 *           component. [TBD: demo resource shadowing too]
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class ActionMapExample extends SingleFrameApplication {
    private static final Logger logger = Logger.getLogger(ActionMapExample.class.getName());
    private static final Insets zeroInsets = new Insets(0, 0, 0, 0);

    @Override
    protected void startup() {
        final View view = this.getMainView();
        view.setComponent(this.createMainPanel());
        this.show(view);
    }

    public static void main(final String[] args) {
        Application.launch(ActionMapExample.class, args);
    }

    private JComponent createMainPanel() {
        final JPanel mainPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();

        this.initGridBagConstraints(c);
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new BaseScenePanel(this), c);

        this.initGridBagConstraints(c);
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(new DerivedScenePanelA(this), c);

        this.initGridBagConstraints(c);
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        mainPanel.add(new DerivedScenePanelB(this), c);
        return mainPanel;
    }

    private void initGridBagConstraints(final GridBagConstraints c) {
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = ActionMapExample.zeroInsets;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
    }

    /**
     * A JComponent that renders a Scene and defines two {@code @Actions}:
     * <ul>
     * <li> {@code create} - adds a new Node to the scene to the right of the
     * last one
     * <li> {@code remove} - removes the selected Node
     * </ul>
     * These actions are added to a popup menu.
     * <p>
     * Subclasses can override the {@code create} and {@code remove} methods to
     * change the corresponding actions.
     */
    public static class BaseScenePanel extends JComponent implements PropertyChangeListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private final Scene scene;
        private final Application application;

        @Action
        public void create() {
            final ResourceMap resourceMap = this.getContext().getResourceMap(this.getClass(), BaseScenePanel.class);
            final Node node = new Node(resourceMap.getIcon("circleIcon"));
            final Insets insets = this.getInsets();
            final int iconGap = 8;
            final Rectangle r = this.getScene().getLastNodeBounds();
            final int x = insets.left + ((r.width == 0) ? 0 : (r.x + r.width + iconGap));
            final int y = insets.top;
            node.setLocation(new Point(x, y));
            this.getScene().add(node);
        }

        @Action
        public void remove() {
            this.getScene().remove(this.getScene().getSelectedNode());
        }

        public BaseScenePanel(final Application application) {
            if (application == null) {
                throw new IllegalArgumentException("null applicaiton");
            }
            this.application = application;
            this.setLayout(new GridBagLayout());
            this.setBorder(BorderFactory.createTitledBorder(this.getClass().getSimpleName()));
            final ActionMap actionMap = this.getContext().getActionMap(BaseScenePanel.class, this);
            final JPopupMenu menu = new JPopupMenu();
            menu.add(actionMap.get("create"));
            menu.add(actionMap.get("remove"));
            this.addMouseListener(new PopupMenuListener(menu));
            this.addMouseListener(new SelectionListener());
            this.scene = new Scene();
            this.scene.addPropertyChangeListener(this);
        }

        private class SelectionListener extends MouseAdapter {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (!e.isPopupTrigger()) {
                    final Node node = BaseScenePanel.this.getScene().nodeAt(e.getX(), e.getY());
                    if (node != null) {
                        BaseScenePanel.this.getScene().setSelectedNode(node);
                    }
                }
            }
        }

        protected final Scene getScene() {
            return this.scene;
        }

        protected final Application getApplication() {
            return this.application;
        }

        protected final ApplicationContext getContext() {
            return this.application.getContext();
        }

        public void propertyChange(final PropertyChangeEvent e) {
            if (e.getPropertyName() == null) {
                this.repaint();
            } else if (e.getPropertyName().equals("selectedNode")) {
                this.repaint(); // TBD oldSelection + newSelection bounds
            }
        }

        @Override
        protected void paintComponent(final Graphics g) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            for (final Node node : this.getScene().getNodes()) {
                final Icon icon = node.getIcon();
                final Point location = node.getLocation();
                icon.paintIcon(this, g, location.x, location.y);
                if (node == this.getScene().getSelectedNode()) {
                    g.setColor(this.getForeground());
                    final Rectangle r = node.getBounds();
                    g.drawRect(r.x, r.y, r.width, r.height);
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            final List<Node> nodes = this.getScene().getNodes();
            int maxX = 128;
            int maxY = 128;
            for (final Node node : nodes) {
                final Rectangle r = node.getBounds();
                maxX = Math.max(maxX, r.x);
                maxY = Math.max(maxY, r.y);
            }
            final Insets insets = this.getInsets();
            return new Dimension(maxX + insets.left + insets.right, maxY + insets.top + insets.bottom);
        }
    }

    public static class DerivedScenePanelA extends BaseScenePanel {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public DerivedScenePanelA(final Application application) {
            super(application);
        }

        @Override
        public void create() {
            final ResourceMap resourceMap = this.getContext().getResourceMap(this.getClass(), BaseScenePanel.class);
            final Node node = new Node(resourceMap.getIcon("squareIcon"));
            final Insets insets = this.getInsets();
            int x = insets.left;
            final int y = insets.top;
            final List<Node> nodes = this.getScene().getNodes();
            if (nodes.size() > 0) {
                final int iconGap = 8;
                final Node lastNode = nodes.get(nodes.size() - 1);
                final Rectangle r = lastNode.getBounds();
                x += r.x + r.width + iconGap;
            }
            node.setLocation(new Point(x, y));
            this.getScene().add(node);
        }
    }

    public static class DerivedScenePanelB extends BaseScenePanel {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public DerivedScenePanelB(final Application application) {
            super(application);
        }

        @Override
        public void create() {
            final ResourceMap resourceMap = this.getContext().getResourceMap(this.getClass(), BaseScenePanel.class);
            final Node node = new Node(resourceMap.getIcon("triangleIcon"));
            final Insets insets = this.getInsets();
            final int x = insets.left;
            int y = insets.top;
            final List<Node> nodes = this.getScene().getNodes();
            if (nodes.size() > 0) {
                final int iconGap = 8;
                final Node lastNode = nodes.get(nodes.size() - 1);
                final Rectangle r = lastNode.getBounds();
                y += r.y + r.height + iconGap;
            }
            node.setLocation(new Point(x, y));
            this.getScene().add(node);
        }
    }

    /*
     * This is essentially boilerplate: popup the specified menu when the
     * platform-specific mouse press/release event occurs.
     */
    private static class PopupMenuListener extends MouseAdapter {
        private final JPopupMenu menu;

        PopupMenuListener(final JPopupMenu menu) {
            this.menu = menu;
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            this.maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            this.maybeShowPopup(e);
        }

        private void maybeShowPopup(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                this.menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /*
     * Trivial scene model: just a list of Nodes and a selected node property.
     * Any change to the list of nodes is reported to listeners with a
     * PropertyChangeEvent whose name and old/new values are null. This is a
     * more or less conventional use of the class, it means that "that an
     * arbitrary set of [the source object's] properties have changed". See
     * http://java.sun.com/javase/6/docs/api/java/beans/PropertyChangeEvent.html
     */
    private static class Scene extends AbstractBean {
        private final List<Node> nodes = new ArrayList<Node>();
        private Node selectedNode = null;

        public void add(final Node node) {
            if (node == null) {
                throw new IllegalArgumentException("null node");
            }
            this.nodes.add(node);
            this.firePropertyChange(null, null, null);
        }

        public void remove(final Node node) {
            if (this.nodes.remove(node)) {
                this.firePropertyChange(null, null, null);
            }
        }

        public final List<Node> getNodes() {
            return Collections.unmodifiableList(this.nodes);
        }

        public final Rectangle getLastNodeBounds() {
            if (this.nodes.size() == 0) {
                return new Rectangle(0, 0, 0, 0);
            }
            final Node lastNode = this.nodes.get(this.nodes.size() - 1);
            return lastNode.getBounds();
        }

        public final Node nodeAt(final int x, final int y) {
            Node lastNode = null;
            for (final Node node : this.nodes) {
                if (node.getBounds().contains(x, y)) {
                    lastNode = node;
                }
            }
            return lastNode;
        }

        public Node getSelectedNode() {
            return this.selectedNode;
        }

        public void setSelectedNode(final Node selectedNode) {
            final Node oldValue = this.getSelectedNode();
            this.selectedNode = selectedNode;
            this.firePropertyChange("selectedNode", oldValue, this.selectedNode);
        }
    }

    /*
     * Trivial scene model element: an icon and the location it's to appear in.
     * The location property is bound.
     */
    private static class Node extends AbstractBean {
        private final Icon icon;
        private final Point location = new Point(0, 0);

        public Node(final Icon icon) {
            if (icon == null) {
                throw new IllegalArgumentException("null icon");
            }
            this.icon = icon;
        }

        public final Icon getIcon() {
            return this.icon;
        }

        public Point getLocation() {
            return new Point(this.location);
        }

        public void setLocation(final Point location) {
            if (location == null) {
                throw new IllegalArgumentException("null location");
            }
            final Point oldValue = this.getLocation();
            this.location.setLocation(location);
            this.firePropertyChange("location", oldValue, this.getLocation());
        }

        public final Rectangle getBounds() {
            final Point p = this.getLocation();
            return new Rectangle(p.x, p.y, this.icon.getIconWidth(), this.icon.getIconHeight());
        }
    }

}
