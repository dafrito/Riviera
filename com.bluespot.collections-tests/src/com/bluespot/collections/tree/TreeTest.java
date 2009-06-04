package com.bluespot.collections.tree;

import org.junit.Test;

import com.bluespot.collections.tree.PrintVisitor;
import com.bluespot.collections.tree.Tree;

public class TreeTest {

	@Test
	public void testSimpleTree() {
		final Tree<String> group = new Tree<String>(null, "Root");
		group.append("First child");
		group.append("Second child");
		group.append("Child group").append("Inside the nested group");
		group.append("Third child");

		group.visit(new PrintVisitor<String>());
	}
}
