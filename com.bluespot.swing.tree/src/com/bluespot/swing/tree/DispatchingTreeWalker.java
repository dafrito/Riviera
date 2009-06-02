package com.bluespot.swing.tree;

import javax.swing.tree.TreePath;

import com.bluespot.tree.Tree;
import com.bluespot.tree.TreeWalker;
import com.bluespot.tree.Trees;
import com.bluespot.tree.WalkerFactory;

/**
 * A TreeWalker that dispatches events.
 * <p>
 * This bridges a TreeWalker with a TreeModelDispatcher, allowing you to attach
 * listeners to this walker. Events will be emitted whenever this walker changes
 * the tree. This means that if the tree is changed externally, this walker
 * won't notice and won't emit events.
 * <p>
 * This class is designed to be used with ProxiedTreeModel, since that class
 * creates a TreeModelDispatcher that we can use here.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            Type of the tree we're walking.
 */
public class DispatchingTreeWalker<T> extends TreeWalker<T> {

	private final TreeModelDispatcher dispatcher;

	public DispatchingTreeWalker(final ProxiedTreeModel<T> model) {
		this(model.getDispatcher(), model.getRoot());
	}

	public DispatchingTreeWalker(final TreeModelDispatcher dispatcher, final Tree<T> root) {
		super(root);
		this.dispatcher = dispatcher;
	}

	@Override
	public Tree<T> append(final T value) {
		final TreePath pathToAppendedParent = Trees.asTreePath(this.getCurrentNode());
		final Tree<T> addedNode = super.append(value);
		this.dispatcher.fireTreeNodeInserted(pathToAppendedParent, addedNode);
		return addedNode;
	}

	public TreeModelDispatcher getDispatcher() {
		return this.dispatcher;
	}

	public static <U> WalkerFactory<U> createFactory(final TreeModelDispatcher dispatcher) {
		return new WalkerFactory<U>() {

			public TreeWalker<U> newInstance(final Tree<U> tree) {
				return new DispatchingTreeWalker<U>(dispatcher, tree);
			}

		};
	}

}
