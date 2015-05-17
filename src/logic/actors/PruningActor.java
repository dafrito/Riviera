package logic.actors;

import java.util.Collection;

/**
 * A {@link Actor} that prunes a specified collection.
 * {@link Collection#remove(Object)} on the specified collection for every given
 * argument. The actual action taken by the specified collection is, of course,
 * implementation-dependent.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value that is accepted by this sink
 * @see PopulatingActor
 */
public final class PruningActor<T> implements Actor<T> {

	private final Collection<? super T> collection;

	/**
	 * Constructs a {@link PruningActor} that will prune the specified
	 * collection.
	 * 
	 * @param collection
	 *            the collection that is pruned by this actor
	 * @throws NullPointerException
	 *             if {@code collection} is null
	 */
	public PruningActor(final Collection<? super T> collection) {
		if (collection == null) {
			throw new NullPointerException("collection is null");
		}
		this.collection = collection;
	}

	/**
	 * Returns the collection that is pruned by this actor.
	 * 
	 * @return the collection that is pruned by this actor
	 */
	public Collection<? super T> getCollection() {
		return this.collection;
	}

	/**
	 * Removes the specified value from this actor's collection.
	 */
	@Override
	public void receive(final T value) {
		this.getCollection().remove(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PruningActor<?>)) {
			return false;
		}
		final PruningActor<?> other = (PruningActor<?>) obj;
		/*
		 * We intentionally use identity here; we don't want false positives for
		 * lists that contain the same items
		 */
		if (other.getCollection() != this.getCollection()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 19;
		result = 31 * result + this.getCollection().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("remove from collection \"%s\"", this.getCollection());
	}
}
