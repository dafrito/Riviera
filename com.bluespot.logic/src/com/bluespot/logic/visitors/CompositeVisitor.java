package com.bluespot.logic.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * A {@link Visitor} that forwards all values to its child visitors.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value expected by this sentinel
 */
public final class CompositeVisitor<T> implements Visitor<T> {

    private final Collection<? extends Visitor<? super T>> visitors;

    /**
     * Constructs a {@link CompositeVisitor} that forwards any accepted value to
     * the specified visitors.
     * 
     * @param visitors
     *            the child visitors
     * @throws NullPointerException
     *             if any of the visitors are null
     */
    public CompositeVisitor(final Visitor<? super T>... visitors) {
        this(Arrays.asList(visitors));
    }

    /**
     * Constructs a {@link CompositeVisitor} that forwards any accepted value to
     * the specified collection of visitors.
     * 
     * @param visitors
     *            a collection of child visitors
     * @throws NullPointerException
     *             if {@code visitors} is null, or if any visitor contained in
     *             {@code visitors} is null
     */
    public CompositeVisitor(final Collection<? extends Visitor<? super T>> visitors) {
        if (visitors == null) {
            throw new NullPointerException("visitors is null");
        }
        this.visitors = Collections.unmodifiableList(new ArrayList<Visitor<? super T>>(visitors));
        if (this.visitors.contains(null)) {
            throw new NullPointerException("visitors cannot contain null elements");
        }
    }

    /**
     * Returns this visitors's children.
     * 
     * @return a view of this sentinel's visitors
     */
    public Collection<? extends Visitor<? super T>> getVisitors() {
        // It's safe to return our list directly since it's already
        // unmodifiable.
        return this.visitors;
    }

    /**
     * Forwards the specified value to all of the child visitor's of this
     * composite. Since this class makes no effort to recover from exceptions,
     * <em>this operation is not guaranteed to be atomic</em>. This is
     * unfortunate, but I believe it's safer than attempting to handle
     * exceptions or ignoring them until all visitors have been called.
     */
    public void accept(final T value) {
        for (final Visitor<? super T> visitor : this.visitors) {
            visitor.accept(value);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CompositeVisitor<?>)) {
            return false;
        }
        final CompositeVisitor<?> other = (CompositeVisitor<?>) obj;
        if (!this.getVisitors().equals(other.getVisitors())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 11;
        result = 31 * result + this.getVisitors().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("CompositeVisitor[%d visitors]", this.getVisitors().size());
    }

}
