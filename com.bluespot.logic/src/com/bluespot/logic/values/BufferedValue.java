package com.bluespot.logic.values;

/**
 * A {@link Value} implementation that stores a value retrieved from some other
 * specified {@code Value}. This allows efficient and safe access to unreliable
 * {@link Value}.
 * <p>
 * The value will be fetched on the first call to {@link #get()}. Subsequent
 * calls to {@code get()} will return the cached value. You may use
 * {@link #flush()} to clear the cached value, or explicitly refresh the cached
 * value through {@link #refresh()}.
 * <p>
 * The source value should not return {@code null}, as this class uses that
 * value to determine whether a value should be retrieved. As a consequence,
 * this may cause unnecessary inefficiency.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value that is returned by this {@code Value} object
 */
public final class BufferedValue<T> implements Value<T> {

    private final Value<? extends T> source;

    private T bufferedValue;

    /**
     * Constructs a {@link BufferedValue} that uses the specified source value
     * for its data.
     * 
     * @param source
     *            the source of data for this {@code Value} object. It must not
     *            be {@code null}, and its {@link Value#get()} method should
     *            never return null.
     */
    public BufferedValue(final Value<? extends T> source) {
        if (source == null) {
            throw new NullPointerException("source is null");
        }
        this.source = source;
    }

    /**
     * Flushes any stored values for this {@link BufferedValue}.
     */
    public void flush() {
        this.bufferedValue = null;
    }

    /**
     * Returns the source value from which actual values are retrieved.
     * 
     * @return the source value of this value.
     */
    public Value<? extends T> getSource() {
        return this.source;
    }

    /**
     * Refreshes this value by retrieving the latest value from
     * {@link #getSource source}.
     */
    public void refresh() {
        this.bufferedValue = this.source.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @return the buffered value, if any. If the value has not yet been
     *         retrieved, it will be automatically retrieved.
     */
    public T get() {
        if (this.bufferedValue == null) {
            this.refresh();
        }
        return this.bufferedValue;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BufferedValue<?>)) {
            return false;
        }
        final BufferedValue<?> other = (BufferedValue<?>) obj;
        if (!this.getSource().equals(other.getSource())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getSource().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("BufferedValue[%s]", this.getSource().toString());
    }

}
