package com.bluespot.script;

import java.util.Queue;

public interface ScriptContext {
    
    Object getValue(String name);
    void putValue(String name, Object value);
    
    ScriptFunction<?> getFunction(String name);
    void putFunction(String name, ScriptFunction<?> function);
    
    ScriptContext getContext(String name);
    void putContext(String name, ScriptContext context);
    
    Queue<Object> getPositionalValues();
    void pushValue(Object value);
    Object shiftValue();

    void setParentContext(ScriptContext parentContext);
    ScriptContext getParentContext();

    void integrateInto(ScriptContext parentContext);
}
