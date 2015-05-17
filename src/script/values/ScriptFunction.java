package script.values;

import java.util.List;

import script.exceptions.ScriptException;
import script.operations.ScriptExecutable;
import script.parsing.Referenced;
import script.parsing.ScriptKeywordType;

public interface ScriptFunction {
	public void addExpression(ScriptExecutable exp) throws ScriptException;

	public void addExpressions(List<ScriptExecutable> list) throws ScriptException;

	public boolean areParametersConvertible(List<ScriptValue> list);

	public boolean areParametersEqual(List<ScriptValue> list);

	public void execute(Referenced ref, List<ScriptValue> valuesGiven) throws ScriptException;

	public List<ScriptValue> getParameters();

	public ScriptKeywordType getPermission();

	public ScriptValueType getReturnType();

	public ScriptValue getReturnValue();

	// ScriptFunction implementation
	public boolean isAbstract();

	public boolean isStatic();

	public void setReturnValue(Referenced element, ScriptValue value) throws ScriptException;
}
