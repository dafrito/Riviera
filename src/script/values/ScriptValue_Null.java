package script.values;

import inspect.Nodeable;
import logging.Logs;
import script.exceptions.IncompatibleObjectsException;
import script.exceptions.ScriptException;
import script.operations.ScriptExecutable;
import script.parsing.Referenced;
import script.parsing.ScriptElement;

public class ScriptValue_Null extends ScriptElement implements ScriptExecutable, ScriptValue, Nodeable {
	public ScriptValue_Null(Referenced ref) {
		super(ref);
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		return this;
	}

	@Override
	public ScriptValue execute() throws ScriptException {
		return this;
	}

	// ScriptValue_Abstract implementation
	@Override
	public ScriptValueType getType() {
		return ScriptValueType.VOID;
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		return this;
	}

	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return true;
	}

	@Override
	public void nodificate() {
		assert Logs.addNode("Null Script-Value");
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws ScriptException {
		throw new UnsupportedOperationException("Set Value");
	}

	@Override
	public int valuesCompare(Referenced ref, ScriptValue rhs) throws ScriptException {
		throw new IncompatibleObjectsException(ref, this, rhs);
	}

	@Override
	public boolean valuesEqual(Referenced ref, ScriptValue rhs) throws ScriptException {
		return (rhs == null || rhs.getValue() == null || rhs.getValue() instanceof ScriptValue_Null);
	}
}
