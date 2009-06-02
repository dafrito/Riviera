package com.bluespot.table;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;

import com.bluespot.table.iteration.NaturalTableIteration;

/**
 * Skeletal implementation of the {@link Table} interface.
 * 
 * Be sure to also implement {@link AbstractTable#get(Point)} when implementing this class.
 * 
 * @author Aaron Faanes
 * @param <T> Type of the elements in this table
 */
public abstract class AbstractTable<T> implements Table<T> {
    
    public AbstractTable() {
        this.defaultValue = null;
    }
    
    public AbstractTable(T defaultValue) {
         this.defaultValue = defaultValue;
    }

    protected final T defaultValue;
    
    /**
     * Returns a suitable default value for this table.
     * <p>
     * The default implementation returns {@code null}.
     * @return the default value.
     * @see Table#clear()
     * @see Table#remove(Point)
     */
    protected T getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void clear() {
        for(TableIterator<T> iterator = this.tableIterator(); iterator.hasNext();) {
            iterator.next();
            iterator.remove();
        }
    }

    @Override
    public T get(Point location) {
        this.validateLocation(location);
        return this.getDefaultValue();
    }

    @Override
    public int size() {
        return this.getWidth() * this.getHeight();
    }

    @Override
    public T remove(Point location) {
        this.validateLocation(location);
        return this.put(location, this.getDefaultValue());
    }

    @Override
    public Table<T> subTable(Point newOrigin, Dimension size) {
        this.validateLocation(newOrigin);
        if(newOrigin.x + size.width > this.getWidth()) {
            throw new IndexOutOfBoundsException("subTable's X-dimension cannot extend outside of this table");
        }
        if(newOrigin.y + size.height > this.getHeight()) {
            throw new IndexOutOfBoundsException("subTable's Y-dimension cannot extend outside of this table");
        }
        return new SubTable<T>(this, newOrigin, size, this.defaultValue);
    }

    @Override
    public TableIterator<T> tableIterator() {
        return new StrategyTableIterator<T>(this, NaturalTableIteration.getInstance());
    }

    @Override
    public Iterator<T> iterator() {
        return this.tableIterator();
    }

    @Override
    public Table<T> subTable(Point newOrigin) {
        return this.subTable(newOrigin, new Dimension(this.getWidth() - newOrigin.x, this.getHeight() - newOrigin.y));
    }
    
    protected void validateLocation(Point point) {
        if(point.x < 0)
            throw new IndexOutOfBoundsException("X cannot be negative");
        if(point.y < 0)
            throw new IndexOutOfBoundsException("Y cannot be negative");
        if(point.x >= this.getWidth())
            throw new IndexOutOfBoundsException("X exceeds the width of this table");
        if(point.y >= this.getHeight())
            throw new IndexOutOfBoundsException("Y exceeds the height of this table");
    }

    protected static class SubTable<T> extends AbstractTable<T> {

        private final Table<T> owningTable;
        private final Dimension size;
        private final Point origin;

        public SubTable(Table<T> owningTable, Point origin, Dimension size, T defaultValue) {
            super(defaultValue);
            this.owningTable = owningTable;
            this.origin = new Point(origin);
            this.size = new Dimension(size);
        }

        @Override
        public T get(Point location) {
            return this.owningTable.get(this.translate(location));
        }

        @Override
        public int getHeight() {
            return this.size.height;
        }

        @Override
        public int getWidth() {
            return this.size.width;
        }

        @Override
        public T put(Point location, T element) {
            return this.owningTable.put(this.translate(location), element);
        }

        protected Point translate(Point original) {
            this.validateLocation(original);
            Point point = new Point(original);
            point.translate(this.origin.x, this.origin.y);
            return point;
        }

    }

}
