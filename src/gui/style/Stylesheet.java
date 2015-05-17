package gui.style;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import inspect.Inspectable;
import logging.Logs;
import script.ScriptConvertible;
import script.ScriptEnvironment;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.proxies.FauxTemplate;
import script.values.ScriptTemplate;
import script.values.ScriptTemplate_Abstract;
import script.values.ScriptValue;
import script.values.ScriptValueType;

@Inspectable
public class Stylesheet extends FauxTemplate implements ScriptValue, ScriptConvertible<Stylesheet> {
	private final Map<StylesheetProperty, Object> styleElements = new EnumMap<StylesheetProperty, Object>(StylesheetProperty.class);
	public static final String STYLESHEETSTRING = "Stylesheet";

	public Stylesheet(ScriptEnvironment env) {
		super(env, ScriptValueType.createType(env, STYLESHEETSTRING), ScriptValueType.getObjectType(env), new LinkedList<ScriptValueType>(), false);
	}

	public Stylesheet(ScriptEnvironment env, boolean flag) {
		super(env, ScriptValueType.createType(env, STYLESHEETSTRING));
	}

	public void addElement(StylesheetProperty type, Object element) {
		assert Logs.openNode("Stylesheet Element Additions", "Adding a " + element + " element to this stylesheet");
		assert Logs.addNode(this);
		assert Logs.addNode(element);
		this.styleElements.put(type, element);
		assert Logs.closeNode();
	}

	// ScriptConvertible implementation
	@Override
	public Stylesheet convert(ScriptEnvironment env) {
		return this;
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute(Referenced ref, String name, List<ScriptValue> params, ScriptTemplate_Abstract template) throws ScriptException {
		return null;
	}

	public Object getElement(StylesheetProperty elementCode) {
		return this.styleElements.get(elementCode);
	}

	// FauxTemplate extensions
	@Override
	public ScriptTemplate instantiateTemplate() {
		return new Stylesheet(this.getEnvironment(), true);
	}

	@Inspectable
	public Map<StylesheetProperty, Object> getProperties() {
		return Collections.unmodifiableMap(this.styleElements);
	}
}
