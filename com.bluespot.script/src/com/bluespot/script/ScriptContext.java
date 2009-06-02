package com.bluespot.script;

import java.util.Queue;

public interface ScriptContext {

	ScriptContext getContext(String name);

	ScriptFunction<?> getFunction(String name);

	ScriptContext getParentContext();

	Queue<Object> getPositionalValues();

	Object getValue(String name);

	void integrateInto(ScriptContext parentContext);

	void pushValue(Object value);

	void putContext(String name, ScriptContext context);

	void putFunction(String name, ScriptFunction<?> function);

	void putValue(String name, Object value);

	void setParentContext(ScriptContext parentContext);

	Object shiftValue();
}
