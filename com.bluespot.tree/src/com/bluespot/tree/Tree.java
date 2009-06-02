package com.bluespot.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tree<T> implements Iterable<Tree<T>> {

	private final List<Tree<T>> children = new ArrayList<Tree<T>>();
	private final Tree<T> parent;

	private final T value;
	private WalkerFactory<T> walkerFactory;

	public Tree(final T value) {
		this(null, value);
	}

	public Tree(final Tree<T> parent, final T value) {
		this.parent = parent;
		this.value = value;
		this.walkerFactory = TreeWalker.<T> createFactory();
	}

	public Tree<T> append(final T addedValue) {
		return this.append(new Tree<T>(this, addedValue));
	}

	public Tree<T> append(final Tree<T> node) {
		this.children.add(node);
		node.setWalkerFactory(this.walkerFactory);
		return node;
	}

	public Tree<T> get(final int i) {
		return this.children.get(i);
	}

	public Tree<T> getParent() {
		return this.parent;
	}

	public T getValue() {
		return this.value;
	}

	public WalkerFactory<T> getWalkerFactory() {
		return this.walkerFactory;
	}

	public int indexOf(final Object child) {
		if (child == this) {
			throw new IllegalArgumentException("Child cannot be its own parent");
		}
		return this.children.indexOf(child);
	}

	public boolean isEmpty() {
		return this.size() == 0;
	}

	public boolean isRoot() {
		return this.getParent() == null;
	}

	public Iterator<Tree<T>> iterator() {
		return this.children.iterator();
	}

	public void setWalkerFactory(final WalkerFactory<T> factory) {
		this.walkerFactory = factory;
	}

	public int size() {
		return this.children.size();
	}

	@Override
	public String toString() {
		return String.format("Node(%s)", this.getValue() != null ? this.getValue().toString() : "<no value>");
	}

	public void visit(final Visitor<T> visitor) {
		visitor.visitNode(this);
	}

	public TreeWalker<T> walker() {
		return this.walkerFactory.newInstance(this);
	}

}
