package com.bluespot.collections.observable.list;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bluespot.collections.list.AbstractListTest;

public final class ObservableListTest extends AbstractListTest {

    public ObservableList<String> listModel;

    protected final Queue<ListDataEvent> eventList = new ArrayDeque<ListDataEvent>();

    @Before
    public void initListModel() {
        this.listModel = this.newListModel();
        this.listModel.addListDataListener(new ListDataAdapter() {

            @Override
            public void contentsChanged(final ListDataEvent e) {
                ObservableListTest.this.eventList.add(e);
            }

            @Override
            public void intervalAdded(final ListDataEvent e) {
                ObservableListTest.this.eventList.add(e);
            }

            @Override
            public void intervalRemoved(final ListDataEvent e) {
                ObservableListTest.this.eventList.add(e);
            }

        });

    }

    /**
     * AddAll(collection) testing
     */
    @Test
    public void testAddAll() {
        this.listModel.addAll(this.prepopulatedList);
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_ADDED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(0));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(this.prepopulatedList.size() - 1));
    }

    /**
     * AddAllAt(index, collection) testing
     */
    @Test
    public void testAddAllAt() {
        this.prepopulate();
        this.listModel.addAll(1, this.prepopulatedList);
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_ADDED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(1));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(1 + (this.prepopulatedList.size() - 1)));
    }

    /**
     * Add(index, element) testing
     */
    @Test
    public void testAddAt() {
        this.prepopulate();
        this.listModel.add(0, "The craziest value.");
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_ADDED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(0));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(0));
    }

    /**
     * Clear() testing
     */
    @Test
    public void testClear() {
        this.prepopulate();
        this.listModel.clear();
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_REMOVED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(0));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(this.prepopulatedList.size() - 1));
    }

    /**
     * Set(index, element) testing
     */
    @Test
    public void testContentsChanged() {
        this.prepopulate();
        this.listModel.set(0, "The craziest value.");
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.CONTENTS_CHANGED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(0));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(0));
    }

    /**
     * Add(element) testing
     */
    @Test
    public void testIntervalAdded() {
        this.listModel.add("A");
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_ADDED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(0));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(0));
    }

    @Test
    public void testListenerSanity() {
        final ListDataListener listener = new ListDataAdapter();
        this.listModel.addListDataListener(listener);
        this.listModel.removeListDataListener(listener);
    }

    @Test(expected = NullPointerException.class)
    public void testNPEOnNullCollectionCtor() {
        new ObservableList<String>(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNPEOnNullListener() {
        this.listModel.addListDataListener(null);
    }

    /**
     * Remove(object) testing
     */
    @Test
    public void testRemove() {
        this.prepopulate();
        assertThat(this.listModel.remove("B"), is(true));
        assertThat(this.listModel.remove("Some absent value"), is(false));

        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_REMOVED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(1));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(1));
    }

    /**
     * RemoveAll(collection) testing
     */
    @Test
    public void testRemoveAllInCollection() {
        this.prepopulate();
        // SubList is B, C, and E
        this.listModel.removeAll(this.subsetList);
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.CONTENTS_CHANGED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(1));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(4));
    }

    /**
     * Remove(index) testing
     */
    @Test
    public void testRemoveIndex() {
        this.prepopulate();
        this.listModel.remove(0);
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_REMOVED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(0));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(0));
    }

    /**
     * RetainAll(collection) testing
     */
    @Test
    public void testRetainAllInCollection() {
        this.prepopulate();
        // SubList is B, C, and E .
        // Retaining will yield only these elements.
        this.listModel.retainAll(this.subsetList);
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.CONTENTS_CHANGED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(0));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(3));
    }

    @Test
    public void testSilentOnNullListenerRemoved() {
        this.listModel.removeListDataListener(null);
    }

    @Test
    public void testSimpleListModel() {
        this.listModel.add("A");
        Assert.assertEquals(this.listModel.getElementAt(0), "A");
        Assert.assertEquals(this.listModel.getSize(), 1);
        this.listModel.clear();
        Assert.assertEquals(this.listModel.getSize(), 0);
    }

    /**
     * Sublist(first, last) - AddAll testing
     */
    @Test
    public void testSublistAddAll() {
        this.prepopulate();
        final List<String> sublist = this.listModel.subList(0, 2);
        sublist.addAll(Arrays.asList("G", "H", "I"));
        Assert.assertThat(this.listModel.getSize(), CoreMatchers.is(8));
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_ADDED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(2));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(4));
    }

    /**
     * Sublist(first, last) - Clear testing
     */
    @Test
    public void testSublistClear() {
        this.prepopulate();
        this.listModel.subList(0, 2).clear();
        Assert.assertThat(this.listModel.getSize(), CoreMatchers.is(3));
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.INTERVAL_REMOVED));
        Assert.assertThat(event.getIndex0(), CoreMatchers.is(0));
        Assert.assertThat(event.getIndex1(), CoreMatchers.is(1));
    }

    /**
     * Sublist(first, last) - AddAll testing
     */
    @Test
    public void testSublistSet() {
        this.prepopulate();
        final List<String> sublist = this.listModel.subList(2, 4);
        sublist.set(1, "No time");
        Assert.assertThat("Event-list has a mismatched size.", this.eventList.size(), CoreMatchers.is(1));
        final ListDataEvent event = this.eventList.remove();
        Assert.assertThat(event.getType(), CoreMatchers.is(ListDataEvent.CONTENTS_CHANGED));
        Assert.assertThat(this.listModel.get(event.getIndex0()), CoreMatchers.is("No time"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testThrowsOnBadGetElement() {
        this.listModel.getElementAt(1);
    }

    private void prepopulate() {
        this.listModel.addAll(this.prepopulatedList);
        this.eventList.clear();
    }

    @Override
    protected List<String> newList() {
        return this.newListModel();
    }

    protected ObservableList<String> newListModel() {
        return new ObservableList<String>();
    }

}
