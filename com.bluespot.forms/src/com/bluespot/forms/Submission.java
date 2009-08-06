package com.bluespot.forms;

/**
 * Represents a form of submission. These may be mutable but should not be
 * concurrently accessed. Specifically, the values they return during a period
 * of validation should be consistent. Violating this constraint will not allow
 * validation to work.
 * <p>
 * If you require a {@link Submission} that is concurrently accessed, classes
 * will exist to allow this sort of behavior. Typically, a snapshot submission
 * object will be created, and <em>that</em> object is used during validation.
 * <p>
 * This class is fundamentally related to a {@link Schema}, as that class
 * maintains the rules regarding the process of validation.
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
public interface Submission<K> {

    public Class<?> getType(final K keyValue);

    public <T> T get(final K keyValue, final Class<T> type);
}
