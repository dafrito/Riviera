package com.bluespot.swing.tree;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.bluespot.dispatcher.StatefulDispatcher;

public class TreeModelDispatcher extends StatefulDispatcher<TreeModelEvent, TreeModelListener> {

	private final TreeModel model;

	public TreeModelDispatcher(TreeModel model) {
		this.model = model;
	}

	private int[] getIndices(TreePath path, Object[] children) {
		Object parent = path.getLastPathComponent();
		int[] childIndices = new int[children.length];
		for (int i = 0; i < children.length; i++) {
			Object child = children[i];
			int childIndex = this.model.getIndexOfChild(parent, child);
			if (childIndex < 0) {
				throw new IllegalArgumentException("Child was not found in the tree!");
			}
			childIndices[i] = childIndex;
		}
		return childIndices;
	}

	public void fireTreeNodesChanged(TreePath path, Object[] children) {
		TreeModelEvent event = new TreeModelEvent(this.model, path, this.getIndices(path, children), children);
		this.dispatch(TreeModelEventType.CHANGED_NODES, event);
	}

	public void fireTreeNodesInserted(TreePath path, Object[] children) {
		TreeModelEvent event = new TreeModelEvent(this.model, path, this.getIndices(path, children), children);
		this.dispatch(TreeModelEventType.INSERTED_NODES, event);
	}

	public void fireTreeNodesRemoved(TreePath path, int[] childIndices, Object[] children) {
		this.dispatch(TreeModelEventType.REMOVED_NODES, new TreeModelEvent(this.model, path, childIndices, children));
	}

	public void fireTreeStructureChanged(TreePath path) {
		this.dispatch(TreeModelEventType.CHANGED_STRUCTURE, new TreeModelEvent(this.model, path));
	}

	public void fireTreeNodeInserted(TreePath path, Object child) {
		this.fireTreeNodesInserted(path, new Object[] { child });
	}

	public void fireTreeNodeChanged(TreePath path, Object child) {
		this.fireTreeNodesChanged(path, new Object[] { child });
	}

	public void fireTreeNodeRemoved(TreePath path, int childIndex, Object child) {
		this.fireTreeNodesRemoved(path, new int[] { childIndex }, new Object[] { child });
	}
}
