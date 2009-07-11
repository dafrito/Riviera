package com.bluespot.logic.visitors;

import java.util.Collection;

/**
 * A {@link Visitor} that prunes a specified collection. For every element that
 * is accepted by this visitor, a call to {@link Collection#remove(Object)} will
 * be made. The actual action taken by the specified collection is, of course,
 * implementation-dependent.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value that is accepted by this visitor
 * @see PopulatingVisitor
 */
public final class PruningVisitor<T> implements Visitor<T> {

    private final Collection<? super T> collection;

    /**
     * Constructs a {@link PruningVisitor} that will prune the specified
     * collection.
     * 
     * @param collection
     *            the collection that is pruned by this visitor
     * @throws NullPointerException
     *             if {@code collection} is null
     */
    public PruningVisitor(final Collection<? super T> collection) {
        if (collection == null) {
            throw new NullPointerException("collection is null");
        }
        this.collection = collection;
    }

    /**
     * Returns the collection that is pruned by this visitor.
     * 
     * @return the collection that is pruned by this visitor
     */
    public Collection<? super T> getCollection() {
        return this.collection;
    }

    /**
     * Removes the specified value from this visitor's collection.
     */
    public void accept(final T value) {
        this.getCollection().remove(value);
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
         * We intentionally use identity here; we don't want false positives for
         * lists that contain the same items
         */
        if (visitor.getCollection() != this.getCollection()) {
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
