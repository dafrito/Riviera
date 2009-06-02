package com.bluespot.tree;

public interface Visitor<T> {
	public void visitNode(Tree<T> group);
}
