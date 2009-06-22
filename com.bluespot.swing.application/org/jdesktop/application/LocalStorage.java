/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.jdesktop.application;

import java.awt.Rectangle;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

/**
 * Access to per application, per user, local file storage.
 * 
 * @see ApplicationContext#getLocalStorage
 * @see SessionStorage
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class LocalStorage extends AbstractBean {
    private static Logger logger = Logger.getLogger(LocalStorage.class.getName());
    private final ApplicationContext context;
    private long storageLimit = -1L;
    private LocalIO localIO = null;
    private final File unspecifiedFile = new File("unspecified");
    private File directory = this.unspecifiedFile;

    protected LocalStorage(final ApplicationContext context) {
        if (context == null) {
            throw new IllegalArgumentException("null context");
        }
        this.context = context;
    }

    // FIXME - documentation
    protected final ApplicationContext getContext() {
        return this.context;
    }

    private void checkFileName(final String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("null fileName");
        }
    }

    public InputStream openInputFile(final String fileName) throws IOException {
        this.checkFileName(fileName);
        return this.getLocalIO().openInputFile(fileName);
    }

    public OutputStream openOutputFile(final String fileName) throws IOException {
        this.checkFileName(fileName);
        return this.getLocalIO().openOutputFile(fileName);
    }

    public boolean deleteFile(final String fileName) throws IOException {
        this.checkFileName(fileName);
        return this.getLocalIO().deleteFile(fileName);
    }

    /*
     * If an exception occurs in the XMLEncoder/Decoder, we want to throw an
     * IOException. The exceptionThrow listener method doesn't throw a checked
     * exception so we just set a flag here and check it when the encode/decode
     * operation finishes
     */
    private static class AbortExceptionListener implements ExceptionListener {
        public Exception exception = null;

        public void exceptionThrown(final Exception e) {
            if (this.exception == null) {
                this.exception = e;
            }
        }
    }

    private static boolean persistenceDelegatesInitialized = false;

    public void save(final Object bean, final String fileName) throws IOException {
        final AbortExceptionListener el = new AbortExceptionListener();
        XMLEncoder e = null;
        /*
         * Buffer the XMLEncoder's output so that decoding errors don't cause us
         * to trash the current version of the specified file.
         */
        final ByteArrayOutputStream bst = new ByteArrayOutputStream();
        try {
            e = new XMLEncoder(bst);
            if (!LocalStorage.persistenceDelegatesInitialized) {
                e.setPersistenceDelegate(Rectangle.class, new RectanglePD());
                LocalStorage.persistenceDelegatesInitialized = true;
            }
            e.setExceptionListener(el);
            e.writeObject(bean);
        } finally {
            if (e != null) {
                e.close();
            }
        }
        if (el.exception != null) {
            throw new LSException("save failed \"" + fileName + "\"", el.exception);
        }
        OutputStream ost = null;
        try {
            ost = this.openOutputFile(fileName);
            ost.write(bst.toByteArray());
        } finally {
            if (ost != null) {
                ost.close();
            }
        }
    }

    public Object load(final String fileName) throws IOException {
        InputStream ist = null;
        try {
            ist = this.openInputFile(fileName);
        } catch (final IOException e) {
            return null;
        }
        final AbortExceptionListener el = new AbortExceptionListener();
        XMLDecoder d = null;
        try {
            d = new XMLDecoder(ist);
            d.setExceptionListener(el);
            final Object bean = d.readObject();
            if (el.exception != null) {
                throw new LSException("load failed \"" + fileName + "\"", el.exception);
            }
            return bean;
        } finally {
            if (d != null) {
                d.close();
            }
        }
    }

    private void closeStream(final Closeable st, final String fileName) throws IOException {
        if (st != null) {
            try {
                st.close();
            } catch (final java.io.IOException e) {
                throw new LSException("close failed \"" + fileName + "\"", e);
            }
        }
    }

    public long getStorageLimit() {
        return this.storageLimit;
    }

    public void setStorageLimit(final long storageLimit) {
        if (storageLimit < -1L) {
            throw new IllegalArgumentException("invalid storageLimit");
        }
        final long oldValue = this.storageLimit;
        this.storageLimit = storageLimit;
        this.firePropertyChange("storageLimit", oldValue, this.storageLimit);
    }

    private String getId(final String key, final String def) {
        final ResourceMap appResourceMap = this.getContext().getResourceMap();
        String id = appResourceMap.getString(key);
        if (id == null) {
            LocalStorage.logger.log(Level.WARNING, "unspecified resource " + key + " using " + def);
            id = def;
        } else if (id.trim().length() == 0) {
            LocalStorage.logger.log(Level.WARNING, "empty resource " + key + " using " + def);
            id = def;
        }
        return id;
    }

    private String getApplicationId() {
        return this.getId("Application.id", this.getContext().getApplicationClass().getSimpleName());
    }

    private String getVendorId() {
        return this.getId("Application.vendorId", "UnknownApplicationVendor");
    }

    /*
     * The following enum and method only exist to distinguish Windows and OSX
     * for the sake of getDirectory().
     */
    private enum OSId {
        WINDOWS, OSX, UNIX
    }

    private OSId getOSId() {
        final PrivilegedAction<String> doGetOSName = new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty("os.name");
            }
        };
        OSId id = OSId.UNIX;
        final String osName = AccessController.doPrivileged(doGetOSName);
        if (osName != null) {
            if (osName.toLowerCase().startsWith("mac os x")) {
                id = OSId.OSX;
            } else if (osName.contains("Windows")) {
                id = OSId.WINDOWS;
            }
        }
        return id;
    }

    public File getDirectory() {
        if (this.directory == this.unspecifiedFile) {
            this.directory = null;
            String userHome = null;
            try {
                userHome = System.getProperty("user.home");
            } catch (final SecurityException ignore) {
            }
            if (userHome != null) {
                final String applicationId = this.getApplicationId();
                final OSId osId = this.getOSId();
                if (osId == OSId.WINDOWS) {
                    File appDataDir = null;
                    try {
                        final String appDataEV = System.getenv("APPDATA");
                        if ((appDataEV != null) && (appDataEV.length() > 0)) {
                            appDataDir = new File(appDataEV);
                        }
                    } catch (final SecurityException ignore) {
                    }
                    final String vendorId = this.getVendorId();
                    if ((appDataDir != null) && appDataDir.isDirectory()) {
                        // ${APPDATA}\{vendorId}\${applicationId}
                        final String path = vendorId + "\\" + applicationId + "\\";
                        this.directory = new File(appDataDir, path);
                    } else {
                        // ${userHome}\Application
                        // Data\${vendorId}\${applicationId}
                        final String path = "Application Data\\" + vendorId + "\\" + applicationId + "\\";
                        this.directory = new File(userHome, path);
                    }
                } else if (osId == OSId.OSX) {
                    // ${userHome}/Library/Application Support/${applicationId}
                    final String path = "Library/Application Support/" + applicationId + "/";
                    this.directory = new File(userHome, path);
                } else {
                    // ${userHome}/.${applicationId}/
                    final String path = "." + applicationId + "/";
                    this.directory = new File(userHome, path);
                }
            }
        }
        return this.directory;
    }

    public void setDirectory(final File directory) {
        final File oldValue = this.directory;
        this.directory = directory;
        this.firePropertyChange("directory", oldValue, this.directory);
    }

    /*
     * Papers over the fact that the String,Throwable IOException constructor
     * was only introduced in Java 6.
     */
    private static class LSException extends IOException {
        /**
         * 
         */
        private static final long serialVersionUID = 7600308812390861735L;

        public LSException(final String s, final Throwable e) {
            super(s);
            this.initCause(e);
        }

        public LSException(final String s) {
            super(s);
        }
    }

    /*
     * There are some (old) Java classes that aren't proper beans. Rectangle is
     * one of these. When running within the secure sandbox, writing a Rectangle
     * with XMLEncoder causes a security exception because
     * DefaultPersistenceDelegate calls Field.setAccessible(true) to gain access
     * to private fields. This is a workaround for that problem. A bug has been
     * filed, see JDK bug ID 4741757
     */
    private static class RectanglePD extends DefaultPersistenceDelegate {
        public RectanglePD() {
            super(new String[] { "x", "y", "width", "height" });
        }

        @Override
        protected Expression instantiate(final Object oldInstance, final Encoder out) {
            final Rectangle oldR = (Rectangle) oldInstance;
            final Object[] constructorArgs = new Object[] { oldR.x, oldR.y, oldR.width, oldR.height };
            return new Expression(oldInstance, oldInstance.getClass(), "new", constructorArgs);
        }
    }

    private synchronized LocalIO getLocalIO() {
        if (this.localIO == null) {
            this.localIO = this.getPersistenceServiceIO();
            if (this.localIO == null) {
                this.localIO = new LocalFileIO();
            }
        }
        return this.localIO;
    }

    private abstract class LocalIO {
        public abstract InputStream openInputFile(String fileName) throws IOException;

        public abstract OutputStream openOutputFile(String fileName) throws IOException;

        public abstract boolean deleteFile(String fileName) throws IOException;
    }

    private class LocalFileIO extends LocalIO {
        @Override
        public InputStream openInputFile(final String fileName) throws IOException {
            final File path = new File(LocalStorage.this.getDirectory(), fileName);
            try {
                return new BufferedInputStream(new FileInputStream(path));
            } catch (final IOException e) {
                throw new LSException("couldn't open input file \"" + fileName + "\"", e);
            }
        }

        @Override
        public OutputStream openOutputFile(final String fileName) throws IOException {
            final File dir = LocalStorage.this.getDirectory();
            if (!dir.isDirectory()) {
                if (!dir.mkdirs()) {
                    throw new LSException("couldn't create directory " + dir);
                }
            }
            final File path = new File(dir, fileName);
            try {
                return new BufferedOutputStream(new FileOutputStream(path));
            } catch (final IOException e) {
                throw new LSException("couldn't open output file \"" + fileName + "\"", e);
            }
        }

        @Override
        public boolean deleteFile(final String fileName) throws IOException {
            final File path = new File(LocalStorage.this.getDirectory(), fileName);
            return path.delete();
        }
    }

    /*
     * Determine if we're a web started application and the JNLP
     * PersistenceService is available without forcing the JNLP API to be
     * class-loaded. We don't want to require apps that aren't web started to
     * bundle javaws.jar
     */
    private LocalIO getPersistenceServiceIO() {
        try {
            final Class smClass = Class.forName("javax.jnlp.ServiceManager");
            final Method getServiceNamesMethod = smClass.getMethod("getServiceNames");
            final String[] serviceNames = (String[]) getServiceNamesMethod.invoke(null);
            boolean psFound = false;
            boolean bsFound = false;
            for (final String serviceName : serviceNames) {
                if (serviceName.equals("javax.jnlp.BasicService")) {
                    bsFound = true;
                } else if (serviceName.equals("javax.jnlp.PersistenceService")) {
                    psFound = true;
                }
            }
            if (bsFound && psFound) {
                return new PersistenceServiceIO();
            }
        } catch (final Exception ignore) {
            // either the classes or the services can't be found
        }
        return null;
    }

    private class PersistenceServiceIO extends LocalIO {
        private BasicService bs;
        private PersistenceService ps;

        private String initFailedMessage(final String s) {
            return this.getClass().getName() + " initialization failed: " + s;
        }

        PersistenceServiceIO() {
            try {
                this.bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
                this.ps = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
            } catch (final UnavailableServiceException e) {
                LocalStorage.logger.log(Level.SEVERE, this.initFailedMessage("ServiceManager.lookup"), e);
                this.bs = null;
                this.ps = null;
            }
        }

        private void checkBasics(final String s) throws IOException {
            if ((this.bs == null) || (this.ps == null)) {
                throw new IOException(this.initFailedMessage(s));
            }
        }

        private URL fileNameToURL(final String name) throws IOException {
            try {
                return new URL(this.bs.getCodeBase(), name);
            } catch (final MalformedURLException e) {
                throw new LSException("invalid filename \"" + name + "\"", e);
            }
        }

        @Override
        public InputStream openInputFile(final String fileName) throws IOException {
            this.checkBasics("openInputFile");
            final URL fileURL = this.fileNameToURL(fileName);
            try {
                return new BufferedInputStream(this.ps.get(fileURL).getInputStream());
            } catch (final Exception e) {
                throw new LSException("openInputFile \"" + fileName + "\" failed", e);
            }
        }

        @Override
        public OutputStream openOutputFile(final String fileName) throws IOException {
            this.checkBasics("openOutputFile");
            final URL fileURL = this.fileNameToURL(fileName);
            try {
                FileContents fc = null;
                try {
                    fc = this.ps.get(fileURL);
                } catch (final FileNotFoundException e) {
                    /*
                     * Verify that the max size for new PersistenceService files
                     * is >= 100K (2^17) before opening one.
                     */
                    final long maxSizeRequest = 131072L;
                    final long maxSize = this.ps.create(fileURL, maxSizeRequest);
                    if (maxSize >= maxSizeRequest) {
                        fc = this.ps.get(fileURL);
                    }
                }
                if ((fc != null) && (fc.canWrite())) {
                    return new BufferedOutputStream(fc.getOutputStream(true));
                } else {
                    throw new IOException("unable to create FileContents object");
                }
            } catch (final Exception e) {
                throw new LSException("openOutputFile \"" + fileName + "\" failed", e);
            }
        }

        @Override
        public boolean deleteFile(final String fileName) throws IOException {
            this.checkBasics("deleteFile");
            final URL fileURL = this.fileNameToURL(fileName);
            try {
                this.ps.delete(fileURL);
                return true;
            } catch (final Exception e) {
                throw new LSException("openInputFile \"" + fileName + "\" failed", e);
            }
        }
    }
}
