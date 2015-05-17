package script.values;

import inspect.Nodeable;
import logging.Logs;
import script.ScriptConvertible;
import script.ScriptEnvironment;
import script.exceptions.ClassCastScriptException;
import script.exceptions.IncompatibleObjectsException;
import script.exceptions.ScriptException;
import script.parsing.Referenced;

public class ScriptValue_Boolean implements ScriptConvertible<Boolean>, ScriptValue, Nodeable {
	private boolean value;
	private final ScriptEnvironment environment;

	public ScriptValue_Boolean(ScriptEnvironment env, boolean value) {
		this.environment = env;
		this.value = value;
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		assert Logs.addNode("Type Casting", "Casting (" + this.getType() + " to " + type + ")");
		if (this.getType().equals(type)) {
			return this;
		}
		throw new ClassCastScriptException(ref, this, type);
	}

	// Overloaded and miscellaneous functions
	@Override
	public Boolean convert(ScriptEnvironment env) {
		return Boolean.valueOf(this.value);
	}

	public boolean getBooleanValue() {
		return this.value;
	}

	// Abstract-value implementation
	@Override
	public ScriptEnvironment getEnvironment() {
		return this.environment;
	}

	@Override
	public ScriptValueType getType() {
		return ScriptValueType.BOOLEAN;
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		return this;
	}

	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return this.getType().equals(type);
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Boolean Script Value (" + this.getBooleanValue() + ")");
		assert Logs.addNode("Reference: " + this);
		assert Logs.closeNode();
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws ScriptException {
		assert Logs.openNode("Value Assignments", "Setting Boolean Value");
		assert Logs.addSnapNode("Former value", this);
		this.value = ((ScriptValue_Boolean) value.castToType(ref, this.getType())).getBooleanValue();
		assert Logs.closeNode("New value", this);
		return this;
	}

	@Override
	public int valuesCompare(Referenced ref, ScriptValue rhs) throws ScriptException {
		throw new IncompatibleObjectsException(ref, this, rhs);
	}

	@Override
	public boolean valuesEqual(Referenced ref, ScriptValue rhs) throws ScriptException {
		return ((ScriptValue_Boolean) this.getValue()).getBooleanValue() == ((ScriptValue_Boolean) rhs.castToType(ref, this.getType())).getBooleanValue();
	}
}
