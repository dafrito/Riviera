package com.bluespot.script;

import java.util.ArrayDeque;
import java.util.Queue;

public class EmptyScriptContext extends ImmutableScriptContext {

	public ScriptContext getContext(final String name) {
		return null;
	}

	public ScriptFunction<?> getFunction(final String name) {
		return null;
	}

	public ScriptContext getParentContext() {
		return null;
	}

	public Queue<Object> getPositionalValues() {
		return EmptyScriptContext.emptyQueue;
	}

	public Object getValue(final String name) {
		return null;
	}

	public void integrateInto(final ScriptContext parentContext) {
		// Do nothing, since this is empty.
	}

	public Object shiftValue() {
		throw new UnsupportedOperationException("This ScriptContext does not have any positional values");
	}

	private static final Queue<Object> emptyQueue = new ArrayDeque<Object>();

}
