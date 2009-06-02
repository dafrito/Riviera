package com.bluespot.tree;

import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.tree.TreePath;

public final class Trees {

	private Trees() {
		throw new UnsupportedOperationException("Instantiation not allowed");
	}

	public static TreePath asTreePath(final Tree<?> toNode) {
		final Deque<Tree<?>> path = new ArrayDeque<Tree<?>>();
		Tree<?> node = toNode;
		while (node != null) {
			path.addFirst(node);
			node = node.getParent();
		}
		return new TreePath(path.toArray());
	}
}
