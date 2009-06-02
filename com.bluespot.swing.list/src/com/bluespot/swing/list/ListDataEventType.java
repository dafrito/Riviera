package com.bluespot.swing.list;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.bluespot.dispatcher.Dispatchable;

public enum ListDataEventType implements Dispatchable<ListDataEvent, ListDataListener> {
	CONTENTS_CHANGED(ListDataEvent.CONTENTS_CHANGED) {

		public void dispatch(ListDataEvent event, ListDataListener listener) {
			listener.contentsChanged(event);
		}

	},
	INTERVAL_ADDED(ListDataEvent.INTERVAL_ADDED) {

		public void dispatch(ListDataEvent event, ListDataListener listener) {
			listener.intervalAdded(event);
		}

	},
	INTERVAL_REMOVED(ListDataEvent.INTERVAL_REMOVED) {

		public void dispatch(ListDataEvent event, ListDataListener listener) {
			listener.intervalRemoved(event);
		}
	};

	private final int swingEventType;

	ListDataEventType(int swingEventType) {
		this.swingEventType = swingEventType;
	}

	public ListDataEvent newEvent(ListModel model, int startIndex, int endIndex) {
		return new ListDataEvent(model, this.swingEventType, startIndex, endIndex);
	}
}
