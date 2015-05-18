package script.proxies;

import java.util.LinkedList;
import java.util.List;

import inspect.Nodeable;
import logging.Logs;
import script.Conversions;
import script.ScriptConvertible;
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
import asset.Ace;
import asset.Archetype;

public class FauxTemplate_Ace extends FauxTemplate implements ScriptConvertible<Ace>, Nodeable {
	public static final String ACESTRING = "Ace";
	private Ace ace;

	public FauxTemplate_Ace(ScriptEnvironment env) {
		super(env, ScriptValueType.createType(env, ACESTRING), ScriptValueType.getObjectType(env), new LinkedList<ScriptValueType>(), false);
	}

	public FauxTemplate_Ace(ScriptEnvironment env, ScriptValueType type) {
		super(env, type);
	}

	// Nodeable and ScriptConvertible interfaces
	@Override
	public Ace convert(ScriptEnvironment env) {
		return this.ace;
	}

	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	@Override
	public ScriptValue execute(Referenced ref, String name, List<ScriptValue> params, ScriptTemplate_Abstract rawTemplate) throws ScriptException {
		assert Logs.openNode("Faux Template Executions", "Executing ace faux template function (" + RiffScriptFunction.getDisplayableFunctionName(name) + ")");
		FauxTemplate_Ace template = (FauxTemplate_Ace) rawTemplate;
		assert Logs.addSnapNode("Template provided", template);
		assert Logs.addSnapNode("Parameters provided", params);
		if (name == null || name.equals("")) {
			if (template == null) {
				template = (FauxTemplate_Ace) this.createObject(ref, template);
			}
			template.setAce(new Ace(Conversions.getArchetype(this.getEnvironment(), params.get(0)), Conversions.getDouble(this.getEnvironment(), params.get(1))));
			params.clear();
		} else if (name.equals("setEfficiency")) {
			template.getAce().setEfficiency(Conversions.getDouble(this.getEnvironment(), params.get(0)));
			assert Logs.closeNode();
			return null;
		} else if (name.equals("getEfficiency")) {
			ScriptValue returning = Conversions.wrapDouble(this.getEnvironment(), template.getAce().getEfficiency());
			assert Logs.closeNode();
			return returning;
		} else if (name.equals("getArchetype")) {
			ScriptValue returning = Conversions.wrapArchetype(this.getEnvironment(), template.getAce().getArchetype());
			assert Logs.closeNode();
			return returning;
		}
		ScriptValue returning = this.getExtendedFauxClass().execute(ref, name, params, template);
		assert Logs.closeNode();
		return returning;
	}

	public Ace getAce() {
		return this.ace;
	}

	// All functions must be defined here. All function bodies are defined in 'execute'.
	@Override
	public void initialize() throws ScriptException {
		assert Logs.openNode("Faux Template Initializations", "Initializing ace faux template");
		List<ScriptValue> fxnParams = new LinkedList<ScriptValue>();
		fxnParams.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_Archetype.ARCHETYPESTRING)));
		fxnParams.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.DOUBLE));
		this.addConstructor(this.getType(), fxnParams);
		this.disableFullCreation();
		this.getExtendedClass().initialize();
		fxnParams = new LinkedList<ScriptValue>();
		fxnParams.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.DOUBLE));
		this.addFauxFunction("setEfficiency", ScriptValueType.VOID, fxnParams, ScriptKeywordType.PUBLIC, false, false);
		fxnParams = new LinkedList<ScriptValue>();
		this.addFauxFunction("getEfficiency", ScriptValueType.DOUBLE, new LinkedList<ScriptValue>(), ScriptKeywordType.PUBLIC, false, false);
		this.addFauxFunction("getArchetype", ScriptValueType.createType(this.getEnvironment(), FauxTemplate_Archetype.ARCHETYPESTRING), fxnParams, ScriptKeywordType.PUBLIC, false, false);
		assert Logs.closeNode();
	}

	// Define default constructor here
	@Override
	public ScriptTemplate instantiateTemplate() {
		return new FauxTemplate_Ace(this.getEnvironment(), this.getType());
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Ace Faux Script-Element");
		super.nodificate();
		assert Logs.addNode(this.ace);
		assert Logs.closeNode();
	}

	public void setAce(Ace ace) {
		this.ace = ace;
	}
}
