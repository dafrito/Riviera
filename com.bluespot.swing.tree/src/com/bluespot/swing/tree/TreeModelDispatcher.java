package com.bluespot.swing.tree;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.bluespot.dispatcher.StatefulDispatcher;

public class TreeModelDispatcher extends StatefulDispatcher<TreeModelEvent, TreeModelListener> {

	private final TreeModel model;

	public TreeModelDispatcher(final TreeModel model) {
		this.model = model;
	}

	public void fireTreeNodeChanged(final TreePath path, final Object child) {
		this.fireTreeNodesChanged(path, new Object[] { child });
	}

	public void fireTreeNodeInserted(final TreePath path, final Object child) {
		this.fireTreeNodesInserted(path, new Object[] { child });
	}

	public void fireTreeNodeRemoved(final TreePath path, final int childIndex, final Object child) {
		this.fireTreeNodesRemoved(path, new int[] { childIndex }, new Object[] { child });
	}

	public void fireTreeNodesChanged(final TreePath path, final Object[] children) {
		final TreeModelEvent event = new TreeModelEvent(this.model, path, this.getIndices(path, children), children);
		this.dispatch(TreeModelEventType.CHANGED_NODES, event);
	}

	public void fireTreeNodesInserted(final TreePath path, final Object[] children) {
		final TreeModelEvent event = new TreeModelEvent(this.model, path, this.getIndices(path, children), children);
		this.dispatch(TreeModelEventType.INSERTED_NODES, event);
	}

	public void fireTreeNodesRemoved(final TreePath path, final int[] childIndices, final Object[] children) {
		this.dispatch(TreeModelEventType.REMOVED_NODES, new TreeModelEvent(this.model, path, childIndices, children));
	}

	public void fireTreeStructureChanged(final TreePath path) {
		this.dispatch(TreeModelEventType.CHANGED_STRUCTURE, new TreeModelEvent(this.model, path));
	}

	private int[] getIndices(final TreePath path, final Object[] children) {
		final Object parent = path.getLastPathComponent();
		final int[] childIndices = new int[children.length];
		for (int i = 0; i < children.length; i++) {
			final Object child = children[i];
			final int childIndex = this.model.getIndexOfChild(parent, child);
			if (childIndex < 0) {
				throw new IllegalArgumentException("Child was not found in the tree!");
			}
			childIndices[i] = childIndex;
		}
		return childIndices;
	}
}
