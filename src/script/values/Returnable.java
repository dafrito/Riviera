package script.values;

import script.exceptions.ScriptException;

public interface Returnable {
	public ScriptValue getReturnValue() throws ScriptException;

	// Returnable implementation
	public boolean shouldReturn();
}
