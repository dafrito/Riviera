package com.bluespot.collections.observable.tree;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.bluespot.collections.tree.Tree;

/**
 * A {@link TreeModel} based on a {@link Tree} and {@link TreeModelDispatcher}.
 * This TreeModel relies on a {@code Tree} for representation and a {@code
 * TreeModelDispatcher} for handling changes made to the tree.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            The type of the underlying tree.
 */
public final class ObservableTree<T> implements TreeModel {

	private final List<TreeModelListener> listeners = new CopyOnWriteArrayList<TreeModelListener>();

	private final Tree<T> root;

	public ObservableTree(final T value) {
		this.root = new Tree<T>(value);
	}

	public ObservableTree(final Tree<T> root) {
		this.root = root;
	}

	public Tree<?> getChild(final Object parent, final int index) {
		if (!(parent instanceof Tree<?>)) {
			throw new ClassCastException();
		}
		if (index < 0) {
			throw new IllegalArgumentException("Index cannot be less than 0");
		}
		final Tree<?> node = (Tree<?>) parent;
		return node.get(index);
	}

	public int getChildCount(final Object parent) {
		if (!(parent instanceof Tree<?>)) {
			return 0;
		}
		final Tree<?> node = (Tree<?>) parent;
		return node.size();
	}

	public int getIndexOfChild(final Object parent, final Object child) {
		if (!(child instanceof Tree<?>)) {
			throw new ClassCastException();
		}
		if (!(parent instanceof Tree<?>)) {
			return -1;
		}
		final Tree<?> node = (Tree<?>) parent;
		return node.indexOf(child);
	}

	public Tree<T> getRoot() {
		return this.root;
	}

	public void valueForPathChanged(final TreePath path, final Object newValue) {
		throw new UnsupportedOperationException();
	}

	public boolean isLeaf(final Object nodeObject) {
		final Tree<?> node = (Tree<?>) nodeObject;
		return !node.isRoot() && node.isEmpty();
	}

	public void addTreeModelListener(final TreeModelListener listener) {
		this.listeners.add(listener);
	}

	public void removeTreeModelListener(final TreeModelListener listener) {
		this.listeners.remove(listener);
	}

}
