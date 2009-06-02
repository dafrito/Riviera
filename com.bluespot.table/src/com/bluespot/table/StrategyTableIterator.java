package com.bluespot.table;

import java.awt.Point;

import com.bluespot.table.iteration.TableIteration;

/**
 * Uses {@link TableIteration} strategies within a iterator.
 * 
 * @author Aaron Faanes
 * @param <T> The type of element in this iterator's owning table
 * @see TableIterator
 */
public class StrategyTableIterator<T> extends AbstractTableIterator<T> {

    protected final TableIteration strategy;

    public StrategyTableIterator(Table<T> table, TableIteration strategy) {
        super(table);
        this.strategy = strategy;
    }

    @Override
    public boolean hasNext() {
        if(this.currentPoint == null) {
            return this.table.size() > 0;
        }
        Point nextPoint = new Point(this.currentPoint);
        this.strategy.next(nextPoint);
        // If the comparison is < 0, our reference is less than our next point,
        // which means there was no last-to-origin wrapping. Otherwise,
        // referencePoint
        // is "further" away than the next point, which means we did wrap.
        return this.strategy.comparePoints(this.table, this.currentPoint, nextPoint) < 0;
    }

    @Override
    public T next() {
        if(this.currentPoint == null) {
            this.currentPoint = new Point(0, 0);
        } else {
            this.strategy.next(this.currentPoint);
            this.strategy.wrap(this.table, this.currentPoint, this.currentPoint);
        }
        return this.table.get(this.currentPoint);
    }

    @Override
    public boolean hasPrevious() {
        if(this.currentPoint == null) {
            return false;
        }
        Point previousPoint = new Point(this.currentPoint);
        this.strategy.previous(previousPoint);
        // If the comparison is < 0, our reference is less than our next point,
        // which means there was no last-to-origin wrapping. Otherwise,
        // referencePoint
        // is "further" away than the next point, which means we did wrap.
        return this.strategy.comparePoints(this.table, this.currentPoint, previousPoint) > 0;
    }

    @Override
    public T previous() {
        this.strategy.previous(this.currentPoint);
        this.strategy.wrap(this.table, this.currentPoint, this.currentPoint);
        return this.table.get(this.currentPoint);
    }

}
