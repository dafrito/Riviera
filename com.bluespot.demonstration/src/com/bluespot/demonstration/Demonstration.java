package com.bluespot.demonstration;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.bluespot.swing.Components;

/**
 * A simple utility that runs {@code Runnable}s and set ups the look and feel.
 * 
 * @author Aaron Faanes
 * 
 */
public abstract class Demonstration {

    private final void bootstrap() {
        final JFrame frame = new JFrame(this.getTitle());
        this.preInitialize(frame);
        if (this.initialize(frame)) {
            this.postInitialize(frame);
        }
    }

    /**
     * 
     * @param frame
     *            the target frame that is initialized with this demonstration
     */
    protected final void preInitialize(final JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Adds all children and lays out the specified frame. This method is
     * invoked after {@link #preInitialize(JFrame)} and before
     * {@link #postInitialize(JFrame)}.
     * <p>
     * The default implementation sets the content pane of the specified frame
     * to this demonstration's content pane, as returned by
     * {@link #getContentPane()}.
     * 
     * @param frame
     *            the target frame that is initialized with this demonstration
     * @return {@code true} if {@link #postInitialize(JFrame)} should be invoked
     */
    protected final boolean initialize(final JFrame frame) {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        frame.setContentPane(panel);
        panel.add(this.getContentPane());
        return true;
    }

    /**
     * Sets the size, position, and visibility of the specified frame. This
     * method is called immediately after {@link #initialize(JFrame)}.
     * <p>
     * The default implementation calls {@link JFrame#pack()} on the specified
     * frame, centers it, and shows it.
     * 
     * @param frame
     *            the target frame that is initialized with this demonstration
     */
    protected final void postInitialize(final JFrame frame) {
        frame.pack();
        Components.center(frame);
        frame.setVisible(true);
    }

    /**
     * Returns the title used for this demonstration
     * 
     * @return the title used for this demonstration
     */
    protected String getTitle() {
        return this.getClass().getSimpleName();
    }

    private JComponent component;

    /**
     * Returns the content pane used with this demonstration. This will cache a
     * created content pane, so {@link #newContentPane()} will only be called
     * once.
     * 
     * @return the content pane for this demonstration
     * 
     * @throws IllegalStateException
     *             if this method is invoked on a non-EDT thread
     * @throws NullPointerException
     *             if {@link #newContentPane()} returns a null content pane
     * @throws UnsupportedOperationException
     *             if {@link #newContentPane()} is not overridden
     */
    public final JComponent getContentPane() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Method must be invoked within the EDT");
        }
        if (this.component == null) {
            this.component = this.newContentPane();
            if (this.component == null) {
                throw new NullPointerException("newly created component is null");
            }
        }
        return this.component;
    }

    /**
     * Create a new content pane that contains your demonstration. If you're
     * using the new style of demonstrations, you must override this method. If,
     * instead, you're using the legacy form, then you must override
     * {@link #initialize(JFrame)}.
     * 
     * @return the created content pane
     * @throws UnsupportedOperationException
     *             if this method is not overridden
     */
    protected JComponent newContentPane() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Runs the specified runnable on the EDT. The class provided must have a
     * zero-argument constructor.
     * 
     * @param klass
     *            the class from which a runnable is created
     */
    public static void launch(final Class<? extends Demonstration> klass) {
        if (!Components.LookAndFeel.NIMBUS.activate()) {
            /*
             * Activate system look-and-feel if Nimbus is unavailable. If this
             * one fails, then we just give up and use the default.
             */
            Components.LookAndFeel.SYSTEM.activate();
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Demonstration demonstration = null;
                try {
                    demonstration = klass.newInstance();
                } catch (final InstantiationException e) {
                    /*
                     * Creating a new demonstration should never fail, and any
                     * occurrence always indicates programming errors. At any
                     * rate, this seems smarter than printing the stack trace.
                     */
                    throw new AssertionError(e);
                } catch (final IllegalAccessException e) {
                    // See above message.
                    throw new AssertionError(e);
                }
                demonstration.bootstrap();
            }
        });
    }
}
