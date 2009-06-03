package com.bluespot.table;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code Table} implementation optimized for use with tables that contain a
 * limited number of values, like enumerations.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            The type of element in this table
 */
public class ChoiceTable<T> extends AbstractTable<T> {

	private final Map<T, Integer> choiceMap = new HashMap<T, Integer>();

	private transient final T[] choices;

	private final int[][] internalTable;

	/**
	 * Constructs an {@link ChoiceTable} using the specified enumeration.
	 * 
	 * @param choices
	 *            the choices that this table has for its values
	 * @param width
	 *            the width of this table
	 * @param height
	 *            the height of this table
	 * @param defaultValue
	 *            the default value of this table, used when values are removing
	 *            or missing
	 */
	public ChoiceTable(final T[] choices, final int width, final int height, final T defaultValue) {
		super(defaultValue);

		this.choices = Arrays.copyOf(choices, choices.length);
		for (int i = 0; i < this.choices.length; i++) {
			this.choiceMap.put(this.choices[i], i);
		}

		if (!this.isValidChoice(defaultValue)) {
			throw new IllegalArgumentException("Default value is not a valid choice");
		}

		this.internalTable = new int[height][width];

		// Clear is necessary to set our values to their defaults; otherwise
		// we'd have to force 0 to be the default value.
		this.clear();
	}

	@Override
	public T get(final Point location) {
		return this.choices[this.internalTable[location.y][location.x]];
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

	/**
	 * Returns whether the specified choice is valid as a value in this table.
	 * 
	 * @param choice
	 *            the choice to check
	 * @return {@code true} if this table is allowed to contain the specified
	 *         choice, {@code false} otherwise
	 */
	public boolean isValidChoice(final T choice) {
		return this.choiceMap.containsKey(choice);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException
	 *             if the valid is not a valid choice
	 */
	@Override
	public T put(final Point location, final T element) {
		final T old = this.get(location);

		final Integer value = this.choiceMap.get(element);
		if (value == null) {
			throw new IllegalArgumentException("Value is not a valid choice");
		}
		this.internalTable[location.y][location.x] = value;

		return old;
	}

	@Override
	protected T getDefaultValue() {
		return this.defaultValue;
	}
}
