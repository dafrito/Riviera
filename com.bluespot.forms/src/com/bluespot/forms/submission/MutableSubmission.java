package com.bluespot.forms.submission;

import java.util.HashMap;
import java.util.Map;

import com.bluespot.forms.Schema;

/**
 * A very simple {@link SchemaBasedSubmission} implementation. It is essentially
 * a naked wrapper around a {@link Map}.
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
public class MutableSubmission<K> extends SchemaBasedSubmission<K> {

    private final Map<K, Object> values = new HashMap<K, Object>();

    /**
     * Constructs a {@link MutableSubmission} using the specified schema.
     * 
     * @param schema
     *            the schema used by this submission
     * 
     * @throws NullPointerException
     *             if {@code schema} is null
     * @see SchemaBasedSubmission#SchemaBasedSubmission(Schema)
     */
    public MutableSubmission(final Schema<K> schema) {
        super(schema);
    }

    @Override
    public Map<? extends K, Object> asMap() {
        return new HashMap<K, Object>(this.values);
    }

    @Override
    protected Object getValue(final K key) {
        return this.values.get(key);
    }

    /**
     * Puts the specified value at the specified key in this submission.
     * 
     * @param key
     *            the key that describes the destination of the specified value.
     *            It must not be null.
     * @param value
     *            the value that will occupy the field described by the key. It
     *            must not be null.
     * @return the old value at the specified key, if any
     * @throws NullPointerException
     *             if either argument is null
     * @throws ClassCastException
     *             if the specified value is not of the proper type for this
     *             submission
     */
    public final Object put(final K key, final Object value) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final Class<?> type = this.getType(key);
        if (!type.isInstance(value)) {
            throw new ClassCastException(String.format("Value '%s' not of type '%s'", value, type));
        }
        return this.values.put(key, value);
    }

}
