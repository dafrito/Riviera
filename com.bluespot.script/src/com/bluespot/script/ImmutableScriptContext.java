package com.bluespot.script;

public abstract class ImmutableScriptContext implements ScriptContext {

	public void pushValue(final Object value) {
		throw new UnsupportedOperationException("This ScriptContext does not support adding positional values");
	}

	public void putContext(final String name, final ScriptContext context) {
		throw new UnsupportedOperationException("This ScriptContext does not support adding contexts");
	}

	public void putFunction(final String name, final ScriptFunction<?> function) {
		throw new UnsupportedOperationException("This ScriptContext does not support adding functions");
	}

	public void putValue(final String name, final Object value) {
		throw new UnsupportedOperationException("This ScriptContext does not support adding values");
	}

	public void setParentContext(final ScriptContext parentContext) {
		throw new UnsupportedOperationException("This ScriptContext cannot have any parents");
	}

}
