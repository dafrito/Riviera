package com.bluespot.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ConcurrentModificationException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;

import com.bluespot.collections.observable.list.ListWorker;
import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.collections.observable.list.Observables;
import com.bluespot.geom.Geometry;
import com.bluespot.logic.Adapters;
import com.bluespot.logic.adapters.Adapter;

/**
 * A collection of utility methods dealing with Swing components.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Components {

    /**
     * An collection of built-in Swing look and feels. This saves clients the
     * trouble of manually setting the look and feel.
     * 
     * @author Aaron Faanes
     * 
     * @see javax.swing.LookAndFeel
     */
    public enum LookAndFeel {

        /**
         * Represents the Nimbus look-and-feel that was added in Java 6 Update
         * 10.
         */
        NIMBUS() {

            private volatile String name;

            @Override
            public String getName() {
                if (this.name == null) {
                    for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if (info.getClassName().contains("nimbus")) {
                            this.name = info.getClassName();
                        }
                    }
                }
                return this.name;
            }

        },

        /**
         * Represents the look-and-feel that emulates the system
         * 
         * @see UIManager#getSystemLookAndFeelClassName()
         */
        SYSTEM() {

            @Override
            public String getName() {
                return UIManager.getSystemLookAndFeelClassName();
            }
        };

        /**
         * Returns the name of the class that implements this look-and-feel. The
         * returned name, if non-null, can be directly used in a
         * {@link UIManager#setLookAndFeel(String)} invocation. If the returned
         * value is null, then no class name could be found.
         * 
         * 
         * @return the name of the class that implements this look-and-feel, or
         *         {@code null} if no class name could be found
         */
        public abstract String getName();

        /**
         * Sets the global look and feel to this look and feel.
         * 
         * @return {@code true} if setting the look-and-feel was successful,
         *         otherwise {@code false}
         */
        public boolean activate() {
            final String name = this.getName();
            if (name == null) {
                return false;
            }
            try {
                UIManager.setLookAndFeel(name);
                return true;
            } catch (final Exception e) {
                return false;
            }
        }
    }

    /**
     * Populates the specified container with elements in the
     * {@link ObservableList}. The list will be monitored and the container's
     * children will be synchronized with the specified list.
     * <p>
     * This does the necessary work to update the list on Swing's event dispatch
     * thread.
     * 
     * @param container
     *            the container to populate. It cannot contain any children and
     *            should not be modified once connected.
     * @param components
     *            the list of components. It may be freely modified and the
     *            containers children will be updated accordingly.
     * @return the listener added to {@code components}. It is already added to
     *         the specified list, and is only returned if you need to remove it
     *         later.
     * @throws NullPointerException
     *             if either argument is null
     * @throws IllegalArgumentException
     *             if {@code container} is not empty
     */
    public static ListDataListener connect(final JComponent container,
            final ObservableList<? extends JComponent> components) {
        return Observables.listenOnEdt(components, new ComponentListWorker(container));
    }

    /**
     * Populates the specified container with elements in the
     * {@link ObservableList}. The list will be monitored and the container's
     * children will be synchronized with the specified list.
     * <p>
     * The specified adapter will be used to generate the constraints for the
     * specified component.
     * <p>
     * This does the necessary work to update the list on Swing's event dispatch
     * thread.
     * 
     * @param container
     *            the container to populate. It cannot contain any children and
     *            should not be modified once connected.
     * @param components
     *            the list of components. It may be freely modified and the
     *            containers children will be updated accordingly.
     * @param adapter
     *            the adapter that creates the constraints for the specified
     *            component
     * @return the listener added to {@code components}. It is already added to
     *         the specified list, and is only returned if you need to remove it
     *         later.
     * @throws NullPointerException
     *             if either argument is null
     * @throws IllegalArgumentException
     *             if {@code container} is not empty
     */
    public static ListDataListener connectWithConstraints(final JComponent container,
            final ObservableList<? extends JComponent> components, final Adapter<? super JComponent, ?> adapter) {
        return Observables.listenOnEdt(components, new ComponentListWorker(container) {

            @Override
            protected Object getConstraints(final JComponent newValue) {
                return adapter.adapt(newValue);
            }

        });
    }

    /**
     * Connects a list of components with a container. A {@link CardLayout} will
     * be used in the specified container, and the selection model will
     * determine which element will be shown.
     * 
     * @param components
     *            the components that populate the container
     * @param model
     *            the selection model that determines the visible component
     * @param container
     *            the container that is populated by {@code components}
     */
    public static void connectWithCardLayout(final ObservableList<? extends JComponent> components,
            final SingleSelectionModel model, final JComponent container) {
        if (components == null) {
            throw new NullPointerException("components is null");
        }
        if (model == null) {
            throw new NullPointerException("model is null");
        }
        if (container == null) {
            throw new NullPointerException("container is null");
        }
        if (container.getComponentCount() > 0) {
            throw new IllegalArgumentException("comntainer is not empty");
        }
        final CardLayout layout = new CardLayout();
        container.setLayout(layout);
        final Adapter<Component, String> adapter = Adapters.componentName();
        Components.connectWithConstraints(container, components, adapter);
        final ChangeListener listener = new ChangeListener() {

            public void stateChanged(final ChangeEvent e) {
                if (container.getLayout() != layout) {
                    throw new IllegalStateException("Container does not have this card layout");
                }
                if (model.getSelectedIndex() >= components.size()) {
                    throw new IndexOutOfBoundsException("selected index >= components.size");
                }
                if (model.getSelectedIndex() >= 0) {
                    layout.show(container, adapter.adapt(components.get(model.getSelectedIndex())));
                }
                // If selectedIndex is < 0, we don't have anything selected, so
                // don't do anything.
            }
        };
        model.addChangeListener(listener);
        final Runnable firstTimeRefresh = new Runnable() {
            public void run() {
                listener.stateChanged(null);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            firstTimeRefresh.run();
        } else {
            SwingUtilities.invokeLater(firstTimeRefresh);
        }
    }

    /**
     * Levels of texture interpolation
     * 
     * @author Aaron Faanes
     * 
     */
    public static enum Interpolation {
        /**
         * Represents bicubic texture filtering.
         * 
         * @see RenderingHints#VALUE_INTERPOLATION_BICUBIC
         */
        BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),

        /**
         * Represents bilinear texture filtering.
         * 
         * @see RenderingHints#VALUE_INTERPOLATION_BILINEAR
         */
        BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),

        /**
         * Represents nearest-neighbor filtering.
         * 
         * @see RenderingHints#VALUE_INTERPOLATION_NEAREST_NEIGHBOR
         * 
         */
        NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        private final Object interpolationValue;

        private Interpolation(final Object interpolationValue) {
            this.interpolationValue = interpolationValue;
        }

        /**
         * Returns the value of this filtering strategy, as it is known to
         * Swing.
         * 
         * @return the value of this filtering strategy
         * 
         * @see Graphics2D#setRenderingHint(java.awt.RenderingHints.Key, Object)
         */
        public Object getValue() {
            return this.interpolationValue;
        }

        /**
         * Sets the graphics context to use this filtering strategy.
         * 
         * @param g
         *            the context to modify
         */
        public void set(final Graphics2D g) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.getValue());
        }
    }

    private Components() {
        throw new AssertionError("This class cannot be instantiated.");
    }

    /**
     * Centers the specified frame on the screen.
     * 
     * @param component
     *            the frame to center
     * @see JFrame#setLocationRelativeTo(java.awt.Component)
     */
    public static void center(final JFrame component) {
        component.setLocationRelativeTo(null);
    }

    /**
     * Returns the center of the specified component's drawing area.
     * 
     * @param component
     *            the {@link JComponent} used in this operation
     * @return the center {@link Point}
     * @see Components#getDrawableArea(JComponent)
     * @see Geometry#getCenter(Rectangle)
     */
    public static Point getCenter(final JComponent component) {
        return Geometry.getCenter(Components.getDrawableArea(component));
    }

    /**
     * Returns the {@link Rectangle} object that is equal to the available
     * drawing area of the specified {@link JComponent}, minus the insets of
     * that component.
     * 
     * @param component
     *            the {@code JComponent} used in this operation
     * @return the area available for drawing
     * @see JComponent#getInsets()
     */
    public static Rectangle getDrawableArea(final JComponent component) {
        final Rectangle rectangle = new Rectangle(component.getSize());
        Geometry.subtractInsets(rectangle, component.getInsets());
        return rectangle;
    }

    /**
     * Returns the {@link Dimension} object that is equal to the available
     * drawing area of the specified {@link JComponent}, minus the insets of
     * that component.
     * 
     * @param component
     *            the {@code JComponent} used in this operation
     * @return the dimensions available for drawing
     * @see JComponent#getInsets()
     */
    public static Dimension getDrawableSize(final JComponent component) {
        final Dimension dimension = component.getSize();
        Geometry.subtractInsets(dimension, component.getInsets());
        return dimension;
    }

    /**
     * Returns the index of the specified component in the specified parent.
     * 
     * @param parent
     *            the parent of the specified component
     * @param component
     *            the component that is the target of this search
     * @return the index of the specified component, otherwise {@code -1} is
     *         returned
     */
    public static int getIndexOf(final Container parent, final JComponent component) {
        for (int i = 0; i < parent.getComponentCount(); i++) {
            if (parent.getComponent(i) == component) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Sets whether the specified graphics context should use antialiasing.
     * 
     * @param g
     *            the graphics context to modify
     * @param isAntialiased
     *            {@code true} if antialiasing should be enabled, otherwise
     *            {@code false}
     * @see RenderingHints#KEY_ANTIALIASING
     */
    public static void setAntialias(final Graphics2D g, final boolean isAntialiased) {
        final Object antialiased = isAntialiased ? RenderingHints.VALUE_ANTIALIAS_ON
                : RenderingHints.VALUE_ANTIALIAS_OFF;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiased);
    }

    /**
     * Sets the Swing look and feel using the specified {@link LookAndFeel}
     * instance.
     * 
     * @param laf
     *            the look and feel to activates
     * @return {@code true} if the activation succeeded, otherwise {@code false}
     * @see LookAndFeel#activate()
     */
    public static boolean setLookAndFeel(final LookAndFeel laf) {
        return laf.activate();
    }

    /**
     * Sets the graphics context to use the specified level of texture
     * interpolation.
     * 
     * @param g
     *            the graphics context to modify
     * @param interpolation
     *            the level of texture interpolation
     * @see RenderingHints#KEY_INTERPOLATION
     */
    public static void setInterpolation(final Graphics2D g, final Interpolation interpolation) {
        interpolation.set(g);
    }

}

