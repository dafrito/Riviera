package com.bluespot.swing.list.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public abstract class ListTest {

    protected List<String> reference;

    protected List<String> list;
    
    protected final String testString = "The first!";
    protected final String otherString = "The second!";
    
    protected List<String> prepopulatedList;
    protected List<String> subsetList;
    
    protected abstract List<String> newList();
    
    @Before 
    public void initList() {
        this.list = this.newList();
    }

    @Before
    public void initPrepopulatedList() {
        this.prepopulatedList = new ArrayList<String>();
        Collections.addAll(this.prepopulatedList, "A", "B", "C", "D", "E");
        this.prepopulatedList = Collections.unmodifiableList(this.prepopulatedList);
    }
    
    @Before
    public void initSubsetList() {
        this.subsetList = new ArrayList<String>();
        Collections.addAll(this.subsetList, "B", "C", "E");
        this.subsetList = Collections.unmodifiableList(this.subsetList);
    }
    
    @Before
    public void initReference() {
        this.reference = new ArrayList<String>();
    }
    
    public void testSanity() {
        assertArrayEquals(this.reference.toArray(), this.list.toArray());
    }

    @Test
    public void testSomeAdditions() {
        assertThat(this.list.size(), is(0));
        this.list.add(this.testString);
        this.reference.add(this.testString);
        assertThat(this.list.size(), is(1));
        assertThat(this.list.get(0), is(this.testString));
        this.testSanity();
        this.list.add(0, this.otherString);
        this.reference.add(0, this.otherString);
        this.testSanity();
        this.list.clear();
        this.reference.clear();
        this.testSanity();
        assertThat(this.list.size(), is(0));
        this.reference.add(this.testString);
        this.reference.add(this.otherString);
        this.list.addAll(this.reference);
        this.testSanity();
    }

    @Test
    public void testSomeRemovals() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        testSanity();
        this.list.remove(0);
        this.reference.remove(0);
        testSanity();
        this.list.remove("C");
        this.reference.remove("C");
        testSanity();
        this.list.removeAll(this.subsetList);
        this.reference.removeAll(this.subsetList);
        testSanity();
        this.list.clear();
        this.reference.clear();
        testSanity();
        assertTrue(this.list.isEmpty());
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        this.list.retainAll(this.subsetList);
        this.reference.retainAll(this.subsetList);
        testSanity();
    }
    
    @Test
    public void testSubList() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        assertTrue(this.list.subList(2, 4).equals(this.reference.subList(2, 4)));
    }

    @Test
    public void testCommunativeEquality() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        assertTrue(this.reference.equals(this.list));
        assertTrue(this.list.equals(this.reference));
        this.reference.clear();
        assertTrue(!this.list.equals(this.reference));
        assertTrue(!this.reference.equals(this.list));
    }

    @Test
    public void testQueryMethods() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        assertEquals(this.list.contains("A"), true);
        assertEquals(this.list.contains("Nonexistent value!"), false);
        assertEquals(this.list.get(0), "A");
        assertEquals(this.list.get(1), "B");
        assertEquals(this.list.indexOf("A"), 0);
        assertEquals(this.list.indexOf("A"), 0);
    }
    
    @Test
    public void testContainsAll() {
        this.list.addAll(this.prepopulatedList);
        this.reference.add("Nonexistent value!");
        assertEquals(this.list.containsAll(this.prepopulatedList), true);
        assertEquals(this.list.containsAll(this.reference), false);
    }

    @Test
    public void testLastIndexOf() {
        Collections.addAll(this.list, "A", "A", "A");
        assertEquals(this.list.lastIndexOf("A"), 2);
        assertEquals(this.list.lastIndexOf("B"), -1);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(this.list.isEmpty());
        this.list.add("A");
        assertEquals(this.list.isEmpty(), false);
    }
    
    @Test
    public void testToArray() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        Object[] testArray = this.list.toArray();
        Object[] referenceArray = this.reference.toArray();
        assertArrayEquals(testArray, referenceArray);
    }
    
    @Test
    public void testToArrayWithSpecifiedArray() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        Object[] testArray = new Object[10];
        Object[] referenceArray = new Object[10];
        this.list.toArray(testArray);
        this.reference.toArray(referenceArray);
        assertArrayEquals(testArray, referenceArray);
    }
    
    @Test
    public void testSet() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        this.list.set(0, "NEW!");
        this.reference.set(0, "NEW!");
        testSanity();
    }
    
    @Test
    public void testIterator() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        Iterator<String> testIterator = this.list.iterator();
        Iterator<String> referenceIterator = this.reference.iterator();
        while(testIterator.hasNext() && referenceIterator.hasNext()) {
            String testValue = testIterator.next();
            String referenceValue = referenceIterator.next();
            assertEquals(testValue, referenceValue);
        }
        assertThat(testIterator.hasNext(), is(false));
        assertThat(referenceIterator.hasNext(), is(false));
        testIterator.remove();
        referenceIterator.remove();
        testSanity();
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testBadGetThrows() {
        this.list.get(0);
    }
}
