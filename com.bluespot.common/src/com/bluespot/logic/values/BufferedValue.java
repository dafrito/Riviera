package com.bluespot.logic.values;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A {@link Value} implementation that stores a value retrieved from some other
 * specified {@code Value}. This allows efficient and safe access to other
 * unreliable {@link Value} implementations. It has many similarities to an
 * {@link Iterator}: It is a mediates the exchange of data between some source
 * and clients. It offers a {@link #get()} method that will throw an unchecked
 * exception if a value hasn't been retrieved, and a {@link #hasValue()} method
 * to check whether an exception would be thrown.
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
 * <p>
 * This class is safe for concurrent access
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value that is returned by this {@code Value} object
 */
public final class BufferedValue<T> implements Value<T> {

    private final Value<? extends T> source;

    private volatile T bufferedValue;

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
    public synchronized void clear() {
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
     * Returns whether this {@link BufferedValue} has a value it can
     * successfully return. If this is {@code true}, then
     * {@link BufferedValue#get()} is guaranteed to succeed.
     * 
     * @return {@code true} if this {@code BufferedValue} has successfully
     *         retrieved a value.
     */
    public boolean hasValue() {
        return this.bufferedValue != null;
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
    public synchronized T retrieve() {
        final T newValue = this.source.get();
        if (newValue != null) {
            this.bufferedValue = newValue;
        }
        return newValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the buffered value. {@link #retrieve()} must be explicitly called
     *         in order for this method to return a legitimate value.
     * @throws NoSuchValueException
     *             if there is no non-null value currently saved by this {@code
     *             BufferedValue} object. This would occur if
     *             {@link #retrieve()} was not called, or if the invocation did
     *             not successfully retrieve a value. Reasons for this failure
     *             are dependent on the source {@link Value} implementation.
     */
    @Override
	public T get() {
        if (this.bufferedValue == null) {
            throw new NoSuchValueException(this);
        }
        return this.bufferedValue;
    }

    /**
     * A {@link NoSuchElementException} indicating that a buffered value was
     * unavailable. This occurs whenever {@link BufferedValue#retrieve()} has
     * either not been called before {@link BufferedValue#get()}, or if a call
     * to {@code retrieve()} did not successfully return a value.
     * 
     * @author Aaron Faanes
     * 
     */
    public static class NoSuchValueException extends NoSuchElementException {
        private static final long serialVersionUID = -6500918133163478011L;

        private final BufferedValue<?> source;

        /**
         * Constructs a {@link NoSuchValueException} with the specified source.
         * 
         * @param source
         *            the source that is the origin of this exception
         */
        private NoSuchValueException(final BufferedValue<?> source) {
            super("bufferedValue is not set");
            this.source = source;
        }

        /**
         * Returns the source {@link BufferedValue} that caused this exception.
         * 
         * @return the source {@code BufferedValue}
         */
        public BufferedValue<?> getSource() {
            return this.source;
        }

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
