package script.proxies;

import java.util.LinkedList;
import java.util.List;

import logging.Logs;
import script.ScriptEnvironment;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.values.RiffScriptFunction;
import script.values.ScriptTemplate;
import script.values.ScriptTemplate_Abstract;
import script.values.ScriptValue;
import script.values.ScriptValueType;

public class FauxTemplate_GraphicalElement extends FauxTemplate {
	public static final String GRAPHICALELEMENTSTRING = "GraphicalElement";

	public FauxTemplate_GraphicalElement(ScriptEnvironment env) {
		super(env, ScriptValueType.createType(env, GRAPHICALELEMENTSTRING), ScriptValueType.getObjectType(env), new LinkedList<ScriptValueType>(), true);
	}

	public FauxTemplate_GraphicalElement(ScriptEnvironment env, ScriptValueType type) {
		super(env, type);
	}

	public FauxTemplate_GraphicalElement(ScriptEnvironment env, ScriptValueType type, ScriptValueType extended, List<ScriptValueType> implemented, boolean isAbstract) {
		super(env, type, extended, implemented, isAbstract);
	}

	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	@Override
	public ScriptValue execute(Referenced ref, String name, List<ScriptValue> params, ScriptTemplate_Abstract rawTemplate) throws ScriptException {
		assert Logs.openNode("Faux Template Executions", "Executing Graphical Element Faux Template Function (" + RiffScriptFunction.getDisplayableFunctionName(name) + ")");
		FauxTemplate_GraphicalElement template = (FauxTemplate_GraphicalElement) rawTemplate;
		ScriptValue returning;
		assert Logs.addSnapNode("Template provided", template);
		assert Logs.addSnapNode("Parameters provided", params);
		returning = this.getExtendedFauxClass().execute(ref, name, params, template);
		assert Logs.closeNode();
		return returning;
	}

	// All functions must be defined here. All function bodies are defined in 'execute'.
	@Override
	public void initialize() throws ScriptException {
		assert Logs.openNode("Faux Template Initializations", "Initializing graphical element faux template");
		this.addConstructor(this.getType());
		this.disableFullCreation();
		this.getExtendedClass().initialize();
		assert Logs.closeNode();
	}

	// Define default constructor here
	@Override
	public ScriptTemplate instantiateTemplate() {
		return new FauxTemplate_GraphicalElement(this.getEnvironment(), this.getType());
	}
}
