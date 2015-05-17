package script.operations;

import inspect.Nodeable;
import logging.Logs;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptElement;
import script.values.ScriptValue;
import script.values.ScriptValueType;
import script.values.ScriptValue_Boolean;

public class ScriptExecutable_InvertBoolean extends ScriptElement implements ScriptExecutable, ScriptValue, Nodeable {
	private ScriptExecutable value;

	public ScriptExecutable_InvertBoolean(Referenced ref, ScriptExecutable value) {
		super(ref);
		this.value = value;
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		return this.getValue().castToType(ref, type);
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		return new ScriptValue_Boolean(this.getEnvironment(), !((ScriptValue_Boolean) this.value.execute()).getBooleanValue());
	}

	// ScriptValue_Abstract implementation
	@Override
	public ScriptValueType getType() {
		return ScriptValueType.BOOLEAN;
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		return this.execute();
	}

	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return this.getType().equals(type);
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Boolean Inverter");
		assert Logs.addSnapNode("Value", this.value);
		assert Logs.closeNode();
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws ScriptException {
		return this.getValue().setValue(ref, value);
	}

	@Override
	public int valuesCompare(Referenced ref, ScriptValue rhs) throws ScriptException {
		return this.getValue().valuesCompare(ref, rhs);
	}

	@Override
	public boolean valuesEqual(Referenced ref, ScriptValue rhs) throws ScriptException {
		return this.getValue().valuesEqual(ref, rhs);
	}
}
