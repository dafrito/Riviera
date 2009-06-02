package com.bluespot.swing.list.tests;

import org.junit.Assert;
import org.junit.Test;

import com.bluespot.swing.list.MutableListModel;
import com.bluespot.swing.list.ProxiedListModel;

public class ProxiedListModelTest extends MutableListModelTest {

	@Test
	public void testSomeCustomStuff() {
		Assert.assertTrue(true);
	}

	@Override
	protected MutableListModel<String> newListModel() {
		return new ProxiedListModel<String>();
	}
}
