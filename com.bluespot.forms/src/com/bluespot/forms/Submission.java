package com.bluespot.forms;

import java.util.Map;

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

    /**
     * Returns the unmodifiable map that contains the mappings between keys and
     * their types.
     * 
     * @return the unmodifiable map that contains the mappings between keys and
     *         their types
     */
    public Map<? extends K, Class<?>> getTypes();

    /**
     * Returns a map that represents the state of this submission. The returned
     * map may be freely modified, but changes to that map will not be reflected
     * in this submission object.
     * 
     * @return a map that represents the state of this submission.
     */
    public Map<? extends K, Object> asMap();

    /**
     * Returns whether this submission contains a mapping for the specified key.
     * 
     * @param key
     *            the key that is used in this search. It cannot be null.
     * @return {@code true} if this submission contains a mapping for the
     *         specified key
     * @throws NullPointerException
     *             if {@code key} is null
     */
    public boolean containsKey(final K key);

    /**
     * Returns the type that will be returned for the specified key. It is
     * guaranteed that, for the specified key, a value returned by
     * {@link #get(Object, Class)} will be a type or subtype of the returned
     * class.
     * 
     * @param key
     *            the non-null key that is used to retrieve the related type. It
     *            must not be null.
     * @return the type that relates to the specified key
     * @throws NullPointerException
     *             if {@code key} is null
     * @throws IllegalArgumentException
     *             if there is no type that relates to the specified key
     */
    public Class<?> getType(final K key);

    /**
     * Returns the value that is mapped to the specified key.
     * 
     * @param <T>
     *            the type of value that will be returned
     * @param key
     *            the key that corresponds to the requested value
     * @param type
     *            the expected type or supertype of the returned value.
     * @return the value that is mapped to the specified key. It is guaranteed
     *         to be a type or subtype of the specified {@code type}
     * @throws ClassCastException
     *             if the retrieved value is not compatible with the provided
     *             type
     * @throws NullPointerException
     *             if either argument is null
     * @throws IllegalArgumentException
     *             if {@code key} does not correspond to a valid key in this
     *             submission
     */
    public <T> T get(final K key, final Class<T> type);
}
