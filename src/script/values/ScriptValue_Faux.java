package script.values;

import inspect.Nodeable;
import logging.Logs;
import script.ScriptEnvironment;
import script.exceptions.InternalException;
import script.exceptions.ScriptException;
import script.parsing.Referenced;

public class ScriptValue_Faux implements Nodeable, ScriptValue {
	private final ScriptEnvironment environment;
	private final ScriptValueType type;

	public ScriptValue_Faux(ScriptEnvironment env, ScriptValueType type) {
		this.environment = env;
		this.type = type;
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		throw new InternalException(this.getEnvironment(), "Invalid call in ScriptValue_Faux");
	}

	@Override
	public ScriptEnvironment getEnvironment() {
		return this.environment;
	}

	@Override
	public ScriptValueType getType() {
		return this.type;
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		throw new InternalException(this.getEnvironment(), "Invalid call in ScriptValue_Faux");
	}

	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return ScriptValueType.isConvertibleTo(this.getEnvironment(), this.getType(), type);
	}

	@Override
	public void nodificate() {
		assert Logs.addNode("Faux Script-Value (" + this.getType() + ")");
	}

	protected void setType(ScriptValueType type) {
		throw new InternalException(this.getEnvironment(), "Invalid call in ScriptValue_Faux");
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws ScriptException {
		throw new InternalException(this.getEnvironment(), "Invalid call in ScriptValue_Faux");
	}

	@Override
	public int valuesCompare(Referenced ref, ScriptValue rhs) throws ScriptException {
		throw new InternalException(this.getEnvironment(), "Invalid call in ScriptValue_Faux");
	}

	@Override
	public boolean valuesEqual(Referenced ref, ScriptValue rhs) throws ScriptException {
		throw new InternalException(this.getEnvironment(), "Invalid call in ScriptValue_Faux");
	}
}