/**
 * A list worker that synchronizes a container with a list of children.
 * 
 * @author Aaron Faanes
 */
class ComponentListWorker implements ListWorker<JComponent> {

    private int lastSize;
    private final JComponent container;

    /**
     * Constructs a {@code ComponentListWorker} from the specified arguments.
     * 
     * @param container
     *            the container to populate. It cannot contain any children and
     *            should not be modified once connected.
     * @throws NullPointerException
     *             if either argument is null
     * @throws IllegalArgumentException
     *             if {@code container} is not empty
     */
    public ComponentListWorker(final JComponent container) {
        if (container == null) {
            throw new NullPointerException("container is null");
        }
        if (container.getComponentCount() > 0) {
            throw new IllegalArgumentException("container is not empty");
        }
        this.container = container;
    }

    public void elementAdded(final int index, final JComponent newValue) {
        this.checkForComodification();
        this.container.add(newValue, this.getConstraints(newValue), index);
        this.lastSize++;
        this.container.revalidate();
    }

    public void elementRemoved(final int index) {
        this.checkForComodification();
        this.container.remove(index);
        this.lastSize--;
        this.container.revalidate();
    }

    public void elementSet(final int index, final JComponent newValue) {
        this.checkForComodification();
        this.container.remove(index);
        this.container.add(newValue, this.getConstraints(newValue), index);
        this.container.revalidate();
    }

    /**
     * @param newValue
     *            the value that will have the returned constraints
     * @return the layout constraints for the specified component
     */
    protected Object getConstraints(final JComponent newValue) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("ComponentListWorker[container: %s]", this.container);
    }

    private void checkForComodification() {
        if (this.lastSize != this.container.getComponentCount()) {
            throw new ConcurrentModificationException("Component was modified");
        }
    }

}
