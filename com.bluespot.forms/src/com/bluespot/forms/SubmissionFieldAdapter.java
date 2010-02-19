package com.bluespot.forms;

import com.bluespot.logic.adapters.AbstractHandledAdapter;
import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.adapters.HandledAdapter;

/**
 * An {@link Adapter} that retrieves a field from a {@link Submission} object.
 * It will retrieve, from a given {@code Submission} object, one field value and
 * ensure that it is of the proper type.
 * <p>
 * This is a {@link HandledAdapter} that provides two possible exceptions, both
 * of type {@link SubmissionFieldAdapterException}:
 * <ul>
 * <li>{@link IncompatibleTypesSubmissionException} if the provided class is not
 * compatible with the class or value stored in the submission
 * <li>{@link MissingKeySubmissionException} if there is no value for the
 * specified key.
 * </ul>
 * 
 * @author Aaron Faanes
 * 
 * @param <K>
 *            the type of key used for the {@code Submission} object
 * @param <T>
 *            the type of object contained in the field
 */
public final class SubmissionFieldAdapter<K, T> extends
        AbstractHandledAdapter<Submission<K>, T, SubmissionFieldAdapterException> {

    private final Class<T> type;
    private final K key;

    /**
     * Constructs a {@link SubmissionFieldAdapter} that uses the specified key
     * and type.
     * 
     * @param key
     *            the value of the key used to retrieve the value
     * @param type
     *            the type of value that should be retrieved
     */
    public SubmissionFieldAdapter(final K key, final Class<T> type) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        this.key = key;
        this.type = type;
    }

    @Override
    public T adapt(final Submission<K> source) {
        if (source == null) {
            return null;
        }
        if (!source.containsKey(this.getKey())) {
            this.dispatch(new MissingKeySubmissionException(source, this));
            return null;
        }
        final Class<?> sourceType = source.getType(this.getKey());
        if (!this.getType().isAssignableFrom(sourceType)) {
            this.dispatch(new IncompatibleTypesSubmissionException(source, this));
            return null;
        }
        return source.get(this.getKey(), this.getType());
    }

    /**
     * Returns the key for this adapter. This key is used to retrieve a field
     * value from a {@link Submission} object.
     * 
     * @return the key for this adapter used to retrieve a field value from a
     *         given submission
     */
    public K getKey() {
        return this.key;
    }

    /**
     * Returns the type of value expected to be stored in {@link Submission}
     * objects at this adapter's key.
     * 
     * @return the type of value expected at this adapter's key in a given
     *         submission
     */
    public Class<T> getType() {
        return this.type;
    }

    /**
     * A {@link SubmissionFieldAdapterException} that indicates that there is no
     * value in the specified submission object for this adapter's key.
     * 
     * @author Aaron Faanes
     * 
     */
    public static final class MissingKeySubmissionException extends SubmissionFieldAdapterException {

        private static final long serialVersionUID = 5131881689746910218L;

        private static final String MESSAGE = "The submission does not contain the specified key.";

        private MissingKeySubmissionException(final Submission<?> submission, final SubmissionFieldAdapter<?, ?> adapter) {
            super(submission, adapter, MESSAGE);
        }

    }

    /**
     * A {@link SubmissionFieldAdapterException} that indicates that the types
     * are not compatible. This exception occurs whenever the {@link Submission}
     * object provided contains the requested key, but does not contain a valid
     * type for that key.
     * 
     * 
     * @author Aaron Faanes
     * 
     */
    public static final class IncompatibleTypesSubmissionException extends SubmissionFieldAdapterException {

        private static final long serialVersionUID = 3316125370427580200L;

        private static final String MESSAGE = "The types are not compatible.";

        private IncompatibleTypesSubmissionException(final Submission<?> submission,
                final SubmissionFieldAdapter<?, ?> adapter) {
            super(submission, adapter, MESSAGE);
        }

    }

}
