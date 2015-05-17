package script.proxies;

import java.util.LinkedList;
import java.util.List;

import inspect.Nodeable;
import logging.Logs;
import script.ScriptConvertible;
import script.ScriptEnvironment;
import script.exceptions.FunctionNotFoundScriptException;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptKeyword;
import script.values.RiffScriptFunction;
import script.values.ScriptTemplate;
import script.values.ScriptTemplate_Abstract;
import script.values.ScriptValue;
import script.values.ScriptValueType;

public class FauxTemplate_Object extends FauxTemplate implements ScriptConvertible<FauxTemplate_Object>, Nodeable {
	public static ScriptKeyword OBJECT;
	public static final String OBJECTSTRING = "Object";

	public FauxTemplate_Object(ScriptEnvironment env) {
		super(env, ScriptValueType.getObjectType(env), null, new LinkedList<ScriptValueType>(), true);
	}

	public FauxTemplate_Object(ScriptEnvironment env, ScriptValueType type) {
		super(env, type);
	}

	// ScriptConvertible and Nodeable implementations
	@Override
	public FauxTemplate_Object convert(ScriptEnvironment env) {
		return this;
	}

	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	@Override
	public ScriptValue execute(Referenced ref, String name, List<ScriptValue> params, ScriptTemplate_Abstract rawTemplate) throws ScriptException {
		assert Logs.openNode("Faux Template Executions", "Executing Object Faux Template Function (" + RiffScriptFunction.getDisplayableFunctionName(name) + ")");
		assert Logs.addSnapNode("Template provided", rawTemplate);
		assert Logs.addSnapNode("Parameters provided", params);
		if (name == null || name.equals("")) {
			if (params.size() != 0) {
				throw new FunctionNotFoundScriptException(ref, name, params);
			}
			if (rawTemplate == null) {
				rawTemplate = this.createObject(ref, rawTemplate);
			}
			assert Logs.closeNode();
			return rawTemplate;
		}
		throw new FunctionNotFoundScriptException(ref, name, params);
	}

	// addFauxFunction(name,ScriptValueType type,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract)
	// All functions must be defined here. All function bodies are defined in 'execute'.
	@Override
	public void initialize() throws ScriptException {
		assert Logs.openNode("Faux Template Initializations", "Initializing object faux template");
		this.addConstructor(this.getType());
		this.disableFullCreation();
		assert Logs.closeNode();
	}

	// Define default constructor here
	@Override
	public ScriptTemplate instantiateTemplate() {
		return new FauxTemplate_Object(this.getEnvironment(), this.getType());
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Object Faux Template");
		super.nodificate();
		assert Logs.closeNode();
	}
}
