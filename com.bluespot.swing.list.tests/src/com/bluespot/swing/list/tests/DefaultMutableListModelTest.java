package com.bluespot.swing.list.tests;

import org.junit.Ignore;

import com.bluespot.swing.list.DefaultMutableListModel;
import com.bluespot.swing.list.MutableListModel;

@Ignore
public class DefaultMutableListModelTest extends MutableListModelTest {

	@Override
	protected MutableListModel<String> newListModel() {
		return new DefaultMutableListModel<String>();
	}
}