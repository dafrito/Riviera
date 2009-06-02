package com.bluespot.util;

public class Counter {
    private int count;

    public Counter() {
        this(0);
    }
    
    public Counter(int start) {
        this.count = start;
    }
    
    public int increment() {
        return this.count++;
    }
        
    public int getCount() {
        return this.count;
    }
}
