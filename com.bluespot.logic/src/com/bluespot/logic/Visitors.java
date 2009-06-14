package com.bluespot.logic;

import java.util.Collection;

import junit.framework.AssertionFailedError;

import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.visitors.AdaptingVisitor;
import com.bluespot.logic.visitors.PopulatingVisitor;
import com.bluespot.logic.visitors.PruningVisitor;

/**
 * A collection of factory methods for common sentinels and visitors.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Visitors {

    private Visitors() {
        // Suppress default constructor to ensure non-instantiability.
        throw new AssertionFailedError("Instantiation not allowed");
    }

    /**
     * Returns a new {@link Visitor} that accepts values of type {@code S},
     * converts them with the specified adapter, and passes the converted value
     * to the specified visitor.
     * 
     * @param <S>
     *            the type that is initially accepted by the returned visitor
     *            and converted by the specified adapter
     * @param <D>
     *            the type that is accepted by the specified visitor
     * @param adapter
     *            the adapter that performs the conversion of the accepted value
     * @param targetVisitor
     *            the visitor that ultimately accepted the converted value
     * @return a {@code Visitor} that adapts an accepted value and passes it to
     *         the specified visitor
     * @throws NullPointerException
     *             if either argument is null
     */
    public static <S, D> Visitor<S> withAdapter(final Adapter<? super S, ? extends D> adapter,
            final Visitor<? super D> targetVisitor) {
        return new AdaptingVisitor<S, D>(adapter, targetVisitor);
    }

    /**
     * Returns a new {@link Visitor} that adds all visited elements to the
     * specified collection. This method uses {@link Collection#add(Object)} and
     * does not respond to failed or ignored addition requests.
     * 
     * @param <T>
     *            the type of element in the collection
     * @param collection
     *            the collection that is populated by this visitor
     * @return a new {@code Visitor} that adds elements to the specified
     *         collection
     */
    public static <T> Visitor<T> populate(final Collection<? super T> collection) {
        return new PopulatingVisitor<T>(collection);
    }

    /**
     * Returns a new {@link Visitor} that removes all visited elements from the
     * specified collection. This method uses {@link Collection#remove(Object)}
     * and does not respond to failed or ignored addition requests.
     * 
     * @param <T>
     *            the type of element in the collection
     * @param collection
     *            the collection that is modified by this visitor
     * @return a new {@code Visitor} that adds elements to the specified
     *         collection
     */
    public static <T> Visitor<T> depopulate(final Collection<? super T> collection) {
        return new PruningVisitor<T>(collection);
    }
}
