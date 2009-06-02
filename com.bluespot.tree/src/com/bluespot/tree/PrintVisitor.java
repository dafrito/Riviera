package com.bluespot.tree;

public class PrintVisitor<T> implements Visitor<T> {
	private String prefix = "";

	public String toString(final T value) {
		return value.toString();
	}

	public void visitNode(final Tree<T> node) {
		this.print(node.getValue());
		this.prefix = this.prefix + PrintVisitor.TAB;
		for (final Tree<T> child : node) {
			this.visitNode(child);
		}
		this.prefix = this.prefix.substring(0, this.prefix.length() - PrintVisitor.TAB.length());
	}

	private void print(final T value) {
		System.out.println(this.prefix + this.toString(value));
	}

	public static final String TAB = "    ";
}
