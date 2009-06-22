/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.jdesktop.application;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A base class for converting arbitrary types to and from Strings, as well as a
 * registry of ResourceConverter implementations.
 * <p>
 * The <tt>supportsType</tt> method defines what types a ResourceConverter
 * supports. By default it returns true for classes that are equal to the
 * constructor's <tt>type</tt> argument. The <tt>parseType</tt> methods converts
 * a string the ResourceConverter's supported type, and the <tt>toString</tt>
 * does the inverse, it converts a supported type to a String. Concrete
 * ResourceConverter subclasses must override <tt>parseType()</tt> and, in most
 * cases, the <tt>toString</tt> method as well.
 * <p>
 * This class maintains a registry of ResourceConverters. The <tt>forType</tt>
 * method returns the first ResourceConverter that supports a particular type,
 * new ResourceConverters can be added with <tt>register()</tt>. A small set of
 * generic ResourceConverters are registered by default. They support the
 * following types:
 * <ul>
 * <li><tt>Boolean</tt></li>
 * <li><tt>Integer</tt></li>
 * <li><tt>Float</tt></li>
 * <li><tt>Double</tt></li>
 * <li><tt>Long</tt></li>
 * <li><tt>Short</tt></li>
 * <li><tt>Byte</tt></li>
 * <li><tt>MessageFormat</tt></li>
 * <li><tt>URL</tt></li>
 * <li><tt>URI</tt></li>
 * </ul>
 * <p>
 * The Boolean ResourceConverter returns true for "true", "on", "yes", false
 * otherwise. The other primitive type ResourceConverters rely on the
 * corresponding static parse<i>Type</i> method, e.g.
 * <tt>Integer.parseInt()</tt>. The MessageFormat ResourceConverter just creates
 * MessageFormat object with the string as its constructor argument. The URL/URI
 * converters just apply the corresponding constructor to the resource string.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 * @see ResourceMap
 */
public abstract class ResourceConverter {

    protected final Class type;

    public abstract Object parseString(String s, ResourceMap r) throws ResourceConverterException;

