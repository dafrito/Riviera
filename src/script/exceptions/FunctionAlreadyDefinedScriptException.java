/**
 * 
 */
package script.exceptions;

import logging.Logs;
import script.parsing.Referenced;
import script.values.RiffScriptFunction;

public class FunctionAlreadyDefinedScriptException extends ScriptException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6734795857087945843L;
	private String name;

	public FunctionAlreadyDefinedScriptException(Referenced ref, String name) {
		super(ref);
		this.name = name;
	}

	@Override
	public void getExtendedInformation() {
		assert Logs.addNode("The function, " + RiffScriptFunction.getDisplayableFunctionName(this.name) + ", is already defined");
	}

	@Override
	public String getName() {
		return "Function Already Defined (" + RiffScriptFunction.getDisplayableFunctionName(this.name) + ")";
	}
}