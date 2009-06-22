/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;

/**
 * A demo of the Task class.
 * <p>
 * This demo highlights the importance of background tasks by downloading some
 * very large Mars rover images from JPL's photojournal web site. There are
 * about a dozen images, most with 10-15M pixels. Clicking the next/prev buttons
 * (or control-N,P) cancels the current download and starts loading a new image.
 * The stop button also cancels the current download. The list of images is
 * defined in the startup() method. The first image is shown by the
 * application's ready() method.
 * <p>
 * More images of Mars can be found here: <a
 * href="http://photojournal.jpl.nasa.gov/target/Mars">
 * http://photojournal.jpl.nasa.gov/target/Mars</a>. Some of the MER images are
 * quite large (like this 22348x4487 whopper,
 * http://photojournal.jpl.nasa.gov/jpeg/PIA06917.jpg) and can't be loaded
 * without reconfiguring the Java heap parameters.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class SingleFrameExample5 extends SingleFrameApplication {
    private static Logger logger = Logger.getLogger(SingleFrameExample5.class.getName());
    private JLabel imageLabel;
    private StatusBar statusBar;

    /*
     * The following fields define the application's internal state. We track
     * our current - imageIndex - position in the list of URLs (using a
     * ListIterator instead seemed like a good idea but was not). The value of
     * imageTask is managed by ShowImagTask, it's initialized (on the EDT) when
     * the task is constructed and cleared (on the EDT) by the task, when it's
     * done. The boolean @Action *enabled fields are updated by calling
     * updateNextPreviousEnabledProperties().
     */
    private List<URL> imageLocations;
    private int imageIndex = 0;
    private ShowImageTask imageTask = null;
    private boolean nextImageEnabled = true;
    private boolean previousImageEnabled = false;

    /*
     * A application specific subclass of LoadImageTask.
     * 
     * This class is constructed on the EDT. The constructor stops the current
     * ShowImageTask, if one is still running, clears the display (imageLabel)
     * so that we'll only have one enormous image on the heap, and updates the
     * enabled state of the next/previous @Actions. When the task completes, we
     * update the GUI.
     */
    private class ShowImageTask extends LoadImageTask {
        ShowImageTask(final URL imageURL) {
            super(SingleFrameExample5.this, imageURL);
            SingleFrameExample5.this.stopLoading();
            SingleFrameExample5.this.imageTask = this;
            SingleFrameExample5.this.showImageLoading(imageURL);
        }

        @Override
        protected void cancelled() {
            if (SingleFrameExample5.this.imageTask == this) {
                SingleFrameExample5.this.showImageCancelled(this.getImageURL());
            }
        }

        @Override
        protected void succeeded(final BufferedImage image) {
            super.succeeded(image);
            if (SingleFrameExample5.this.imageTask == this) {
                SingleFrameExample5.this.showImage(this.getImageURL(), image);
            }
        }

        @Override
        protected void failed(final Throwable e) {
            super.failed(e);
            if (SingleFrameExample5.this.imageTask == this) {
                SingleFrameExample5.this.showImageFailed(this.getImageURL());
            }
        }

        @Override
        protected void finished() {
            super.finished();
            SingleFrameExample5.this.imageTask = null;
        }
    }

    /*
     * The next,previous,refreshImage actions clear the displayed image, by
     * calling showImageLoading(), to free up heap space. Most of the images
     * we're loading are so large that there's not enough heap space (by
     * default) to accomodate both the old and new ones. We could adjust the
     * heap size parameters to eliminate the problem, however it's more
     * neighborly to just limit the heap's growth.
     */

    @Action(enabledProperty = "nextImageEnabled")
    public Task nextImage() {
        Task task = null;
        if (this.imageIndex < (this.imageLocations.size() - 1)) {
            this.imageIndex += 1;
            this.updateNextPreviousEnabledProperties();
            task = new ShowImageTask(this.imageLocations.get(this.imageIndex));
        }
        return task;
    }

    @Action(enabledProperty = "previousImageEnabled")
    public Task previousImage() {
        Task task = null;
        if (this.imageIndex > 0) {
            this.imageIndex -= 1;
            this.updateNextPreviousEnabledProperties();
            task = new ShowImageTask(this.imageLocations.get(this.imageIndex));
        }
        return task;
    }

    @Action
    public Task refreshImage() {
        return new ShowImageTask(this.imageLocations.get(this.imageIndex));
    }

    @Action
    public void stopLoading() {
        if ((this.imageTask != null) && !this.imageTask.isDone()) {
            this.imageTask.cancel(true);
        }
    }

    /*
     * The properties below define the enabled state for the corresponding
     * @Actions. The ApplicationActionMap class uses a PropertyChangeListener to
     * keep the Actions in sync with their enabledProperty properties.
     */

    private void updateNextPreviousEnabledProperties() {
        this.setNextImageEnabled(this.imageIndex < (this.imageLocations.size() - 1));
        this.setPreviousImageEnabled(this.imageIndex > 0);
    }

    public boolean isNextImageEnabled() {
        return this.nextImageEnabled;
    }

    public void setNextImageEnabled(final boolean nextImageEnabled) {
        final boolean oldValue = this.nextImageEnabled;
        this.nextImageEnabled = nextImageEnabled;
        this.firePropertyChange("nextImageEnabled", oldValue, this.nextImageEnabled);
    }

    public boolean isPreviousImageEnabled() {
        return this.previousImageEnabled;
    }

    public void setPreviousImageEnabled(final boolean previousImageEnabled) {
        final boolean oldValue = this.previousImageEnabled;
        this.previousImageEnabled = previousImageEnabled;
        this.firePropertyChange("previousImageEnabled", oldValue, this.previousImageEnabled);
    }

    /*
     * The ShowImage Task calls one of the following showImage* methods. If the
     * image is successfully loaded, then showImage() is called, otherwise
     * showImageCancelled() or showImageFailed(). Before the ShowImage Task is
     * executed the nextImage/previousImage @Actions call showImageLoading() to
     * alert the user that the process has started, and to free up the
     * heap-space occupied by the current image.
     */

    private void showImage(final URL imageURL, final BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final ResourceMap resourceMap = this.getContext().getResourceMap(this.getClass());
        final String tip = resourceMap.getString("imageTooltip", imageURL, width, height);
        this.imageLabel.setToolTipText(tip);
        this.imageLabel.setText(null);
        this.imageLabel.setIcon(new ImageIcon(image));
    }

    private void showImageMessage(final URL imageURL, final String key) {
        final String msg = this.getContext().getResourceMap(this.getClass()).getString(key, imageURL);
        this.imageLabel.setToolTipText("");
        this.imageLabel.setText(msg);
        this.imageLabel.setIcon(null);
    }

    private void showImageLoading(final URL imageURL) {
        this.showImageMessage(imageURL, "loadingWait");
    }

    private void showImageCancelled(final URL imageURL) {
        this.showImageMessage(imageURL, "loadingCancelled");
    }

    private void showImageFailed(final URL imageURL) {
        this.showImageMessage(imageURL, "loadingFailed");
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
        final JMenuBar menuBar = new JMenuBar();
        final String[] fileMenuActionNames = { "previousImage", "nextImage", "refreshImage", "stopLoading", "---",
                "quit" };
        menuBar.add(this.createMenu("fileMenu", fileMenuActionNames));
        return menuBar;
    }

    private JComponent createToolBar() {
        final String[] toolbarActionNames = { "previousImage", "nextImage", "refreshImage", "stopLoading" };
        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        final Border border = new EmptyBorder(2, 9, 2, 9); // top, left, bottom,
                                                           // right
        for (final String actionName : toolbarActionNames) {
            final JButton button = new JButton();
            button.setBorder(border);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setAction(this.getAction(actionName));
            button.setFocusable(false);
            toolBar.add(button);
        }
        return toolBar;
    }

    private JComponent createMainPanel() {
        this.statusBar = new StatusBar(this, this.getContext().getTaskMonitor());
        this.imageLabel = new JLabel();
        this.imageLabel.setName("imageLabel");
        this.imageLabel.setOpaque(true);
        this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        final JScrollPane scrollPane = new JScrollPane(this.imageLabel);
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.createToolBar(), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(this.statusBar, BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(0, 2, 2, 2)); // top, left, bottom,
                                                      // right
        panel.setPreferredSize(new Dimension(640, 480));
        return panel;
    }

    @Override
    protected void startup() {
        final String imageDir = "http://photojournal.jpl.nasa.gov/jpeg/";
        final String[] imageNames = { "PIA03171", "PIA02652", "PIA05108", "PIA02696", "PIA05049", "PIA05460",
                "PIA07327", "PIA05117", "PIA05199", "PIA05990", "PIA03623" };
        this.imageIndex = 0;
        this.imageLocations = new ArrayList<URL>(imageNames.length);
        for (final String imageName : imageNames) {
            final String path = imageDir + imageName + ".jpg";
            try {
                final URL url = new URL(path);
                this.imageLocations.add(url);
            } catch (final MalformedURLException e) {
                SingleFrameExample5.logger.log(Level.WARNING, "bad image URL " + path, e);
            }
        }
        this.getMainFrame().setJMenuBar(this.createMenuBar());
        this.show(this.createMainPanel());
    }

    /**
     * Runs after the startup has completed and the GUI is up and ready. We show
     * the first image here, rather than initializing it at startup time, so
     * loading the first image doesn't impede getting the GUI visible.
     */
    @Override
    protected void ready() {
        final Task task = new ShowImageTask(this.imageLocations.get(0));
        this.getContext().getTaskService().execute(task);
    }

    public static void main(final String[] args) {
        Application.launch(SingleFrameExample5.class, args);
    }
}
