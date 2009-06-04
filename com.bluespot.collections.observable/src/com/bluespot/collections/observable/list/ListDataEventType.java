package com.bluespot.collections.observable.list;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.bluespot.dispatcher.Dispatchable;

public enum ListDataEventType implements Dispatchable<ListDataEvent, ListDataListener> {
	CONTENTS_CHANGED(ListDataEvent.CONTENTS_CHANGED) {

		public void dispatch(final ListDataEvent event, final ListDataListener listener) {
			listener.contentsChanged(event);
		}

	},
	INTERVAL_ADDED(ListDataEvent.INTERVAL_ADDED) {

		public void dispatch(final ListDataEvent event, final ListDataListener listener) {
			listener.intervalAdded(event);
		}

	},
	INTERVAL_REMOVED(ListDataEvent.INTERVAL_REMOVED) {

		public void dispatch(final ListDataEvent event, final ListDataListener listener) {
			listener.intervalRemoved(event);
		}
	};

	private final int swingEventType;

	ListDataEventType(final int swingEventType) {
		this.swingEventType = swingEventType;
	}

	public ListDataEvent newEvent(final ListModel model, final int startIndex, final int endIndex) {
		return new ListDataEvent(model, this.swingEventType, startIndex, endIndex);
	}
}
