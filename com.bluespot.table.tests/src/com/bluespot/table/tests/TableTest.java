package com.bluespot.table.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.table.StrategyTableIterator;
import com.bluespot.table.Table;
import com.bluespot.table.TableIterator;
import com.bluespot.table.Tables;
import com.bluespot.table.iteration.NaturalTableIteration;

public abstract class TableTest<T> {

    private Table<T> table;
    private final T defaultValue; 

    public TableTest(T defaultValue) {
        this.defaultValue = defaultValue;
    }


    public abstract Table<T> newTable(int width, int height);
    

    @Before
    public void setUp() {
        this.table = this.newTable(2, 2);
    }
    
    @Test
    public void testSize() {
        assertThat(this.newTable(1, 1).size(), is(1));
        assertThat(this.newTable(2, 2).size(), is(4));
        assertThat(this.newTable(2, 4).size(), is(8));
    }

    @Test
    public void testWidth() {
        assertThat(this.newTable(1, 1).getWidth(), is(1));
        assertThat(this.newTable(2, 4).getWidth(), is(2));
    }

    @Test
    public void testHeight() {
        assertThat(this.newTable(1, 1).getHeight(), is(1));
        assertThat(this.newTable(2, 4).getHeight(), is(4));
    }

    @Test
    public void testGetPutAndRemove() {
        Point point = new Point(0, 0);
        assertThat(this.table.get(point), is(this.getDefaultValue()));
        assertThat(this.table.put(point, this.getValue()), is(this.getDefaultValue()));
        assertThat(this.table.get(point), is(this.getValue()));
        assertThat(this.table.put(point, this.getOtherValue()), is(this.getValue()));
        assertThat(this.table.remove(point), is(this.getOtherValue()));
        assertThat(this.table.get(point), is(this.getDefaultValue()));
    }
    
    // Get
    
    protected T getDefaultValue() {
        return this.defaultValue;
    }

    protected abstract T getValue();
    protected abstract T getOtherValue();

    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnGetWithNegativeX() {
        this.table.get(new Point(-1,0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnGetWithNegativeY() {
        this.table.get(new Point(0, -1));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnGetWithExcessiveX() {
        this.table.get(new Point(2, 0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnGetWithExcessiveY() {
        this.table.get(new Point(0,2));
    }

    // Put

    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnPutWithNegativeX() {
        this.table.put(new Point(-1,0), this.getValue());
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnPutWithNegativeY() {
        this.table.put(new Point(0, -1), this.getValue());
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnPutWithExcessiveX() {
        this.table.put(new Point(2, 0), this.getValue());
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnPutWithExcessiveY() {
        this.table.put(new Point(0,2), this.getValue());
    }

    // Remove
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnRemoveWithNegativeX() {
        this.table.remove(new Point(-1,0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnRemoveWithNegativeY() {
        this.table.remove(new Point(0, -1));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnRemoveWithExcessiveX() {
        this.table.remove(new Point(2, 0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testThrowOnRemoveWithExcessiveY() {
        this.table.remove(new Point(0,2));
    }
    
    @Test
    public void testEmptyClear() {
        this.newTable(0, 0).clear();
    }
    
    @Test
    public void testNaturalTableIteration() {
        this.table = this.newTable(2, 2);
        Tables.fill(this.table, this.listOfValues());
        TableIterator<T> iterator = new StrategyTableIterator<T>(this.table, NaturalTableIteration.getInstance());
        
        assertThat(iterator.next(), is(this.listOfValues()[0]));
        assertThat(iterator.getLocation(), is(new Point(0,0)));
        assertThat(iterator.hasNext(), is(true));
        
        assertThat(iterator.next(), is(this.listOfValues()[1]));
        assertThat(iterator.getLocation(), is(new Point(1,0)));
        assertThat(iterator.hasNext(), is(true));
        
        assertThat(iterator.next(), is(this.listOfValues()[2]));
        assertThat(iterator.getLocation(), is(new Point(0,1)));
        assertThat(iterator.hasNext(), is(true));
        
        assertThat(iterator.next(), is(this.listOfValues()[3]));
        assertThat(iterator.getLocation(), is(new Point(1,1)));
        assertThat(iterator.hasNext(), is(false));
    }
    
    @Test
    public void testFill() {
        this.table = this.newTable(2, 2);
        Tables.fill(this.table, this.getValue());
        for(T num : this.table) {
            assertThat(num, is(this.getValue()));
        }
    }

    @Test
    public void testClear() {
        Point point = new Point(0, 0);
        this.table.put(point, this.getValue());
        this.table.put(new Point(1, 0), this.getOtherValue());
        this.table.clear();
        assertThat(this.table.get(point), is(this.getDefaultValue()));
        assertThat(this.table.get(new Point(1, 0)), is(this.getDefaultValue()));
    }
    
    @Test
    public void testSubTable() {
        this.table = this.newTable(2,1);
        Table<T> subTable = this.table.subTable(new Point(1,0), new Dimension(1,1));
        assertThat(subTable.size(), is(1));
    }

    @Test
    public void testConvenienceSubTable() {
        this.table = this.newTable(2,1);
        Table<T> subTable = this.table.subTable(new Point(1,0));
        assertThat(subTable.size(), is(1));
    }
    
    @Test
    public void testSubTableGet() {
        this.table = this.newTable(4,1);
        Tables.fill(this.table, this.listOfValues());
        Table<T> subTable = this.table.subTable(new Point(2,0));
        assertThat(subTable.size(), is(2));
        assertThat(subTable.get(new Point(0,0)), is(this.listOfValues()[2]));
        assertThat(subTable.get(new Point(1,0)), is(this.listOfValues()[3]));
    }

    protected abstract T[] listOfValues();
    protected abstract T[] otherListOfValues();

    @Test
    public void testSubTablePut() {
        this.table = this.newTable(4,1);
        Tables.fill(this.table, this.listOfValues());
        Table<T> subTable = this.table.subTable(new Point(2,0));
        Tables.fill(subTable, this.otherListOfValues());
        assertThat(this.table.get(new Point(0,0)), is(this.listOfValues()[0]));
        assertThat(this.table.get(new Point(1,0)), is(this.listOfValues()[1]));
        assertThat(this.table.get(new Point(2,0)), is(this.otherListOfValues()[0]));
        assertThat(this.table.get(new Point(3,0)), is(this.otherListOfValues()[1]));
    }
    
    @Test
    public void testSubTableClear() {
        this.table = this.newTable(4,1);
        Tables.fill(this.table, this.listOfValues());
        Table<T> subTable = this.table.subTable(new Point(2,0));
        subTable.clear();
        assertThat(this.table.get(new Point(0,0)), is(this.listOfValues()[0]));
        assertThat(this.table.get(new Point(1,0)), is(this.listOfValues()[1]));
        assertThat(this.table.get(new Point(2,0)), is(this.getDefaultValue()));
        assertThat(this.table.get(new Point(3,0)), is(this.getDefaultValue()));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testSubTableInsidiousGet() {
        this.table = this.newTable(4,1);
        Table<T> subTable = this.table.subTable(new Point(2,0));
        subTable.remove(new Point(-1,0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testNegativeWidthSubTable() {
        this.table.subTable(new Point(-1, 0), new Dimension(1,1));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testNegativeHeightSubTable() {
        this.table.subTable(new Point(0, -1), new Dimension(1,1));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testExcessiveHeightSubTable() {
        this.table.subTable(new Point(0, 0), new Dimension(0,3));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testExcessiveWidthSubTable() {
        this.table.subTable(new Point(0, 0), new Dimension(3,0));
    }
}
