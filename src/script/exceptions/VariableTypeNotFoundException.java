/**
 * 
 */
package script.exceptions;

import logging.Logs;
import script.ScriptEnvironment;
import script.parsing.Referenced;

public class VariableTypeNotFoundException extends ScriptException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6495019820246931939L;
	private String type;

	public VariableTypeNotFoundException(Referenced ref, String type) {
		super(ref);
		this.type = type;
	}

	public VariableTypeNotFoundException(ScriptEnvironment env, String type) {
		super(env);
		this.type = type;
	}

	@Override
	public void getExtendedInformation() {
		assert Logs.addNode("The variable type, " + this.type + ", was not found");
	}

	@Override
	public String getName() {
		return "Undefined Variable-Type (" + this.type + ")";
	}
}