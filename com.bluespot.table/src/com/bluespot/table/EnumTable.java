package com.bluespot.table;

import java.awt.Point;

/**
 * A {@code Table} implementation optimized for use with enumerations.
 * 
 * @author Aaron Faanes
 *
 * @param <T> The type of element in this table
 */
public class EnumTable<T extends Enum<T>> extends AbstractTable<T> {

    protected final int[][] internalTable;

    protected final Class<T> elementType;

    protected final T[] enumValues;

    public EnumTable(Class<T> elementType, T[] enumValues, int width, int height, T defaultValue) {
        super(defaultValue);
        this.elementType = elementType;
        this.enumValues = enumValues;
        this.internalTable = new int[height][width];
        this.clear();
    }

    @Override
    public int getHeight() {
        return this.internalTable.length;
    }

    @Override
    public int getWidth() {
        if(this.getHeight() == 0)
            return 0;
        return this.internalTable[0].length;
    }

    @Override
    public T put(Point location, T element) {
        T old = this.get(location);
        this.internalTable[location.y][location.x] = element.ordinal();
        return old;
    }

    @Override
    public T get(Point location) {
        return this.enumValues[this.internalTable[location.y][location.x]];
    }

    @Override
    protected T getDefaultValue() {
        return this.defaultValue;
    }

}
