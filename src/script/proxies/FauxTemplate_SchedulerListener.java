package script.proxies;

import java.util.LinkedList;
import java.util.List;

import inspect.Nodeable;
import logging.Logs;
import script.ScriptEnvironment;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptKeywordType;
import script.values.RiffScriptFunction;
import script.values.ScriptTemplate;
import script.values.ScriptTemplate_Abstract;
import script.values.ScriptValue;
import script.values.ScriptValueType;
import script.values.ScriptValue_Faux;

public class FauxTemplate_SchedulerListener extends FauxTemplate implements Nodeable {
	public static final String SCHEDULERLISTENERSTRING = "SchedulerListener";

	public FauxTemplate_SchedulerListener(ScriptEnvironment env) {
		super(env, ScriptValueType.createType(env, SCHEDULERLISTENERSTRING), ScriptValueType.getObjectType(env), new LinkedList<ScriptValueType>(), true);
	}

	public FauxTemplate_SchedulerListener(ScriptEnvironment env, ScriptValueType type) {
		super(env, type);
	}

	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	@Override
	public ScriptValue execute(Referenced ref, String name, List<ScriptValue> params, ScriptTemplate_Abstract rawTemplate) throws ScriptException {
		assert Logs.openNode("Faux Template Executions", "Executing scheduler listener faux template function (" + RiffScriptFunction.getDisplayableFunctionName(name) + ")");
		assert Logs.addSnapNode("Template provided", rawTemplate);
		assert Logs.addSnapNode("Parameters provided", params);
		ScriptValue returning = this.getExtendedFauxClass().execute(ref, name, params, rawTemplate);
		assert Logs.closeNode();
		return returning;
	}

	// addFauxFunction(name,ScriptValueType type,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract)
	// All functions must be defined here. All function bodies are defined in 'execute'.
	@Override
	public void initialize() throws ScriptException {
		assert Logs.openNode("Faux Template Initializations", "Initializing scheduler listener faux template");
		this.disableFullCreation();
		this.getExtendedClass().initialize();
		List<ScriptValue> params = new LinkedList<ScriptValue>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.LONG));
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_Asset.ASSETSTRING)));
		this.addFauxFunction("iterate", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, true, false);
		assert Logs.closeNode();
	}

	// Define default constructor here
	@Override
	public ScriptTemplate instantiateTemplate() {
		return new FauxTemplate_SchedulerListener(this.getEnvironment(), this.getType());
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Scheduler listener faux template");
		super.nodificate();
		assert Logs.closeNode();
	}
}
