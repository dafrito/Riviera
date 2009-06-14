package com.bluespot.logic.visitors;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bluespot.logic.predicates.Predicate;

/**
 * A {@link Visitor} that guards a list of other {@link Visitor} objects with a
 * given {@link Predicate}. If the sentinel's predicate evaluates to {@code
 * true} for a given object, that object is passed each of the sentinel's
 * visitors.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value expected by this sentinel
 * @see SingleSentinel
 */
public final class CompositeSentinel<T> implements Visitor<T> {

    private final List<Visitor<? super T>> visitors = new CopyOnWriteArrayList<Visitor<? super T>>();
    private final Predicate<? super T> predicate;

    /**
     * Constructs a {@link CompositeSentinel} that uses the specified predicate
     * 
     * @param predicate
     *            the predicate that guards this sentinel's visitors
     * @throws NullPointerException
     *             if {@code predicate} is null
     */
    public CompositeSentinel(final Predicate<? super T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        this.predicate = predicate;
    }

    /**
     * Returns the {@link Predicate} used by this sentinel.
     * 
     * @return the {@code Predicate} used by this sentinel
     */
    public Predicate<? super T> getPredicate() {
        return this.predicate;
    }

    /**
     * Returns a view of this sentinel's visitors.
     * 
     * @return a view of this sentinel's visitors
     */
    public List<Visitor<? super T>> getVisitors() {
        return Collections.unmodifiableList(this.visitors);
    }

    /**
     * Registers a visitor as a listener to this sentinel. It will receive
     * values that evaluate to {@code true} using this sentinel's predicate.
     * 
     * @param visitor
     *            the visitor to add
     * @throws NullPointerException
     *             if {@code visitor} is null
     */
    public void addVisitor(final Visitor<? super T> visitor) {
        if (visitor == null) {
            throw new NullPointerException("visitor is null");
        }
        this.visitors.add(visitor);
    }

    /**
     * Removes the specified visitor from this sentinel. If the visitor is not
     * registered with this sentinel, no action is taken.
     * 
     * @param visitor
     *            the visitor to remove
     */
    public void removeVisitor(final Visitor<? super T> visitor) {
        this.visitors.remove(visitor);
    }

    /**
     * Checks the specified value. If it passes this sentinel's test, it will be
     * passed to all of this sentinel's visitors.
     * 
     * @param value
     *            the value to check
     */
    public void accept(final T value) {
        if (!this.predicate.test(value)) {
            return;
        }
        for (final Visitor<? super T> visitor : this.visitors) {
            visitor.accept(value);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CompositeSentinel<?>)) {
            return false;
        }
        final CompositeSentinel<?> other = (CompositeSentinel<?>) obj;
        if (!this.getPredicate().equals(other.getPredicate())) {
            return false;
        }
        if (!this.getVisitors().equals(other.getVisitors())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.getPredicate().hashCode();
        result = 31 * result + this.getVisitors().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("CompositeSentinel[predicate: %s, visitors: %s]", this.getPredicate(), this.getVisitors());
    }

}
