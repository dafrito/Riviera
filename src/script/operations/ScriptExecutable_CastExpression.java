package script.operations;

import inspect.Nodeable;
import logging.Logs;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptElement;
import script.values.ScriptValue;
import script.values.ScriptValueType;

public class ScriptExecutable_CastExpression extends ScriptElement implements ScriptExecutable, ScriptValue, Nodeable {
	private ScriptExecutable castExpression;
	private ScriptValueType type;

	public ScriptExecutable_CastExpression(Referenced ref, ScriptValueType type, ScriptExecutable exec) {
		super(ref);
		this.castExpression = exec;
		this.type = type;
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		return this.getValue().castToType(ref, type);
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		ScriptValue left = this.castExpression.execute().getValue();
		assert Logs.openNode("Cast Expression Executions", "Executing cast to " + this.getType());
		assert Logs.addSnapNode("Value", left);
		ScriptValue value = this.castExpression.execute().castToType(this, this.getType());
		assert Logs.closeNode();
		return value;
	}

	// Abstract-value implementation
	@Override
	public ScriptValueType getType() {
		return this.type;
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		return this.execute();
	}

	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return ScriptValueType.isConvertibleTo(this.getEnvironment(), this.getType(), type);
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Script Cast Expression (To type: " + this.getType() + ")");
		assert Logs.addSnapNode("Cast Expression", this.castExpression);
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
