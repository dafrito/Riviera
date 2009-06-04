package com.bluespot.collections.tree;

public interface WalkerFactory<T> {
	public TreeWalker<T> newInstance(Tree<T> tree);
}
