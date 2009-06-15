package com.bluespot.logic.visitors;

import com.bluespot.logic.adapters.Adapter;

/**
 * A {@link Visitor} that uses a specified {@link Adapter} to convert accepted
 * values. The converted values are then sent to the specified target visitor.
 * Converted values are sent as-is to the target visitor; no filtering is
 * performed.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of value that is initially accepted by this visitor
 * @param <D>
 *            the type of value that is sent to the target visitor
 * @see Adapter
 */
public class AdaptingVisitor<S, D> implements Visitor<S> {

    private final Adapter<? super S, ? extends D> adapter;
    private final Visitor<? super D> targetVisitor;

    /**
     * Constructs a new {@link AdaptingVisitor} that uses the specified adapter
     * to convert accepted values. The converted values are then sent to the
     * specified target visitor.
     * 
     * @param adapter
     *            the adapter that performs conversion for this visitor
     * @param targetVisitor
     *            the visitor that ultimately accepts the converted values
     * @throws NullPointerException
     *             if either {@code adapter} or {@code targetVisitor} is null
     */
    public AdaptingVisitor(final Adapter<? super S, ? extends D> adapter, final Visitor<? super D> targetVisitor) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        if (targetVisitor == null) {
            throw new NullPointerException("targetVisitor is null");
        }
        this.adapter = adapter;
        this.targetVisitor = targetVisitor;
    }

    /**
     * Returns this visitor's target visitor. It will accept all converted
     * values sent to this visitor.
     * 
     * @return this visitor's target visitor
     */
    public Visitor<? super D> getTargetVisitor() {
        return this.targetVisitor;
    }

    /**
     * Returns the adapter used to convert values sent to this visitor to values
     * that are acceptable to the target visitor.
     * 
     * @return the adapter used to convert accepted values
     */
    public Adapter<? super S, ? extends D> getAdapter() {
        return this.adapter;
    }

    public void accept(final S value) {
        this.getTargetVisitor().accept(this.getAdapter().adapt(value));
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AdaptingVisitor<?, ?>)) {
            return false;
        }
        final AdaptingVisitor<?, ?> visitor = (AdaptingVisitor<?, ?>) obj;
        if (!this.getAdapter().equals(visitor.getAdapter())) {
            return false;
        }
        if (!this.getTargetVisitor().equals(visitor.getTargetVisitor())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.getAdapter().hashCode();
        result = 31 * result + this.getTargetVisitor().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("AdaptingVisitor - adapter: %s, target: %s", this.getAdapter(), this.getTargetVisitor());
    }

}
