package com.bluespot.script;

public interface ScriptFunction<T> {
	T getValue(ScriptContext context);
}
