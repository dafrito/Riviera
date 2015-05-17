package script.operations;

import script.exceptions.ScriptException;
import script.parsing.ScriptElement;
import script.values.ScriptValue;

public interface ScriptExecutable {

	public ScriptValue execute() throws ScriptException;

	public ScriptElement getDebugReference();
}
