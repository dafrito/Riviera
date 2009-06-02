package com.bluespot.table;

import java.awt.Point;

/**
 * A {@code Table} implementation optimized for use with enumerations.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            The type of element in this table
 */
public class EnumTable<T extends Enum<T>> extends AbstractTable<T> {

	protected final Class<T> elementType;

	protected final T[] enumValues;

	protected final int[][] internalTable;

	public EnumTable(final Class<T> elementType, final T[] enumValues, final int width, final int height,
			final T defaultValue) {
		super(defaultValue);
		this.elementType = elementType;
		this.enumValues = enumValues;
		this.internalTable = new int[height][width];
		this.clear();
	}

	@Override
	public T get(final Point location) {
		return this.enumValues[this.internalTable[location.y][location.x]];
	}

	@Override
	public int getHeight() {
		return this.internalTable.length;
	}

	@Override
	public int getWidth() {
		if (this.getHeight() == 0) {
			return 0;
		}
		return this.internalTable[0].length;
	}

	@Override
	public T put(final Point location, final T element) {
		final T old = this.get(location);
		this.internalTable[location.y][location.x] = element.ordinal();
		return old;
	}

	@Override
	protected T getDefaultValue() {
		return this.defaultValue;
	}

}
