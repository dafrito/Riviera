package com.bluespot.table.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.table.ArrayTable;
import com.bluespot.table.Table;
import com.bluespot.table.TableIterator;
import com.bluespot.table.Tables;


public abstract class TableIteratorTest {
    
    protected TableIterator<Integer> iter;
    protected Table<Integer> table;

    public abstract TableIterator<Integer> newIterator(Table<Integer> targetTable);
    
    public Table<Integer> newTable(int width, int height) {
        return new ArrayTable<Integer>(width, height);
    }
    
    @Before
    public void setUp() {
        this.table = this.newTable(2, 2);
        this.iter = this.newIterator(this.table);
    }
    
    @Test
    public void testHasNext() {
        assertThat(this.newIterator(this.newTable(0, 0)).hasNext(), is(false));
        this.iter = this.newIterator(this.newTable(1, 1));
        assertThat(this.iter.hasNext(), is(true));
        assertThat(this.iter.next(), is((Integer)null));
        assertThat(this.iter.hasNext(), is(false));
    }

    @Test
    public void testEmptyTable() {
        assertThat(this.newIterator(this.newTable(0, 0)).hasNext(), is(false));
        assertThat(this.newIterator(this.newTable(0, 0)).hasPrevious(), is(false));
    }
    
    @Test
    public void testHasPrevious() {
        this.iter = this.newIterator(this.table = this.newTable(1, 1));
        Tables.fill(this.table, 1);
        assertThat(this.iter.hasPrevious(), is(false));
        assertThat(this.iter.next(), is(1));
        assertThat(this.iter.hasNext(), is(false));
        assertThat(this.iter.hasPrevious(), is(false));
    }

    @Test
    public void testPrevious() {
        this.iter = this.newIterator(this.table = this.newTable(2, 1));
        Tables.fillCount(this.table, 1);
        this.iter.next();
        this.iter.next();
        assertThat(this.iter.hasNext(), is(false));
        assertThat(this.iter.hasPrevious(), is(true));
        assertThat(this.iter.previous(), is(1));
        assertThat(this.iter.hasPrevious(), is(false));
        assertThat(this.iter.hasNext(), is(true));
    }
    
    @Test
    public void testImplicitGetLocation() {
        assertNotNull(this.iter.getLocation());
    }
    
    @Test
    public void testImplicitGetLocationWithProvidedPoint() {
        this.iter.getLocation(new Point());
    }

    @Test
    public void testImplicitRemove() {
        this.iter.remove();
        assertThat(this.iter.get(), is((Integer)null));
    }

    @Test
    public void testThrowOnGet() {
        Tables.fill(this.table, 1);
        assertThat(this.iter.get(), is(1));
    }

    @Test
    public void testImplicitPut() {
        Tables.fill(this.table, 1);
        assertThat(this.iter.put(2), is(1));
        assertThat(this.iter.get(), is(2));
    }
}
