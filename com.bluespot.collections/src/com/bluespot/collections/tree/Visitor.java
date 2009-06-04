package com.bluespot.collections.tree;

public interface Visitor<T> {
	public void visitNode(Tree<T> group);
}
