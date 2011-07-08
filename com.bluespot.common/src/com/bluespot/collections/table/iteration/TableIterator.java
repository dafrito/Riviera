package com.bluespot.collections.table.iteration;

import java.awt.Point;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.vectors.Vector3i;

/**
 * An iterator over a {@link Table}.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            Type of element contained in this iterator's parent table
 */
public interface TableIterator<T> extends Iterator<T> {

	/**
	 * Gets the value at the current location of this iterator.
	 * <p>
	 * This method will implicitly call {@link TableIterator#next} if the
	 * iteration hasn't been explicitly started.
	 * 
	 * @return the current value
	 * @throws NoSuchElementException
	 *             if iteration was implicitly started, but the table was empty
	 * @see Table#get(Point)
	 */
	public T get();

	/**
	 * Gets the current location of this iterator.
	 * <p>
	 * This method will implicitly call {@link TableIterator#next} if the
	 * iteration hasn't been explicitly started.
	 * 
	 * @return the current location as a frozen vector
	 * @throws NoSuchElementException
	 *             if iteration was implicitly started, but the table was empty
	 */
	public Vector3i location();

	/**
	 * Returns the magnitude of movement from the previous position to the
	 * current position.
	 * <p>
	 * 
	 * @return the magnitude of movement during this iteration
	 */
	public Vector3i offset();

	/**
	 * Returns whether the location immediately after the current location will
	 * be contained inside the table.
	 * <p>
	 * This method is used to implement wrapping, as it would return
	 * {@code false} if the next location would be after the end of the table.
	 * Looping iterators, on the other hand, would always return {@code true}
	 * from this method.
	 * 
	 * @return {@code true} if the next point is contained inside the table,
	 *         {@code false} otherwise.
	 */
	@Override
	public boolean hasNext();

	/**
	 * Returns whether the location immediately before the current location will
	 * be contained inside the table.
	 * <p>
	 * This method is used to implement wrapping, as it would return
	 * {@code false} if the previous location would be before the origin of the
	 * table. Looping iterators, on the other hand, would always return
	 * {@code true} from this method.
	 * 
	 * @return {@code true} if the previous point is contained inside the table,
	 *         {@code false} otherwise.
	 */
	public boolean hasPrevious();

	/**
	 * Gets the previous element from the table.
	 * 
	 * @return the previous element
	 * @throws NoSuchElementException
	 *             if there are no elements before the current location
	 */
	public T previous();

	/**
	 * Replaces the value at the current location with the specified value.
	 * <p>
	 * This method will implicitly call {@link TableIterator#next} if the
	 * iteration hasn't been explicitly started.
	 * 
	 * @param value
	 *            the new value
	 * @return the old value at the current location
	 * @throws NoSuchElementException
	 *             if iteration was implicitly started, but the table was empty
	 * @see Table#put(Point, Object)
	 */
	public T put(T value);

}
