package com.bluespot.collections.tree;

/**
 * Walks a tree, allowing positional insertion.
 * <p>
 * This allows insertion at an arbitrary node. You may enter and leave nodes to
 * navigate the tree. The walker has no concept of the tree as a whole, so you
 * can spin off walkers at any point.
 * <p>
 * Currently, this class doesn't really support iteration; you'd have to do it
 * essentially through the tree. I'll probably end up merging this class with
 * Visitor, since it seems like a walker should be able to iterate.
 * <p>
 * I won't do it yet since I'm not sure how to do it without bloating the
 * interface.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            The type of the tree we're walking
 */
public class TreeWalker<T> {

	private Tree<T> currentNode;

	public TreeWalker(final Tree<T> node) {
		this.currentNode = node;
	}

	public Tree<T> append(final T value) {
		return this.currentNode.append(value);
	}

	public Tree<T> appendAndEnter(final T value) {
		return this.enter(this.append(value));
	}

	public Tree<T> appendAndLeave(final T value) {
		this.append(value);
		return this.leave();
	}

	public Tree<T> enter(final Tree<T> node) {
		if (node.getParent() != this.currentNode) {
			throw new IllegalArgumentException("This node is not a child of the currentNode");
		}
		this.currentNode = node;
		return this.currentNode;
	}

	public Tree<T> value() {
		return this.currentNode;
	}

	public Tree<T> leave() {
		if (this.currentNode.isRoot()) {
			throw new IllegalStateException("Cannot close root group!");
		}
		this.currentNode = this.currentNode.getParent();
		return this.currentNode;
	}

	public static <T> WalkerFactory<T> createFactory() {
		return new WalkerFactory<T>() {

			public TreeWalker<T> newInstance(final Tree<T> tree) {
				return new TreeWalker<T>(tree);
			}

		};
	}
}
