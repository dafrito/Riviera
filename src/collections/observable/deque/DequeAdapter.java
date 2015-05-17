package collections.observable.deque;

/**
 * A no-op {@link DequeListener} implementation.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of element in the observed deque
 */
public class DequeAdapter<E> implements DequeListener<E> {

	@Override
	public void dequeChanged() {
		// Do nothing
	}

	@Override
	public void firstElementAdded(final E oldFirstElement) {
		// Do nothing
	}

	@Override
	public void firstElementRemoved(final E oldFirstElement) {
		// Do nothing
	}

	@Override
	public void lastElementAdded(final E oldLastElement) {
		// Do nothing
	}

	@Override
	public void lastElementRemoved(final E oldLastElement) {
		// Do nothing
	}

}
