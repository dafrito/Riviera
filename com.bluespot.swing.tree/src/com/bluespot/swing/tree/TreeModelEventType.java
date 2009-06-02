package com.bluespot.swing.tree;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import com.bluespot.dispatcher.Dispatchable;

public enum TreeModelEventType implements Dispatchable<TreeModelEvent, TreeModelListener> {
	CHANGED_NODES {

		public void dispatch(final TreeModelEvent e, final TreeModelListener listener) {
			listener.treeNodesChanged(e);
		}

	},
	CHANGED_STRUCTURE {

		public void dispatch(final TreeModelEvent e, final TreeModelListener listener) {
			listener.treeStructureChanged(e);
		}
	},
	INSERTED_NODES {

		public void dispatch(final TreeModelEvent e, final TreeModelListener listener) {
			listener.treeNodesInserted(e);
		}
	},
	REMOVED_NODES {

		public void dispatch(final TreeModelEvent e, final TreeModelListener listener) {
			listener.treeNodesRemoved(e);
		}
	};
}
