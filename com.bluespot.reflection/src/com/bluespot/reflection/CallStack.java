package com.bluespot.reflection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CallStack implements Iterable<CallStack.Frame> {

    protected static final Set<Package> ignoredPackages = new HashSet<Package>();
    protected static final Set<String> ignoredMethodNames = new HashSet<String>();
    
    public static void ignorePackage(Package ignoredPackage) {
        ignoredPackages.add(ignoredPackage);
    }
    
    public static void ignoreMethodName(String name) {
        ignoredMethodNames.add(name);
    }
    
    static {
        CallStack.ignorePackage(CallStack.class.getPackage());
        CallStack.ignorePackage(Package.getPackage("java.util.logging"));
    }

    private final Deque<CallStack.Frame> callStack;

    public CallStack() {
        this.callStack = new ArrayDeque<CallStack.Frame>();
    }

    public static boolean isIgnored(CallStack.Frame frame) {
        return ignoredPackages.contains(Package.getPackage(frame.getPackageName())) || ignoredMethodNames.contains(frame.getMethodName());
    }

    public void push(CallStack.Frame frame) {
        if(!CallStack.isIgnored(frame))
            this.callStack.push(frame);
    }

    public CallStack.Frame getCurrentFrame() {
        return this.callStack.peekFirst();
    }

    public CallStack.Frame pop() {
        return this.callStack.removeFirst();
    }
    
    public boolean contains(CallStack.Frame frame) {
        return this.callStack.contains(frame);
    }

    public boolean isEmpty() {
        return this.callStack.isEmpty();
    }

    public int size() {
        return this.callStack.size();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("CallStack (%d element(s))%n", this.size()));
        for(CallStack.Frame frame : this) {
            builder.append(String.format("\t%s%n", frame.toString()));
        }
        return builder.toString();
    }

    public static class Frame {

        private final String methodName;
        private final String className;
        private final String packageName;

        public Frame(String className, String methodName) {
            this.className = className;
            this.methodName = methodName;
            this.packageName = this.className.substring(0, this.className.lastIndexOf("."));
        }

        public String getMethodName() {
            return this.methodName;
        }

        public String getClassName() {
            return this.className;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public boolean isValid() {
            return this.getClassName() != null && this.getMethodName() != null;
        }
        
        @Override
        public boolean equals(Object other) {
            if(this == other)
                return true;
            if(!(other instanceof Frame))
                return false;
            Frame otherEntry = (Frame)other;
            if(this.isValid()) {
                return this.getClassName().equals(otherEntry.getClassName())
                    && this.getMethodName().equals(otherEntry.getMethodName());
            }
            return otherEntry.isValid() == false;

        }

        @Override
        public int hashCode() {
            int result = 17;
            if(this.isValid()) {
                result = 31 * result + this.getClassName().hashCode();
                result = 31 * result + this.getMethodName().hashCode();
            }
            return result;
        }
        
        @Override
        public String toString() {
            return String.format("CallStack.Frame(%s, %s)", this.getClassName(), this.getMethodName());
        }
    }

    public Iterator<Frame> iterator() {
        return this.callStack.iterator();
    }
}