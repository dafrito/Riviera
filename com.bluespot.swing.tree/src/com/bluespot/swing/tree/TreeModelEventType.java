package com.bluespot.swing.tree;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import com.bluespot.dispatcher.Dispatchable;

public enum TreeModelEventType implements Dispatchable<TreeModelEvent, TreeModelListener> {
	CHANGED_NODES {

		public void dispatch(TreeModelEvent e, TreeModelListener listener) {
			listener.treeNodesChanged(e);
		}

	},
	INSERTED_NODES {

		public void dispatch(TreeModelEvent e, TreeModelListener listener) {
			listener.treeNodesInserted(e);
		}
	},
	REMOVED_NODES {

		public void dispatch(TreeModelEvent e, TreeModelListener listener) {
			listener.treeNodesRemoved(e);
		}
	},
	CHANGED_STRUCTURE {

		public void dispatch(TreeModelEvent e, TreeModelListener listener) {
			listener.treeStructureChanged(e);
		}
	};
}
