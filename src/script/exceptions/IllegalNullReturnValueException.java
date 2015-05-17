/**
 * 
 */
package script.exceptions;

import logging.Logs;
import script.ScriptEnvironment;
import script.parsing.Referenced;
import script.values.ScriptFunction;

public class IllegalNullReturnValueException extends ScriptException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6453503758041260366L;
	private ScriptFunction function;

	public IllegalNullReturnValueException(Referenced ref, ScriptFunction fxn) {
		super(ref);
		this.function = fxn;
	}

	public IllegalNullReturnValueException(ScriptEnvironment env, ScriptFunction fxn) {
		super(env);
		this.function = fxn;
	}

	@Override
	public void getExtendedInformation() {
		assert Logs.addSnapNode("This function is attempting to return implicitly, even though it is of type, " + this.function.getReturnType(), this.function);
	}

	@Override
	public String getName() {
		return "Illegal Null Return Value";
	}
}