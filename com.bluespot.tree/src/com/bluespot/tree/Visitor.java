package com.bluespot.tree;

import com.bluespot.tree.Tree;


public interface Visitor<T> {
    public void visitNode(Tree<T> group);
}
