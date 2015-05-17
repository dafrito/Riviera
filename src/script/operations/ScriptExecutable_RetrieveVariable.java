package script.operations;

import inspect.Nodeable;
import logging.Logs;
import script.ScriptEnvironment;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptElement;
import script.parsing.ScriptKeywordType;
import script.values.ScriptTemplate_Abstract;
import script.values.ScriptValue;
import script.values.ScriptValueType;
import script.values.ScriptValue_Variable;

public class ScriptExecutable_RetrieveVariable extends ScriptValue_Variable implements ScriptExecutable, ScriptValue, Nodeable, Referenced {
	private String name;
	private ScriptValue template;
	private ScriptElement reference;

	public ScriptExecutable_RetrieveVariable(Referenced ref, ScriptValue template, String name, ScriptValueType type) throws ScriptException {
		super(ref.getEnvironment(), type, ScriptKeywordType.PRIVATE);
		this.reference = ref.getDebugReference();
		this.name = name;
		this.template = template;
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		return this.getVariable().castToType(ref, type);
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		return this.getValue();
	}

	// Referenced implementation
	@Override
	public ScriptElement getDebugReference() {
		return this.reference;
	}

	@Override
	public ScriptEnvironment getEnvironment() {
		return this.getDebugReference().getEnvironment();
	}

	// Overloaded ScriptValue_Variable functions
	@Override
	public ScriptKeywordType getPermission() throws ScriptException {
		return this.getVariable().getPermission();
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		return this.getVariable().getValue();
	}

	public ScriptValue_Variable getVariable() throws ScriptException {
		assert Logs.openNode("Executing Variable Retrieval (" + this.name + ")");
		ScriptValue_Variable variable;
		if (this.template != null) {
			assert Logs.addSnapNode("Template", this.template);
			variable = ((ScriptTemplate_Abstract) this.template.getValue()).getVariable(this.name);
		} else {
			variable = this.getEnvironment().retrieveVariable(this.name);
		}
		assert variable != null : "Variable not found (" + this.name + ")";
		assert Logs.closeNode();
		return variable;
	}

	// Abstract-value implementation
	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return ScriptValueType.isConvertibleTo(this.getEnvironment(), this.getType(), type);
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Variable-Placeholder (" + this.name + ")");
		super.nodificate();
		if (this.template != null) {
			assert Logs.addSnapNode("Reference Template", this.template);
		}
		assert Logs.closeNode();
	}

	@Override
	public ScriptValue setReference(Referenced ref, ScriptValue value) throws ScriptException {
		return this.getVariable().setReference(ref, value);
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws ScriptException {
		return this.getVariable().setValue(ref, value);
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
