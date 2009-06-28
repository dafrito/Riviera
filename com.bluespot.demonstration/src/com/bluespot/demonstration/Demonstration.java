package com.bluespot.demonstration;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.bluespot.reflection.Reflection;
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
        this.initialize(frame);
        this.postInitialize(frame);
    }

    /**
     * 
     * @param frame
     *            the target frame that is initialized with this demonstration
     */
    protected void preInitialize(final JFrame frame) {
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
     */
    protected final void initialize(final JFrame frame) {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        frame.setContentPane(panel);
        // getContentPane() will invoke newContentPane() if necessary.
        panel.add(this.getContentPane());
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
    protected void postInitialize(final JFrame frame) {
        frame.pack();
        Components.center(frame);
        frame.setVisible(true);
    }

    /**
     * Returns the title used for this demonstration. By default, this will use
     * the string returned by {@link Class#getSimpleName()}, but feel free to
     * override this if you have a better name for your demonstration.
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
     * Create a new content pane that contains your demonstration.
     * 
     * @return the created content pane
     * @throws UnsupportedOperationException
     *             if this method is not overridden
     */
    protected abstract JComponent newContentPane();

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
                Demonstration demonstration;
                try {
                    demonstration = Reflection.invokeZeroArgConstructor(klass);
                } catch (final InvocationTargetException e) {
                    /*
                     * We can't really do anything here: if invocation failed,
                     * then we can only wrap and propagate. Ideally, we'd wrap
                     * this exception in something a bit more specific, but we
                     * almost always just invoke this during a 'main' call, so
                     * it's not a problem. If we ever start launching
                     * demonstrations later on using this method, this will need
                     * more attention.
                     */
                    throw new RuntimeException(e);
                }
                demonstration.bootstrap();
            }
        });
    }
}
