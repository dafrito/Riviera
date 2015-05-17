package script.operations;

import inspect.Nodeable;
import logging.Logs;
import script.ScriptEnvironment;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptElement;
import script.parsing.ScriptKeywordType;
import script.values.ScriptValue;
import script.values.ScriptValueType;
import script.values.ScriptValue_Variable;

public class ScriptExecutable_CreateVariable extends ScriptValue_Variable implements ScriptValue, ScriptExecutable, Nodeable, Referenced {
	private ScriptKeywordType permission;
	private String name;
	private ScriptElement reference;

	public ScriptExecutable_CreateVariable(Referenced ref, ScriptValueType type, String name, ScriptKeywordType permission) throws ScriptException {
		super(ref.getEnvironment(), type, permission);
		this.reference = ref.getDebugReference();
		this.name = name;
		this.permission = permission;
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		return this.getValue().castToType(ref, type);
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		assert Logs.openNode("Creating Variable (" + this.name + ")");
		ScriptValue_Variable value;
		this.getEnvironment().getCurrentObject().addVariable(this, this.name, value = new ScriptValue_Variable(this.getEnvironment(), this.getType(), this.getPermission()));
		assert Logs.addSnapNode("Variable Created", value);
		assert Logs.closeNode();
		return value;
	}

	// Referenced implementation
	@Override
	public ScriptElement getDebugReference() {
		return this.reference;
	}

	// ScriptValue_Abstract implementation
	@Override
	public ScriptEnvironment getEnvironment() {
		return this.getDebugReference().getEnvironment();
	}

	public String getName() {
		return this.name;
	}

	// Overloaded ScriptValue_Variable functions
	@Override
	public ScriptKeywordType getPermission() throws ScriptException {
		return this.permission;
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		return this.execute().getValue();
	}

	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return ScriptValueType.isConvertibleTo(this.getEnvironment(), this.getType(), type);
	}

	// Nodeable implementation
	@Override
	public void nodificate() {
		assert Logs.openNode("Variable-Creation Script-Element (" + this.name + ")");
		super.nodificate();
		assert Logs.closeNode();
	}

	@Override
	public ScriptValue setReference(Referenced ref, ScriptValue value) throws ScriptException {
		return ((ScriptValue_Variable) this.execute()).setReference(ref, value);
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws ScriptException {
		return this.execute().setValue(ref, value);
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
