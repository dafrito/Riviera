package com.bluespot.table;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;

import com.bluespot.table.iteration.NaturalTableIteration;

/**
 * Skeletal implementation of the {@link Table} interface.
 * 
 * Be sure to also implement {@link AbstractTable#get(Point)} when implementing
 * this class.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            Type of the elements in this table
 */
public abstract class AbstractTable<T> implements Table<T> {

	/**
	 * Represents a view of a portion of some parent {@link Table}. This class
	 * has no values of its own; all operations are passed to the parent table.
	 * <p>
	 * This allows clients to work with portions of some table very easily,
	 * without having to pass bounds to every method. For example, to clear a
	 * portion of the table, you may simply write:
	 * 
	 * <pre>
	 * table.subTable(yourOrigin, yourSize).clear();
	 * </pre>
	 * 
	 * @param <T>
	 *            the type of element that this table will contain
	 */
	public static class SubTable<T> extends AbstractTable<T> {

		private final Point origin;
		private final Table<T> owningTable;
		private final Dimension size;

		/**
		 * Constructs a view into a portion of the specified table.
		 * 
		 * @param owningTable
		 *            the table that is being viewed. Since it controls the
		 *            values returned by this table, it "owns" this table
		 * @param origin
		 *            the origin of this table, relative to the parent table
		 * @param size
		 *            the size of the new table
		 * @param defaultValue
		 *            the default value of the table. This will be used to
		 *            populate removed or unset values
		 * @throws IndexOutOfBoundsException
		 *             if the bounds provided are not valid for the specified
		 *             table
		 */
		public SubTable(final Table<T> owningTable, final Point origin, final Dimension size, final T defaultValue) {
			super(defaultValue);
			AbstractTable.validateLocation(origin, owningTable.getWidth(), owningTable.getHeight());
			if (origin.x + size.width > owningTable.getWidth()) {
				throw new IndexOutOfBoundsException("subTable's X-dimension cannot extend outside of this table");
			}
			if (origin.y + size.height > owningTable.getHeight()) {
				throw new IndexOutOfBoundsException("subTable's Y-dimension cannot extend outside of this table");
			}

			this.owningTable = owningTable;
			this.origin = new Point(origin);
			this.size = new Dimension(size);
		}

		@Override
		public T get(final Point location) {
			return this.owningTable.get(this.translate(location));
		}

		@Override
		public int getHeight() {
			return this.size.height;
		}

		/**
		 * @return this table's origin, relative to its parent table
		 */
		public Point getOrigin() {
			return this.origin;
		}

		@Override
		public int getWidth() {
			return this.size.width;
		}

		@Override
		public T put(final Point location, final T element) {
			return this.owningTable.put(this.translate(location), element);
		}

		/**
		 * Translates the specified point from this table's origin to the parent
		 * table's origin.
		 * 
		 * @param original
		 *            the point to translate
		 * @return a point that is properly adjusted for use with the parent
		 *         table.
		 */
		protected Point translate(final Point original) {
			AbstractTable.validateLocation(original, this.getWidth(), this.getHeight());
			final Point point = new Point(original);
			point.translate(this.getOrigin().x, this.getOrigin().y);
			return point;
		}

	}

	/**
	 * The default value. Any removed or unset points should appear to have this
	 * value.
	 */
	protected final T defaultValue;

	/**
	 * Constructs a table using the specified default value.
	 * 
	 * @param defaultValue
	 *            the default value. Any removed or unset points will appear to
	 *            have this value.
	 */
	public AbstractTable(final T defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void clear() {
		for (final TableIterator<T> iterator = this.tableIterator(); iterator.hasNext();) {
			iterator.next();
			iterator.remove();
		}
	}

	@Override
	public T get(final Point location) {
		AbstractTable.validateLocation(location, this.getWidth(), this.getHeight());
		return this.getDefaultValue();
	}

	@Override
	public Iterator<T> iterator() {
		return this.tableIterator();
	}

	@Override
	public T remove(final Point location) {
		AbstractTable.validateLocation(location, this.getWidth(), this.getHeight());
		return this.put(location, this.getDefaultValue());
	}

	@Override
	public int size() {
		return this.getWidth() * this.getHeight();
	}

	@Override
	public Table<T> subTable(final Point newOrigin) {
		return this.subTable(newOrigin, new Dimension(this.getWidth() - newOrigin.x, this.getHeight() - newOrigin.y));
	}

	@Override
	public Table<T> subTable(final Point newOrigin, final Dimension size) {
		AbstractTable.validateLocation(newOrigin, this.getWidth(), this.getHeight());

		return new SubTable<T>(this, newOrigin, size, this.defaultValue);
	}

	@Override
	public TableIterator<T> tableIterator() {
		return new StrategyTableIterator<T>(this, NaturalTableIteration.getInstance());
	}

	/**
	 * Returns a suitable default value for this table.
	 * <p>
	 * The default implementation returns {@code null}.
	 * 
	 * @return the default value.
	 * @see Table#clear()
	 * @see Table#remove(Point)
	 */
	protected T getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * Utility method for ensuring the specified is within the bounds of this
	 * table. If the point is outside the bounds, an
	 * {@link IndexOutOfBoundsException} is thrown.
	 * <p>
	 * Note that this method uses this table's bounds, not any parent table.
	 * 
	 * @param point
	 *            the point to validate
	 * @param width
	 *            the width of the table
	 * @param height
	 *            the height of the table
	 * @throws IndexOutOfBoundsException
	 *             if the point is out of bounds
	 */
	protected static void validateLocation(final Point point, final int width, final int height) {
		if (point.x < 0) {
			throw new IndexOutOfBoundsException("X cannot be negative");
		}
		if (point.y < 0) {
			throw new IndexOutOfBoundsException("Y cannot be negative");
		}
		if (point.x >= width) {
			throw new IndexOutOfBoundsException("X exceeds the width of this table");
		}
		if (point.y >= height) {
			throw new IndexOutOfBoundsException("Y exceeds the height of this table");
		}
	}

}
