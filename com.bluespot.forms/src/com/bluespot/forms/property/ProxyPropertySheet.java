package com.bluespot.forms.property;

/**
 * A {@link PropertySheet} that proxies all requests to the specified {@code
 * PropertySheet}. This class allows property sheets to chain, and is typically
 * used to implement defaults.
 * <p>
 * Extending classes will likely want to override
 * {@link ProxyPropertySheet#resetAll()}. The current implementation will proxy
 * that request just like any other.
 * 
 * @author Aaron Faanes
 */
public class ProxyPropertySheet implements PropertySheet {

    private final PropertySheet proxied;

    /**
     * Creates a property sheet that proxies all requests to the specified
     * {@code proxied} sheet.
     * 
     * @param proxied
     *            the sheet to defer all requests to
     */
    public ProxyPropertySheet(final PropertySheet proxied) {
        if (proxied == null) {
            throw new NullPointerException("Proxied property sheet cannot be null");
        }
        this.proxied = proxied;
    }

    @Override
    public boolean contains(final String name) {
        return this.getProxied().contains(name);
    }

    @Override
    public boolean getBoolean(final String name) {
        return this.getProxied().getBoolean(name);
    }

    @Override
    public double getDouble(final String name) {
        return this.getProxied().getDouble(name);
    }

    @Override
    public double getFloat(final String name) {
        return this.getProxied().getFloat(name);
    }

    @Override
    public int getInteger(final String name) {
        return this.getProxied().getInteger(name);
    }

    @Override
    public long getLong(final String name) {
        return this.getProxied().getLong(name);
    }

    @Override
    public Object getObject(final String name) {
        return this.getProxied().getObject(name);
    }

    /**
     * Returns the {@link PropertySheet} that this sheet is proxying.
     * 
     * @return the proxied {@code PropertySheet}
     */
    public PropertySheet getProxied() {
        return this.proxied;
    }

    @Override
    public String getString(final String name) {
        return this.getProxied().getString(name);
    }

    @Override
    public boolean reset(final String name) {
        return this.getProxied().reset(name);
    }

    @Override
    public void resetAll() {
        this.getProxied().resetAll();
    }

    @Override
    public boolean setBoolean(final String name, final boolean value) {
        return this.getProxied().setBoolean(name, value);
    }

    @Override
    public boolean setDouble(final String name, final double value) {
        return this.getProxied().setDouble(name, value);
    }

    @Override
    public boolean setFloat(final String name, final float value) {
        return this.getProxied().setFloat(name, value);
    }

    @Override
    public boolean setInteger(final String name, final int value) {
        return this.getProxied().setInteger(name, value);
    }

    @Override
    public boolean setLong(final String name, final long value) {
        return this.getProxied().setLong(name, value);
    }

    @Override
    public boolean setObject(final String name, final Object value) {
        return this.getProxied().setObject(name, value);
    }

    @Override
    public boolean setString(final String name, final String value) {
        return this.getProxied().setString(name, value);
    }

}
