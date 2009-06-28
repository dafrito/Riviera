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
 * <p>
 * It is not required to override all hook methods, but if a method is
 * overrided, it must return a non-null {@link JComponent}. Null values will
 * immediately result in {@link NullPointerException} objects being thrown.
 * 
 * @author Aaron Faanes
 * @see BorderLayout
 */
public abstract class BorderLayoutDemonstration extends Demonstration {

    /**
     * This component represents an empty area. We use it instead of null to
     * indicate that areas should not be added to the container.
     * {@link JComponent#equals(Object)}
     */
    private static final JComponent EMPTY = new JComponent() {
        // Empty by design
    };

    private void checkNullComponent(final String name, final JComponent component) {
        if (component == null) {
            throw new NullPointerException(name + " is null");
        }
    }

    @Override
    protected final JComponent newContentPane() {
        final BorderLayout layout = new BorderLayout();
        final JPanel panel = new JPanel(layout);
        if (panel.getLayout() != layout) {
            throw new ConcurrentModificationException("Panel's layout has been modified");
        }
        final JComponent centerPanel = this.newCenterPane();
        if (centerPanel != EMPTY) {
            this.checkNullComponent("centerPanel", centerPanel);
            panel.add(centerPanel, BorderLayout.CENTER);
        }

        final JComponent northPane = this.newNorthPane();
        if (northPane != EMPTY) {
            this.checkNullComponent("northPane", northPane);
            panel.add(northPane, BorderLayout.NORTH);
        }

        final JComponent southPane = this.newSouthPane();
        if (southPane != EMPTY) {
            this.checkNullComponent("southPane", southPane);
            panel.add(southPane, BorderLayout.SOUTH);
        }

        final JComponent westPane = this.newWestPane();
        if (westPane != EMPTY) {
            this.checkNullComponent("westPane", westPane);
            panel.add(westPane, BorderLayout.WEST);
        }

        final JComponent eastPane = this.newEastPane();
        if (eastPane != EMPTY) {
            this.checkNullComponent("eastPane", eastPane);
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
        return EMPTY;
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
        return EMPTY;
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
        return EMPTY;
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
        return EMPTY;
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
        return EMPTY;
    }

}
