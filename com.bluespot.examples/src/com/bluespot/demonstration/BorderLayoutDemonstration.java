package com.bluespot.demonstration;

import java.awt.BorderLayout;
import java.util.ConcurrentModificationException;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A {@link Demonstration} that designed for rapid construction of panels using
 * {@link BorderLayout}. This provides hooks for each area of the border layout.
 * hook is expected to create a new {@link JComponent} for that area (north,
 * west, etc.).
 * 
 * @author Aaron Faanes
 * @see BorderLayout
 */
public abstract class BorderLayoutDemonstration extends Demonstration {

    @Override
    protected final JComponent newContentPane() {
        final BorderLayout layout = new BorderLayout();
        final JPanel panel = new JPanel(layout);
        if (panel.getLayout() != layout) {
            throw new ConcurrentModificationException("Panel's layout has been modified");
        }
        final JComponent centerPanel = this.newCenterPane();
        if (centerPanel != null) {
            panel.add(centerPanel, BorderLayout.CENTER);
        }

        final JComponent northPane = this.newNorthPane();
        if (northPane != null) {
            panel.add(northPane, BorderLayout.NORTH);
        }

        final JComponent southPane = this.newSouthPane();
        if (southPane != null) {
            panel.add(southPane, BorderLayout.SOUTH);
        }

        final JComponent westPane = this.newWestPane();
        if (westPane != null) {
            panel.add(westPane, BorderLayout.WEST);
        }

        final JComponent eastPane = this.newEastPane();
        if (eastPane != null) {
            panel.add(eastPane, BorderLayout.EAST);
        }

        return panel;
    }

    /**
     * Returns a new {@link JComponent} that will be aligned via
     * {@link BorderLayout#CENTER} to this demonstration's container.
     * <p>
     * This factory method is not allowed to return null.
     * 
     * @return a new {@code JComponent} that is positioned in the center of this
     *         demonstration's container.
     */
    protected JComponent newCenterPane() {
        return null;
    }

    /**
     * Returns a new {@link JComponent} that will be aligned via
     * {@link BorderLayout#NORTH} to this demonstration's container.
     * <p>
     * This factory method is not allowed to return null.
     * 
     * @return a new {@code JComponent} that is positioned in the top portion of
     *         this demonstration's container.
     */
    protected JComponent newNorthPane() {
        return null;
    }

    /**
     * Returns a new {@link JComponent} that will be aligned via
     * {@link BorderLayout#SOUTH} to this demonstration's container.
     * <p>
     * This factory method is not allowed to return null.
     * 
     * @return a new {@code JComponent} that is positioned in the southern,
     *         bottom portion of this demonstration's container.
     */
    protected JComponent newSouthPane() {
        return null;
    }

    /**
     * Returns a new {@link JComponent} that will be aligned via
     * {@link BorderLayout#WEST} to this demonstration's container.
     * <p>
     * This factory method is not allowed to return null.
     * 
     * @return a new {@code JComponent} that is positioned in the west,
     *         left-hand portion of this demonstration's container.
     */
    protected JComponent newWestPane() {
        return null;
    }

    /**
     * Returns a new {@link JComponent} that will be aligned via
     * {@link BorderLayout#EAST} to this demonstration's container.
     * <p>
     * This factory method is not allowed to return null.
     * 
     * @return a new {@code JComponent} that is positioned in the east,
     *         right-hand portion of this demonstration's container.
     */
    protected JComponent newEastPane() {
        return null;
    }

}