    public String toString(final Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    protected ResourceConverter(final Class type) {
        if (type == null) {
            throw new IllegalArgumentException("null type");
        }
        this.type = type;
    }

    private ResourceConverter() {
        this.type = null;
    } // not used

    public boolean supportsType(final Class testType) {
        return this.type.equals(testType);
    }

    public static class ResourceConverterException extends Exception {
        /**
         * 
         */
        private static final long serialVersionUID = 9131587619993866282L;
        private final String badString;

        private String maybeShorten(final String s) {
            final int n = s.length();
            return (n < 128) ? s : s.substring(0, 128) + "...[" + (n - 128) + " more characters]";
        }

        public ResourceConverterException(final String message, final String badString, final Throwable cause) {
            super(message, cause);
            this.badString = this.maybeShorten(badString);
        }

        public ResourceConverterException(final String message, final String badString) {
            super(message);
            this.badString = this.maybeShorten(badString);
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer(super.toString());
            sb.append(" string: \"");
            sb.append(this.badString);
            sb.append("\"");
            return sb.toString();
        }
    }

    public static void register(final ResourceConverter resourceConverter) {
        if (resourceConverter == null) {
            throw new IllegalArgumentException("null resourceConverter");
        }
        ResourceConverter.resourceConverters.add(resourceConverter);
    }

    public static ResourceConverter forType(final Class type) {
        if (type == null) {
            throw new IllegalArgumentException("null type");
        }
        for (final ResourceConverter sc : ResourceConverter.resourceConverters) {
            if (sc.supportsType(type)) {
                return sc;
            }
        }
        return null;
    }

    private static ResourceConverter[] resourceConvertersArray = { new BooleanResourceConverter("true", "on", "yes"),
            new IntegerResourceConverter(), new MessageFormatResourceConverter(), new FloatResourceConverter(),
            new DoubleResourceConverter(), new LongResourceConverter(), new ShortResourceConverter(),
            new ByteResourceConverter(), new URLResourceConverter(), new URIResourceConverter() };
    private static List<ResourceConverter> resourceConverters = new ArrayList<ResourceConverter>(
            Arrays.asList(ResourceConverter.resourceConvertersArray));

    private static class BooleanResourceConverter extends ResourceConverter {
        private final String[] trueStrings;

        BooleanResourceConverter(final String... trueStrings) {
            super(Boolean.class);
            this.trueStrings = trueStrings;
        }

        @Override
        public Object parseString(String s, final ResourceMap ignore) {
            s = s.trim();
            for (final String trueString : this.trueStrings) {
                if (s.equalsIgnoreCase(trueString)) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }

        @Override
        public boolean supportsType(final Class testType) {
            return testType.equals(Boolean.class) || testType.equals(boolean.class);
        }
    }

    private static abstract class NumberResourceConverter extends ResourceConverter {
        private final Class primitiveType;

        NumberResourceConverter(final Class type, final Class primitiveType) {
            super(type);
            this.primitiveType = primitiveType;
        }

        protected abstract Number parseString(String s) throws NumberFormatException;

        @Override
        public Object parseString(final String s, final ResourceMap ignore) throws ResourceConverterException {
            try {
                return this.parseString(s);
            } catch (final NumberFormatException e) {
                throw new ResourceConverterException("invalid " + this.type.getSimpleName(), s, e);
            }
        }

        @Override
        public boolean supportsType(final Class testType) {
            return testType.equals(this.type) || testType.equals(this.primitiveType);
        }
    }

    private static class FloatResourceConverter extends NumberResourceConverter {
        FloatResourceConverter() {
            super(Float.class, float.class);
        }

        @Override
        protected Number parseString(final String s) throws NumberFormatException {
            return Float.parseFloat(s);
        }
    }

    private static class DoubleResourceConverter extends NumberResourceConverter {
        DoubleResourceConverter() {
            super(Double.class, double.class);
        }

        @Override
        protected Number parseString(final String s) throws NumberFormatException {
            return Double.parseDouble(s);
        }
    }

    private static abstract class INumberResourceConverter extends ResourceConverter {
        private final Class primitiveType;

        INumberResourceConverter(final Class type, final Class primitiveType) {
            super(type);
            this.primitiveType = primitiveType;
        }

        protected abstract Number parseString(String s, int radix) throws NumberFormatException;

        @Override
        public Object parseString(final String s, final ResourceMap ignore) throws ResourceConverterException {
            try {
                final String[] nar = s.split("&"); // number ampersand radix
                final int radix = (nar.length == 2) ? Integer.parseInt(nar[1]) : -1;
                return this.parseString(nar[0], radix);
            } catch (final NumberFormatException e) {
                throw new ResourceConverterException("invalid " + this.type.getSimpleName(), s, e);
            }
        }

        @Override
        public boolean supportsType(final Class testType) {
            return testType.equals(this.type) || testType.equals(this.primitiveType);
        }
    }

    private static class ByteResourceConverter extends INumberResourceConverter {
        ByteResourceConverter() {
            super(Byte.class, byte.class);
        }

        @Override
        protected Number parseString(final String s, final int radix) throws NumberFormatException {
            return (radix == -1) ? Byte.decode(s) : Byte.parseByte(s, radix);
        }
    }

    private static class IntegerResourceConverter extends INumberResourceConverter {
        IntegerResourceConverter() {
            super(Integer.class, int.class);
        }

        @Override
        protected Number parseString(final String s, final int radix) throws NumberFormatException {
            return (radix == -1) ? Integer.decode(s) : Integer.parseInt(s, radix);
        }
    }

    private static class LongResourceConverter extends INumberResourceConverter {
        LongResourceConverter() {
            super(Long.class, long.class);
        }

        @Override
        protected Number parseString(final String s, final int radix) throws NumberFormatException {
            return (radix == -1) ? Long.decode(s) : Long.parseLong(s, radix);
        }
    }

    private static class ShortResourceConverter extends INumberResourceConverter {
        ShortResourceConverter() {
            super(Short.class, short.class);
        }

        @Override
        protected Number parseString(final String s, final int radix) throws NumberFormatException {
            return (radix == -1) ? Short.decode(s) : Short.parseShort(s, radix);
        }
    }

    private static class MessageFormatResourceConverter extends ResourceConverter {
        MessageFormatResourceConverter() {
            super(MessageFormat.class);
        }

        @Override
        public Object parseString(final String s, final ResourceMap ignore) {
            return new MessageFormat(s);
        }
    }

    private static class URLResourceConverter extends ResourceConverter {
        URLResourceConverter() {
            super(URL.class);
        }

        @Override
        public Object parseString(final String s, final ResourceMap ignore) throws ResourceConverterException {
            try {
                return new URL(s);
            } catch (final MalformedURLException e) {
                throw new ResourceConverterException("invalid URL", s, e);
            }
        }
    }

    private static class URIResourceConverter extends ResourceConverter {
        URIResourceConverter() {
            super(URI.class);
        }

        @Override
        public Object parseString(final String s, final ResourceMap ignore) throws ResourceConverterException {
            try {
                return new URI(s);
            } catch (final URISyntaxException e) {
                throw new ResourceConverterException("invalid URI", s, e);
            }
        }
    }
}
