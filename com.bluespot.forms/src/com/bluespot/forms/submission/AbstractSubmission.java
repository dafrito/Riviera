package com.bluespot.forms.submission;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bluespot.forms.Submission;

/**
 * A skeletal {@link Submission} implementation that provides both a simple way
 * to handle type-checking, and provides a framework for easy implementation of
 * the {@link #get(Object, Class)} method.
 * <p>
 * Implementors need only implement the two abstract methods in this class for
 * full functionality.
 * 
 * @author Aaron Faanes
 * 
 * @param <K>
 *            the key type used to define fields of this submission. This is
 *            useful if you wish to restrict the number of available objects,
 *            such as by using an enumeration. Of course, you are free to use
 *            any class you wish; the only constraint is that objects used as
 *            keys must implement {@link #equals(Object)} appropriately.
 */
public abstract class AbstractSubmission<K> implements Submission<K> {

    /**
     * Returns the value associated with the specified key. The key is
     * guaranteed to be a valid key for this submission. The returned value may
     * be null. This method does not need to perform type-checking on the
     * returned value; the value will be checked once it is returned.
     * 
     * @param key
     *            the non-null, valid key that corresponds to a value
     * @return the object that is associated with the specified key
     */
    protected abstract Object getValue(K key);

    public Map<? extends K, Object> asMap() {
        final Map<K, Object> values = new HashMap<K, Object>();
        for (final Entry<? extends K, Class<?>> entry : this.getTypes().entrySet()) {
            values.put(entry.getKey(), this.get(entry.getKey(), entry.getValue()));
        }
        return values;
    }

    public boolean containsKey(final K key) {
        return this.getTypes().containsKey(key);
    }

    public final <T> T get(final K key, final Class<T> type) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        final Class<?> requiredType = this.getTypes().get(key);
        if (requiredType == null) {
            throw new IllegalArgumentException("requiredType is null for the specified key: " + key);
        }
        if (!type.isAssignableFrom(requiredType)) {
            throw new ClassCastException("types are not compatible for the specified key: " + key);
        }
        final Object value = this.getValue(key);
        if (value == null) {
            return null;
        }
        /*
         * We ostensibly allow this to throw a ClassCastException if the cast is
         * illegal.
         */
        return type.cast(value);
    }

    @Override
    public Class<?> getType(final K key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        final Class<?> type = this.getTypes().get(key);
        if (type == null) {
            throw new IllegalArgumentException("key is not in schema: " + key);
        }
        return type;
    }

}
