package com.bluespot.script;

import java.util.ArrayDeque;
import java.util.Queue;

public class EmptyScriptContext extends ImmutableScriptContext {

    private static final Queue<Object> emptyQueue = new ArrayDeque<Object>();

    public ScriptContext getContext(String name) {
        return null;
    }

    public ScriptFunction<?> getFunction(String name) {
        return null;
    }

    public ScriptContext getParentContext() {
        return null;
    }
    
    public Queue<Object> getPositionalValues() {
        return emptyQueue;
    }

    public Object getValue(String name) {
        return null;
    }

    public void integrateInto(ScriptContext parentContext) {
        // Do nothing, since this is empty.
    }
    
    public Object shiftValue() {
        throw new UnsupportedOperationException("This ScriptContext does not have any positional values");
    }

}
