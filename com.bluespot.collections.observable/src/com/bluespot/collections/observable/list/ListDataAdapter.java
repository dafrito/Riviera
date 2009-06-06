package com.bluespot.collections.observable.list;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A no-op implementation of a {@link ListDataListener}.
 * 
 * @author Aaron Faanes
 * 
 */
public class ListDataAdapter implements ListDataListener {

	public void contentsChanged(final ListDataEvent arg0) {
		// Do nothing
	}

	public void intervalAdded(final ListDataEvent arg0) {
		// Do nothing
	}

	public void intervalRemoved(final ListDataEvent arg0) {
		// Do nothing
	}

}
