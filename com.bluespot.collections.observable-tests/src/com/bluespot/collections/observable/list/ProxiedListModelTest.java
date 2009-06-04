package com.bluespot.collections.observable.list;

import com.bluespot.collections.observable.list.MutableListModel;
import com.bluespot.collections.observable.list.ProxiedListModel;

public class ProxiedListModelTest extends MutableListModelTest {

	@Override
	protected MutableListModel<String> newListModel() {
		return new ProxiedListModel<String>();
	}
}
