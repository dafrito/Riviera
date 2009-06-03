package com.bluespot.table;

import java.awt.Point;

import com.bluespot.table.iteration.TableIteration;

/**
 * Uses {@link TableIteration} strategies within a iterator.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            The type of element in this iterator's owning table
 * @see TableIterator
 */
public class StrategyTableIterator<T> extends AbstractTableIterator<T> {

	/**
	 * The strategy used for iteration
	 */
	protected final TableIteration strategy;

	/**
	 * Constructs an iterator over the specified table using the specified
	 * strategy
	 * 
	 * @param table
	 *            the table used for iteration
	 * @param strategy
	 *            the strategy used for iteration
	 */
	public StrategyTableIterator(final Table<T> table, final TableIteration strategy) {
		super(table);
		this.strategy = strategy;
	}

	@Override
	public boolean hasNext() {
		if (this.currentPoint == null) {
			return this.table.size() > 0;
		}
		final Point nextPoint = new Point(this.currentPoint);
		this.strategy.next(nextPoint);
		// If the comparison is < 0, our reference is less than our next point,
		// which means there was no last-to-origin wrapping. Otherwise,
		// referencePoint
		// is "further" away than the next point, which means we did wrap.
		return this.strategy.comparePoints(this.table, this.currentPoint, nextPoint) < 0;
	}

	@Override
	public boolean hasPrevious() {
		if (this.currentPoint == null) {
			return false;
		}
		final Point previousPoint = new Point(this.currentPoint);
		this.strategy.previous(previousPoint);
		// If the comparison is < 0, our reference is less than our next point,
		// which means there was no last-to-origin wrapping. Otherwise,
		// referencePoint
		// is "further" away than the next point, which means we did wrap.
		return this.strategy.comparePoints(this.table, this.currentPoint, previousPoint) > 0;
	}

	@Override
	public T next() {
		if (this.currentPoint == null) {
			this.currentPoint = new Point(0, 0);
		} else {
			this.strategy.next(this.currentPoint);
			this.strategy.wrap(this.table, this.currentPoint, this.currentPoint);
		}
		return this.table.get(this.currentPoint);
	}

	@Override
	public T previous() {
		this.strategy.previous(this.currentPoint);
		this.strategy.wrap(this.table, this.currentPoint, this.currentPoint);
		return this.table.get(this.currentPoint);
	}

}
