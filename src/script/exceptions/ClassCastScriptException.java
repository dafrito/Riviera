/**
 * 
 */
package script.exceptions;

import script.parsing.Referenced;
import script.values.ScriptValue;
import script.values.ScriptValueType;

public class ClassCastScriptException extends ScriptException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 375067664989244754L;
	private String value, castingValue;

	public ClassCastScriptException(Referenced ref, ScriptValue value, ScriptValue castValue) throws ScriptException {
		this(ref, value.getType().getName(), castValue.getType().getName());
	}

	public ClassCastScriptException(Referenced ref, ScriptValue castingValue, ScriptValueType type) throws ScriptException {
		this(ref, type.getName(), type.getName());
	}

	public ClassCastScriptException(Referenced ref, String type, String castType) {
		super(ref.getEnvironment(), ref);
		this.value = type;
		this.castingValue = castType;
	}

	@Override
	public String getName() {
		return "Casting Exception - Invalid conversion: " + this.castingValue + " --> " + this.value;
	}
}