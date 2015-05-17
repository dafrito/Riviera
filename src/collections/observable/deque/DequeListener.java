package collections.observable.deque;

import java.util.Deque;

/**
 * @author Aaron Faanes
 * @param <E>
 *            the type of element contained in the observed deque
 */
public interface DequeListener<E> {

	/**
	 * Invoked when a new element has been added at the end of this deque.
	 * <p>
	 * The new first element is attainable via {@link Deque#peekFirst()}.
	 * 
	 * @param oldFirstElement
	 *            the previous first element of this deque
	 */
	public void firstElementAdded(E oldFirstElement);

	/**
	 * Invoked when a new element has been added at the end of this deque.
	 * <p>
	 * The new last element is attainable via {@link Deque#peekLast()}.
	 * 
	 * @param oldLastElement
	 *            the previous last element of this deque
	 */

	public void lastElementAdded(E oldLastElement);

	/**
	 * Invoked when the last element is removed from the observed deque.
	 * <p>
	 * The new last element is attainable via {@link Deque#peekLast()}.
	 * 
	 * @param oldLastElement
	 *            the removed element
	 */
	public void lastElementRemoved(E oldLastElement);

	/**
	 * Invoked when the first element is removed from the watched deque.
	 * <p>
	 * The new first element is attainable via {@link Deque#peekFirst()}.
	 * 
	 * @param oldFirstElement
	 *            the removed element
	 */
	public void firstElementRemoved(E oldFirstElement);

	/**
	 * Invoked when the deque has changed so dramatically that it cannot be
	 * described with any other method. This is typically because of non-deque
	 * like behavior, such as removing elements in the middle of the list and so
	 * forth.
	 */
	public void dequeChanged();
}
