package com.bluespot.swing.list.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.swing.list.ListDataAdapter;
import com.bluespot.swing.list.MutableListModel;

public abstract class MutableListModelTest extends ListTest {

    public MutableListModel<String> listModel;
    
    @Override
    protected List<String> newList() {
        return this.newListModel();
    }

    protected abstract MutableListModel<String> newListModel();

    protected final Queue<ListDataEvent> eventList = new ArrayDeque<ListDataEvent>();

    @Before
    public void initListModel() {
        this.listModel = this.newListModel();
        this.listModel.addListDataListener(new ListDataAdapter() {

            @Override
            public void contentsChanged(ListDataEvent e) {
                MutableListModelTest.this.eventList.add(e);
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                MutableListModelTest.this.eventList.add(e);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                MutableListModelTest.this.eventList.add(e);
            }

        });

    }

    private void prepopulate() {
        this.listModel.addAll(this.prepopulatedList);
        this.eventList.clear();
    }

    @Test
    public void testSimpleListModel() {
        this.listModel.add("A");
        assertEquals(this.listModel.getElementAt(0), "A");
        assertEquals(this.listModel.getSize(), 1);
        this.listModel.clear();
        assertEquals(this.listModel.getSize(), 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testThrowsOnBadGetElement() {
        this.listModel.getElementAt(1);
    }

    @Test
    public void testListenerSanity() {
        ListDataListener listener = new ListDataAdapter();
        this.listModel.addListDataListener(listener);
        this.listModel.removeListDataListener(listener);
    }

    /**
     * Add(index, element) testing
     */
    @Test
    public void testAddAt() {
        this.prepopulate();
        this.listModel.add(0, "The craziest value.");
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_ADDED));
        assertThat(event.getIndex0(), is(0));
        assertThat(event.getIndex1(), is(0));
    }

    /**
     * AddAll(collection) testing
     */
    @Test
    public void testAddAll() {
        this.listModel.addAll(this.prepopulatedList);
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_ADDED));
        assertThat(event.getIndex0(), is(0));
        assertThat(event.getIndex1(), is(this.prepopulatedList.size() - 1));
    }

    /**
     * AddAllAt(index, collection) testing
     */
    @Test
    public void testAddAllAt() {
        this.prepopulate();
        this.listModel.addAll(1, this.prepopulatedList);
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_ADDED));
        assertThat(event.getIndex0(), is(1));
        assertThat(event.getIndex1(), is(1 + (this.prepopulatedList.size() - 1)));
    }

    /**
     * Add(element) testing
     */
    @Test
    public void testIntervalAdded() {
        this.listModel.add("A");
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_ADDED));
        assertThat(event.getIndex0(), is(0));
        assertThat(event.getIndex1(), is(0));
    }

    /**
     * Clear() testing
     */
    @Test
    public void testClear() {
        this.prepopulate();
        this.listModel.clear();
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_REMOVED));
        assertThat(event.getIndex0(), is(0));
        assertThat(event.getIndex1(), is(this.prepopulatedList.size() - 1));
    }

    /**
     * Remove(object) testing
     */
    @Test
    public void testRemove() {
        this.prepopulate();
        this.listModel.remove("B");
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_REMOVED));
        assertThat(event.getIndex0(), is(1));
        assertThat(event.getIndex1(), is(1));
    }

    /**
     * RemoveAll(collection) testing
     */
    @Test
    public void testRemoveAllInCollection() {
        this.prepopulate();
        // SubList is B, C, and E
        this.listModel.removeAll(this.subsetList);
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.CONTENTS_CHANGED));
        assertThat(event.getIndex0(), is(1));
        assertThat(event.getIndex1(), is(4));
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
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.CONTENTS_CHANGED));
        assertThat(event.getIndex0(), is(0));
        assertThat(event.getIndex1(), is(3));
    }

    /**
     * Set(index, element) testing
     */
    @Test
    public void testContentsChanged() {
        this.prepopulate();
        this.listModel.set(0, "The craziest value.");
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.CONTENTS_CHANGED));
        assertThat(event.getIndex0(), is(0));
        assertThat(event.getIndex1(), is(0));
    }

    /**
     * Sublist(first, last) - Clear testing
     */
    @Test
    public void testSublistClear() {
        this.prepopulate();
        this.listModel.subList(0, 2).clear();
        assertThat(this.listModel.getSize(), is(3));
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_REMOVED));
        assertThat(event.getIndex0(), is(0));
        assertThat(event.getIndex1(), is(1));
    }

    /**
     * Sublist(first, last) - AddAll testing
     */
    @Test
    public void testSublistAddAll() {
        this.prepopulate();
        List<String> sublist = this.listModel.subList(0, 2);
        sublist.addAll(Arrays.asList("G", "H", "I"));
        assertThat(this.listModel.getSize(), is(8));
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_ADDED));
        assertThat(event.getIndex0(), is(2));
        assertThat(event.getIndex1(), is(4));
    }

    /**
     * Sublist(first, last) - AddAll testing
     */
    @Test
    public void testSublistSet() {
        this.prepopulate();
        List<String> sublist = this.listModel.subList(2, 4);
        sublist.set(1, "No time");
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.CONTENTS_CHANGED));
        assertThat(this.listModel.get(event.getIndex0()), is("No time"));
    }

    /**
     * Remove(index) testing
     */
    @Test
    public void testRemoveIndex() {
        this.prepopulate();
        this.listModel.remove(0);
        assertThat("Event-list has a mismatched size.", this.eventList.size(), is(1));
        ListDataEvent event = this.eventList.remove();
        assertThat(event.getType(), is(ListDataEvent.INTERVAL_REMOVED));
        assertThat(event.getIndex0(), is(0));
        assertThat(event.getIndex1(), is(0));
    }

}
