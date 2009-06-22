/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

/**
 * A Task that loads an image from a URL. Loading and decoding progress is
 * reported via the Task <code>progress</code> property and messages are
 * generated when the Task begins and when it finishes. If errors occur then
 * warnings are logged and the Task's value is null.
 * <p>
 * Applications would typically use LoadImageTask by creating a private subclass
 * that overrode the <code>done</code> method. See SingleFrameExample5.java for
 * an example.
 * <p>
 * Resources for this class are in the
 * <code>resources/LoadImageTask.properties</code> ResourceBundle.
 * 
 */
public class LoadImageTask extends Task<BufferedImage, Void> {
    private static Logger logger = Logger.getLogger(LoadImageTask.class.getName());
    private final URL url;
    private ImageReader imageReader = null;

    public LoadImageTask(final Application app, final URL url) {
        super(app, "LoadImageTask"); // init title/description/messages
        this.url = url;
    }

    protected final URL getImageURL() {
        return this.url;
    }

    @Override
    protected BufferedImage doInBackground() {
        final IIOReadProgressListener rpl = new IIOReadProgressAdapter() {
            @Override
            public void imageProgress(final ImageReader r, final float p) {
                LoadImageTask.this.setProgress(p, 0.0f, 100.0f);
            }
        };
        this.message("startedLoadingImage", this.url);
        this.imageReader = this.findImageReader(this.url);
        return this.loadImage(this.imageReader, rpl);
    }

    private void completionMessage(final String resourceKey) {
        this.message(resourceKey, this.url, this.getExecutionDuration(TimeUnit.MILLISECONDS));
    }

    @Override
    protected void cancelled() {
        if (this.imageReader != null) {
            this.imageReader.abort();
        }
        this.completionMessage("cancelledLoadingImage");
    }

    @Override
    protected void interrupted(final InterruptedException e) {
        if (this.imageReader != null) {
            this.imageReader.abort();
        }
        this.completionMessage("cancelledLoadingImage");
    }

    @Override
    protected void succeeded(final BufferedImage image) {
        this.completionMessage("finishedLoadingImage");
    }

    @Override
    protected void failed(final Throwable e) {
        this.completionMessage("failedLoadingImage");
    }

    @Override
    protected void finished() {
        this.imageReader = null;
    }

    /*
     * The methods below are what's required by the Java imaging API to enable
     * tracking the progress of an ImageIO read() and optionally aborting it. If
     * we weren't interested in tracking image-loading progress or supporting
     * Task.cancel() it would be enough to just use ImageIO.read().
     */
    private ImageReader findImageReader(final URL url) {
        ImageInputStream input = null;
        try {
            input = ImageIO.createImageInputStream(url.openStream());
        } catch (final IOException e) {
            LoadImageTask.logger.log(Level.WARNING, "bad image URL " + url, e);
        }
        ImageReader reader = null;
        if (input != null) {
            final Iterator readers = ImageIO.getImageReaders(input);
            while ((reader == null) && (readers != null) && readers.hasNext()) {
                reader = (ImageReader) readers.next();
            }
            reader.setInput(input);
        }
        return reader;
    }

    private BufferedImage loadImage(final ImageReader reader, final IIOReadProgressListener listener) {
        BufferedImage image = null;
        try {
            if (listener != null) {
                reader.addIIOReadProgressListener(listener);
            }
            final int index = reader.getMinIndex();
            image = reader.read(index);
        } catch (final IOException e) {
            LoadImageTask.logger.log(Level.WARNING, "loadImage failed", e);
        } finally {
            final ImageInputStream input = (ImageInputStream) (reader.getInput());
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException e) {
                }
            }
            if (reader != null) {
                reader.removeAllIIOReadProgressListeners();
                reader.dispose();
            }
        }
        return image;
    }

    /*
     * Makes creating an IIOReadProgressListener less horrible looking; see
     * LoadImageTask.doInBackground() above.
     */
    private static class IIOReadProgressAdapter implements IIOReadProgressListener {
        public void imageStarted(final ImageReader rdr, final int imageIndex) {
        }

        public void imageProgress(final ImageReader rdr, final float percentageDone) {
        }

        public void imageComplete(final ImageReader rdr) {
        }

        public void readAborted(final ImageReader rdr) {
        }

        public void sequenceStarted(final ImageReader rdr, final int minIndex) {
        }

        public void sequenceComplete(final ImageReader rdr) {
        }

        public void thumbnailStarted(final ImageReader rdr, final int imageIndex, final int thumbIndex) {
        }

        public void thumbnailProgress(final ImageReader rdr, final float percentageDone) {
        }

        public void thumbnailComplete(final ImageReader rdr) {
        }
    }
}
