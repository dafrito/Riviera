package com.bluespot.forms.submission;

import java.util.Map;

import com.bluespot.forms.Schema;

/**
 * An {@link AbstractSubmission} subclass that is based off the types of a
 * {@link Schema}.
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
public abstract class SchemaBasedSubmission<K> extends AbstractSubmission<K> {

    private final Schema<K> schema;

    /**
     * Constructs a {@link SchemaBasedSubmission} that uses the specified
     * schema.
     * 
     * @param schema
     *            the schema that backs this submission object. Its types will
     *            be used as the valid types for this submission.
     */
    public SchemaBasedSubmission(final Schema<K> schema) {
        if (schema == null) {
            throw new NullPointerException("schema is null");
        }
        this.schema = schema;
    }

    /**
     * Returns the {@link Schema} associated with this submission object.
     * 
     * @return the {@link Schema} associated with this submission object
     */
    public Schema<K> getSchema() {
        return this.schema;
    }

    @Override
    public Map<? extends K, Class<?>> getTypes() {
        return this.getSchema().getTypes();
    }

}
