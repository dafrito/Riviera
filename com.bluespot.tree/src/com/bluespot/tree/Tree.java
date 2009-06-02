package com.bluespot.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tree<T> implements Iterable<Tree<T>> {

    private final Tree<T> parent;
    private final T value;

    private List<Tree<T>> children = new ArrayList<Tree<T>>();
    private WalkerFactory<T> walkerFactory;

    public Tree(T value) {
        this(null, value);
    }
    
    public Tree(Tree<T> parent, T value) {
        this.parent = parent;
        this.value = value;
        this.walkerFactory = TreeWalker.<T>createFactory();
    }
    
    public T getValue() {
        return this.value;
    }

    public Tree<T> getParent() {
        return this.parent;
    }

    public boolean isRoot() {
        return this.getParent() == null;
    }

    public void visit(Visitor<T> visitor) {
        visitor.visitNode(this);
    }
    
    public Tree<T> append(Tree<T> node) {
        this.children.add(node);
        node.setWalkerFactory(this.walkerFactory);
        return node;
    }

    public Tree<T> append(T addedValue) {
        return this.append(new Tree<T>(this, addedValue));
    }

    public int size() {
        return this.children.size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public Tree<T> get(int i) {
        return this.children.get(i);
    }

    public int indexOf(Object child) {
        if(child == this)
            throw new IllegalArgumentException("Child cannot be its own parent");
        return this.children.indexOf(child);
    }
    
    public Iterator<Tree<T>> iterator() {
        return this.children.iterator();
    }
    
    public TreeWalker<T> walker() {
        return this.walkerFactory.newInstance(this);
    }
    
    public void setWalkerFactory(WalkerFactory<T> factory) {
        this.walkerFactory = factory;
    }
    
    
    public WalkerFactory<T> getWalkerFactory() {
        return this.walkerFactory;
    }

    @Override
    public String toString() {
        return String.format("Node(%s)", this.getValue() != null ? this.getValue().toString() : "<no value>");
    }

}
