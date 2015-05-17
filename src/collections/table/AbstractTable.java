package collections.table;

import java.util.Iterator;

import collections.table.iteration.NaturalTableIterator;
import collections.table.iteration.TableIterator;
import geom.vectors.Vector3i;

/**
 * Skeletal implementation of the {@link Table} interface.
 * 
 * Be sure to also implement {@link AbstractTable#get(Vector3i)} when
 * implementing this class.
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

		private final Vector3i origin;
		private final Table<T> owningTable;
		private final Vector3i size;

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
		public SubTable(final Table<T> owningTable, final Vector3i origin, final Vector3i size, final T defaultValue) {
			super(defaultValue);
			AbstractTable.validateLocation(origin, owningTable.width(), owningTable.height());
			if (origin.x() + size.x() > owningTable.width()) {
				throw new IndexOutOfBoundsException("subTable's X-dimension cannot extend outside of this table");
			}
			if (origin.y() + size.y() > owningTable.height()) {
				throw new IndexOutOfBoundsException("subTable's Y-dimension cannot extend outside of this table");
			}

			this.owningTable = owningTable;
			this.origin = origin.toFrozen();
			this.size = size.toFrozen();
		}

		@Override
		public T get(final Vector3i location) {
			return this.owningTable.get(this.translate(location));
		}

		@Override
		public int height() {
			return this.size.y();
		}

		/**
		 * @return this table's origin, relative to its parent table
		 */
		public Vector3i origin() {
			return this.origin;
		}

		@Override
		public int width() {
			return this.size.x();
		}

		@Override
		public T put(final Vector3i location, final T element) {
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
		protected Vector3i translate(final Vector3i original) {
			AbstractTable.validateLocation(original, this.width(), this.height());
			return original.toMutable().add(this.origin());
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
	public T get(final Vector3i location) {
		AbstractTable.validateLocation(location, this.width(), this.height());
		return this.getDefaultValue();
	}

	@Override
	public T get(int x, int y) {
		return this.get(Vector3i.frozen(x, y));
	}

	@Override
	public Iterator<T> iterator() {
		return this.tableIterator();
	}

	@Override
	public T remove(final Vector3i location) {
		AbstractTable.validateLocation(location, this.width(), this.height());
		return this.put(location, this.getDefaultValue());
	}

	@Override
	public int size() {
		return this.width() * this.height();
	}

	@Override
	public Vector3i dimensions() {
		return Vector3i.frozen(this.width(), this.height(), 1);
	}

	@Override
	public Table<T> subTable(final Vector3i newOrigin) {
		return this.subTable(newOrigin, this.dimensions().toMutable().subtract(newOrigin));
	}

	@Override
	public Table<T> subTable(final Vector3i newOrigin, final Vector3i size) {
		AbstractTable.validateLocation(newOrigin, this.width(), this.height());
		return new SubTable<T>(this, newOrigin, size, this.defaultValue);
	}

	@Override
	public TableIterator<T> tableIterator() {
		return new NaturalTableIterator<T>(this);
	}

	/**
	 * Returns a suitable default value for this table.
	 * <p>
	 * The default implementation returns {@code null}.
	 * 
	 * @return the default value.
	 * @see Table#clear()
	 * @see Table#remove(Vector3i)
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
	protected static void validateLocation(final Vector3i point, final int width, final int height) {
		if (point.x() < 0) {
			throw new IndexOutOfBoundsException(String.format("X cannot be negative. X: %d", point.x()));
		}
		if (point.y() < 0) {
			throw new IndexOutOfBoundsException(String.format("Y cannot be negative. Y: %d", point.y()));
		}
		if (point.x() >= width) {
			throw new IndexOutOfBoundsException(String.format("X exceeds the width of this table. X: %d, width: %d", point.x(), width));
		}
		if (point.y() >= height) {
			throw new IndexOutOfBoundsException(String.format("Y exceeds the height of this table. Y: %d, height: %d", point.y(), height));
		}
	}
}
