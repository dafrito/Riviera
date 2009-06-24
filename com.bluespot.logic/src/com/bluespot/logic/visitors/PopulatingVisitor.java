package com.bluespot.logic.visitors;

import java.util.Collection;

/**
 * A {@link Visitor} that populates a specified collection. For every element
 * that is accepted by this visitor, a call to {@link Collection#add(Object)}
 * will be made. It is up to that collection whether it will actually add the
 * given element.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value that is accepted by this visitor
 * @see PruningVisitor
 */
public final class PopulatingVisitor<T> implements Visitor<T> {

    private final Collection<? super T> collection;

    /**
     * Constructs a {@link PopulatingVisitor} that will populate the specified
     * collection.
     * 
     * @param collection
     *            the collection that is populated by this visitor
     * @throws NullPointerException
     *             if {@code collection} is null
     */
    public PopulatingVisitor(final Collection<? super T> collection) {
        if (collection == null) {
            throw new NullPointerException("collection is null");
        }
        this.collection = collection;
    }

    /**
     * Returns the collection that is populated by this visitor.
     * 
     * @return the collection this is populated by this visitor
     */
    public Collection<? super T> getCollection() {
        return this.collection;
    }

    @Override
    public void accept(final T value) {
        this.getCollection().add(value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PopulatingVisitor<?>)) {
            return false;
        }
        final PopulatingVisitor<?> visitor = (PopulatingVisitor<?>) obj;
        /*
         * Intentionally use identity here; we don't want false positives for
         * lists that contain the same items
         */
        if (visitor.getCollection() != this.getCollection()) {
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
