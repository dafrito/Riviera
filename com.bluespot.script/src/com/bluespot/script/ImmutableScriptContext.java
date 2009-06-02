package com.bluespot.script;


public abstract class ImmutableScriptContext implements ScriptContext {

    public void putContext(String name, ScriptContext context) {
        throw new UnsupportedOperationException("This ScriptContext does not support adding contexts");
    }

    public void putFunction(String name, ScriptFunction<?> function) {
        throw new UnsupportedOperationException("This ScriptContext does not support adding functions");
    }

    public void putValue(String name, Object value) {
        throw new UnsupportedOperationException("This ScriptContext does not support adding values");
    }

    public void setParentContext(ScriptContext parentContext) {
        throw new UnsupportedOperationException("This ScriptContext cannot have any parents");
    }

    public void pushValue(Object value) {
        throw new UnsupportedOperationException("This ScriptContext does not support adding positional values");
    }

}
