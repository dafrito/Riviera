package com.bluespot.script;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;


public class DefaultScriptContext implements ScriptContext {
    
    protected final Map<String, ScriptContext> contexts = new HashMap<String, ScriptContext>();
    protected final Map<String, ScriptFunction<?>> functions = new HashMap<String, ScriptFunction<?>>();
    protected final Map<String, Object> values = new HashMap<String, Object>();

    protected final Queue<Object> positionalValues = new ArrayDeque<Object>();
    private ScriptContext parentContext;

    public ScriptContext getContext(String name) {
        ScriptContext context = this.contexts.get(name);
        if(context != null)
            return context;
        return this.getParentContext().getContext(name);
    }

    public void putContext(String name, ScriptContext context) {
        this.contexts.put(name, context);
    }

    public ScriptFunction<?> getFunction(String name) {
        ScriptFunction<?> function = this.functions.get(name);
        if(function != null)
            return function;
        return this.getParentContext().getFunction(name);
    }

    public void putFunction(String name, ScriptFunction<?> function) {
        this.functions.put(name, function);
    }

    public ScriptContext getParentContext() {
        return this.parentContext;
    }

    public void setParentContext(ScriptContext parentContext) {
        this.parentContext = parentContext;
    }
    
    public Queue<Object> getPositionalValues() {
        return this.positionalValues;
    }

    public Object getValue(String name) {
        Object value = this.values.get(name);
        if(value != null)
            return value;
        return this.getParentContext().getValue(name);
    }

    public void putValue(String name, Object value) {
        this.values.put(name, value);
    }

    public void pushValue(Object value) {
        this.positionalValues.add(value);
    }


    public Object shiftValue() {
        return this.positionalValues.remove();
    }

    public void integrateInto(ScriptContext targetContext) {
        for(Entry<String, ScriptContext> entry : this.contexts.entrySet()) {
            targetContext.putContext(entry.getKey(), entry.getValue());
        }
        for(Entry<String, ScriptFunction<?>> entry : this.functions.entrySet()) {
            targetContext.putFunction(entry.getKey(), entry.getValue());
        }
        for(Entry<String, Object> entry: this.values.entrySet()) {
            targetContext.putValue(entry.getKey(), entry.getValue());
        }
        for(Object value : this.positionalValues) {
            targetContext.pushValue(value);
        }
    }

}
