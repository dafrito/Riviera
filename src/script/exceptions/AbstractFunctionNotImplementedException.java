/**
 * 
 */
package script.exceptions;

import logging.Logs;
import script.parsing.Referenced;
import script.values.ScriptFunction;
import script.values.ScriptTemplate_Abstract;

public class AbstractFunctionNotImplementedException extends ScriptException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8937024716489715221L;
	private ScriptTemplate_Abstract object;
	private ScriptFunction function;

	public AbstractFunctionNotImplementedException(Referenced ref, ScriptTemplate_Abstract object, ScriptFunction function) {
		super(ref);
		this.object = object;
		this.function = function;
	}

	@Override
	public void getExtendedInformation() {
		assert Logs.addSnapNode("The template is not abstract and does not implement the following function", this.object);
		assert Logs.addNode(this.function);
	}

	@Override
	public String getName() {
		return "Abstract Function Not Implememented";
	}
}