package com.bluespot.swing.list.tests;

import com.bluespot.swing.list.MutableListModel;
import com.bluespot.swing.list.ProxiedListModel;

public class ProxiedListModelTest extends MutableListModelTest {

	@Override
	protected MutableListModel<String> newListModel() {
		return new ProxiedListModel<String>();
	}
}
