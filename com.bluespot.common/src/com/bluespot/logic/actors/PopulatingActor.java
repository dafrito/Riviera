package com.bluespot.logic.actors;

import java.util.Collection;

/**
 * A {@link Actor} that populates a specified collection. Every element received
 * by this actor will be added to the underlying collection, using
 * {@link Collection#add(Object)} will be made. It is up to that collection
 * whether it will actually add the given element.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of the received value
 * @see PruningActor
 */
public final class PopulatingActor<T> implements Actor<T> {

	private final Collection<? super T> collection;

	/**
	 * Constructs a {@link PopulatingActor} that will populate the specified
	 * collection.
	 * 
	 * @param collection
	 *            the collection that is populated by this actor
	 * @throws NullPointerException
	 *             if {@code collection} is null
	 */
	public PopulatingActor(final Collection<? super T> collection) {
		if (collection == null) {
			throw new NullPointerException("collection is null");
		}
		this.collection = collection;
	}

	/**
	 * Returns the collection that is populated by this actor.
	 * 
	 * @return the collection this is populated by this actor
	 */
	public Collection<? super T> getCollection() {
		return this.collection;
	}

	@Override
	public void receive(final T value) {
		this.getCollection().add(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PopulatingActor<?>)) {
			return false;
		}
		final PopulatingActor<?> other = (PopulatingActor<?>) obj;
		/*
		 * Intentionally use identity here; we don't want false positives for
		 * lists that contain the same items
		 */
		if (other.getCollection() != this.getCollection()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 3;
		result = 31 * result + this.getCollection().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("add to collection \"%s\"", this.getCollection());
	}
}
