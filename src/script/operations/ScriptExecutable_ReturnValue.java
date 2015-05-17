package script.operations;

import inspect.Nodeable;
import logging.Logs;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptElement;
import script.values.Returnable;
import script.values.ScriptValue;

public class ScriptExecutable_ReturnValue extends ScriptElement implements ScriptExecutable, Returnable, Nodeable {
	private ScriptValue value;

	public ScriptExecutable_ReturnValue(Referenced ref, ScriptValue value) {
		super(ref);
		this.value = value;
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		assert Logs.openNode("Executing returnable script-value");
		ScriptValue value = this.value.getValue();
		assert Logs.closeNode();
		return value;
	}

	@Override
	public ScriptValue getReturnValue() throws ScriptException {
		return this.value.getValue();
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Returnable Script-Value");
		assert Logs.addSnapNode("Returned Value", this.value);
		assert Logs.closeNode();
	}

	// Returnabled implementation
	@Override
	public boolean shouldReturn() {
		return true;
	}
}
