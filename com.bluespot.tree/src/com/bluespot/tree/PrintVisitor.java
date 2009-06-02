package com.bluespot.tree;

public class PrintVisitor<T> implements Visitor<T> {
    public static final String TAB = "    ";
    
    private String prefix = "";
    
    private void print(T value) {
        System.out.println(this.prefix + this.toString(value));
    }
    
    public String toString(T value) {
        return value.toString();
    }

    public void visitNode(Tree<T> node) {
        this.print(node.getValue());
        this.prefix = this.prefix + TAB;
        for(Tree<T> child : node) {
            this.visitNode(child);
        }
        this.prefix = this.prefix.substring(0, this.prefix.length() - TAB.length() );
    }
}
