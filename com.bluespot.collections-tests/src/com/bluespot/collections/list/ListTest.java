package com.bluespot.collections.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class ListTest {

    protected List<String> list;

    protected final String otherString = "The second!";

    protected List<String> prepopulatedList;
    protected List<String> reference;

    protected List<String> subsetList;
    protected final String testString = "The first!";

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
    public void initReference() {
        this.reference = new ArrayList<String>();
    }

    @Before
    public void initSubsetList() {
        this.subsetList = new ArrayList<String>();
        Collections.addAll(this.subsetList, "B", "C", "E");
        this.subsetList = Collections.unmodifiableList(this.subsetList);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadGetThrows() {
        this.list.get(0);
    }

    @Test
    public void testCommunativeEquality() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        Assert.assertTrue(this.reference.equals(this.list));
        Assert.assertTrue(this.list.equals(this.reference));
        this.reference.clear();
        Assert.assertTrue(!this.list.equals(this.reference));
        Assert.assertTrue(!this.reference.equals(this.list));
    }

    @Test
    public void testContainsAll() {
        this.list.addAll(this.prepopulatedList);
        this.reference.add("Nonexistent value!");
        Assert.assertEquals(this.list.containsAll(this.prepopulatedList), true);
        Assert.assertEquals(this.list.containsAll(this.reference), false);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(this.list.isEmpty());
        this.list.add("A");
        Assert.assertEquals(this.list.isEmpty(), false);
    }

    @Test
    public void testIterator() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        final Iterator<String> testIterator = this.list.iterator();
        final Iterator<String> referenceIterator = this.reference.iterator();
        while (testIterator.hasNext() && referenceIterator.hasNext()) {
            final String testValue = testIterator.next();
            final String referenceValue = referenceIterator.next();
            Assert.assertEquals(testValue, referenceValue);
        }
        Assert.assertThat(testIterator.hasNext(), CoreMatchers.is(false));
        Assert.assertThat(referenceIterator.hasNext(), CoreMatchers.is(false));
        testIterator.remove();
        referenceIterator.remove();
        this.testSanity();
    }

    @Test
    public void testLastIndexOf() {
        Collections.addAll(this.list, "A", "A", "A");
        Assert.assertEquals(this.list.lastIndexOf("A"), 2);
        Assert.assertEquals(this.list.lastIndexOf("B"), -1);
    }

    @Test
    public void testQueryMethods() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        Assert.assertEquals(this.list.contains("A"), true);
        Assert.assertEquals(this.list.contains("Nonexistent value!"), false);
        Assert.assertEquals(this.list.get(0), "A");
        Assert.assertEquals(this.list.get(1), "B");
        Assert.assertEquals(this.list.indexOf("A"), 0);
        Assert.assertEquals(this.list.indexOf("A"), 0);
    }

    public void testSanity() {
        Assert.assertArrayEquals(this.reference.toArray(), this.list.toArray());
    }

    @Test
    public void testSet() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        this.list.set(0, "NEW!");
        this.reference.set(0, "NEW!");
        this.testSanity();
    }

    @Test
    public void testSomeAdditions() {
        Assert.assertThat(this.list.size(), CoreMatchers.is(0));
        this.list.add(this.testString);
        this.reference.add(this.testString);
        Assert.assertThat(this.list.size(), CoreMatchers.is(1));
        Assert.assertThat(this.list.get(0), CoreMatchers.is(this.testString));
        this.testSanity();
        this.list.add(0, this.otherString);
        this.reference.add(0, this.otherString);
        this.testSanity();
        this.list.clear();
        this.reference.clear();
        this.testSanity();
        Assert.assertThat(this.list.size(), CoreMatchers.is(0));
        this.reference.add(this.testString);
        this.reference.add(this.otherString);
        this.list.addAll(this.reference);
        this.testSanity();
    }

    @Test
    public void testSomeRemovals() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        this.testSanity();
        this.list.remove(0);
        this.reference.remove(0);
        this.testSanity();
        this.list.remove("C");
        this.reference.remove("C");
        this.testSanity();
        this.list.removeAll(this.subsetList);
        this.reference.removeAll(this.subsetList);
        this.testSanity();
        this.list.clear();
        this.reference.clear();
        this.testSanity();
        Assert.assertTrue(this.list.isEmpty());
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        this.list.retainAll(this.subsetList);
        this.reference.retainAll(this.subsetList);
        this.testSanity();
    }

    @Test
    public void testSubList() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        Assert.assertTrue(this.list.subList(2, 4).equals(this.reference.subList(2, 4)));
    }

    @Test
    public void testToArray() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        final Object[] testArray = this.list.toArray();
        final Object[] referenceArray = this.reference.toArray();
        Assert.assertArrayEquals(testArray, referenceArray);
    }

    @Test
    public void testToArrayWithSpecifiedArray() {
        this.list.addAll(this.prepopulatedList);
        this.reference.addAll(this.prepopulatedList);
        final Object[] testArray = new Object[10];
        final Object[] referenceArray = new Object[10];
        this.list.toArray(testArray);
        this.reference.toArray(referenceArray);
        Assert.assertArrayEquals(testArray, referenceArray);
    }

    protected abstract List<String> newList();
}
