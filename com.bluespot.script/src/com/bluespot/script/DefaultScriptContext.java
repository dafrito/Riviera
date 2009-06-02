package com.bluespot.script;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

public class DefaultScriptContext implements ScriptContext {

	private ScriptContext parentContext;
	protected final Map<String, ScriptContext> contexts = new HashMap<String, ScriptContext>();
	protected final Map<String, ScriptFunction<?>> functions = new HashMap<String, ScriptFunction<?>>();

	protected final Queue<Object> positionalValues = new ArrayDeque<Object>();
	protected final Map<String, Object> values = new HashMap<String, Object>();

	public ScriptContext getContext(final String name) {
		final ScriptContext context = this.contexts.get(name);
		if (context != null) {
			return context;
		}
		return this.getParentContext().getContext(name);
	}

	public ScriptFunction<?> getFunction(final String name) {
		final ScriptFunction<?> function = this.functions.get(name);
		if (function != null) {
			return function;
		}
		return this.getParentContext().getFunction(name);
	}

	public ScriptContext getParentContext() {
		return this.parentContext;
	}

	public Queue<Object> getPositionalValues() {
		return this.positionalValues;
	}

	public Object getValue(final String name) {
		final Object value = this.values.get(name);
		if (value != null) {
			return value;
		}
		return this.getParentContext().getValue(name);
	}

	public void integrateInto(final ScriptContext targetContext) {
		for (final Entry<String, ScriptContext> entry : this.contexts.entrySet()) {
			targetContext.putContext(entry.getKey(), entry.getValue());
		}
		for (final Entry<String, ScriptFunction<?>> entry : this.functions.entrySet()) {
			targetContext.putFunction(entry.getKey(), entry.getValue());
		}
		for (final Entry<String, Object> entry : this.values.entrySet()) {
			targetContext.putValue(entry.getKey(), entry.getValue());
		}
		for (final Object value : this.positionalValues) {
			targetContext.pushValue(value);
		}
	}

	public void pushValue(final Object value) {
		this.positionalValues.add(value);
	}

	public void putContext(final String name, final ScriptContext context) {
		this.contexts.put(name, context);
	}

	public void putFunction(final String name, final ScriptFunction<?> function) {
		this.functions.put(name, function);
	}

	public void putValue(final String name, final Object value) {
		this.values.put(name, value);
	}

	public void setParentContext(final ScriptContext parentContext) {
		this.parentContext = parentContext;
	}

	public Object shiftValue() {
		return this.positionalValues.remove();
	}

}
