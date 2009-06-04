package com.bluespot.collections.observable.list;

import javax.swing.ListModel;

public interface TypedListModel<T> extends ListModel {
	public T getElementAt(int index);
}
