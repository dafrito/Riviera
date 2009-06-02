package com.bluespot.swing.list.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bluespot.swing.list.MutableListModel;
import com.bluespot.swing.list.ProxiedListModel;


public class ProxiedListModelTest extends MutableListModelTest {

    @Test
    public void testSomeCustomStuff() {
        assertTrue(true);
    }

    @Override
    protected MutableListModel<String> newListModel() {
        return new ProxiedListModel<String>();
    }
}
