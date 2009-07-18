package com.bluespot.logic.values;

/**
 * A {@link Value} implementation that stores a value retrieved from some other
 * specified {@code Value}. This allows efficient and safe access to other
 * unreliable {@link Value} implementations.
 * <p>
 * {@code BufferedValue} holds two responsibilities: It represents a cached
 * version of some value, and it also ensures to clients that it will maximize
 * the time of valid returned results. It does this by forcing clients to call
 * {@link #retrieve()} explicitly before calling {@link #get()}; not doing so
 * will result in a {@link IllegalStateException}. It also does not allow null
 * values, since these represent a failed retrieval. If a subsequent retrieval
 * returns null, it will not override a previous legitimate retrieval.
 * <p>
 * The {@link #clear()} method is provided if you wish to remove any cached
 * value, but not immediately retrieve a new value.
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
     * Removes the stored value, if any, for this {@link BufferedValue}.
     */
    public void clear() {
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
     * Retrieves the newest value from the {@link #getSource source}. If the
     * returned value is non-null, it becomes the new buffered value. If, on the
     * other hand, it is null, then the old buffered value is retained. In this
     * way, buffered value maximizes the time it spends in a legitimate state.
     * This is at the expense of not allowing null values, and not necessarily
     * staying in "sync" with its source value.
     * 
     * @return the result from the source. It is the actual result from the
     *         source, and not necessarily the value returned from a call to
     *         {@link #get()}.
     */
    public T retrieve() {
        final T newValue = this.source.get();
        if (newValue != null) {
            this.bufferedValue = newValue;
        }
        return newValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the buffered value, if any. {@link #retrieve()} must be
     *         explicitly called in order for this method to return a legitimate
     *         value. If {@code retrieve()} has not yet been called, {@code
     *         null} is returned.
     */
    public T get() {
        if (this.bufferedValue == null) {
            throw new IllegalStateException("bufferedValue is not set");
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
