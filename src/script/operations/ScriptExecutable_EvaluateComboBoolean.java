package script.operations;

import inspect.Nodeable;
import logging.Logs;
import script.exceptions.ClassCastScriptException;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptElement;
import script.parsing.ScriptOperatorType;
import script.values.ScriptValue;
import script.values.ScriptValueType;
import script.values.ScriptValue_Boolean;

public class ScriptExecutable_EvaluateComboBoolean extends ScriptElement implements ScriptValue, ScriptExecutable, Nodeable {
	private ScriptValue lhs, rhs;
	private final ScriptOperatorType operator;

	public ScriptExecutable_EvaluateComboBoolean(Referenced ref, ScriptValue lhs, ScriptValue rhs, ScriptOperatorType operator) {
		super(ref);
		this.lhs = lhs;
		this.rhs = rhs;
		this.operator = operator;
		if (this.operator == null) {
			throw new NullPointerException("operator must not be null");
		}
		if (this.operator != ScriptOperatorType.AND && this.operator != ScriptOperatorType.OR) {
			throw new IllegalArgumentException("operator must be AND or OR");
		}
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		return this.getValue().castToType(ref, type);
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		assert Logs.openNode("Combo-Boolean Evaluations", "Evaluating Combo-Boolean Expression (" + this.operator + ")");
		assert Logs.addNode(this);
		if (this.lhs.isConvertibleTo(ScriptValueType.BOOLEAN)) {
			throw new ClassCastScriptException(this, this.lhs, ScriptValueType.BOOLEAN);
		}
		if (this.rhs.isConvertibleTo(ScriptValueType.BOOLEAN)) {
			throw new ClassCastScriptException(this, this.rhs, ScriptValueType.BOOLEAN);
		}
		ScriptValue_Boolean lhs = (ScriptValue_Boolean) this.lhs.getValue();
		ScriptValue_Boolean rhs = (ScriptValue_Boolean) this.rhs.getValue();
		switch (this.operator) {
		case AND:
			return new ScriptValue_Boolean(this.getEnvironment(), (lhs.getBooleanValue() && rhs.getBooleanValue()));
		case OR:
			return new ScriptValue_Boolean(this.getEnvironment(), (lhs.getBooleanValue() || rhs.getBooleanValue()));
		default:
			throw new AssertionError("Unexpected default");
		}
	}

	// ScriptValue_Abstract implementation
	@Override
	public ScriptValueType getType() {
		return this.lhs.getType();
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
		assert Logs.openNode("Combo Boolean Expression");
		assert Logs.addSnapNode("Left side", this.lhs);
		assert Logs.addSnapNode("Right side", this.rhs);
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
