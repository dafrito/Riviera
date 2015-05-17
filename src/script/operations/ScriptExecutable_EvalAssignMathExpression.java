package script.operations;

import inspect.Nodeable;
import logging.Logs;
import script.exceptions.DivisionByZeroScriptException;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptElement;
import script.parsing.ScriptOperatorType;
import script.values.ScriptValue;
import script.values.ScriptValue_Numeric;

public class ScriptExecutable_EvalAssignMathExpression extends ScriptElement implements ScriptExecutable, Nodeable {
	private ScriptValue left, right;
	private ScriptOperatorType operation;

	public ScriptExecutable_EvalAssignMathExpression(Referenced ref, ScriptValue lhs, ScriptValue rhs, ScriptOperatorType operation) {
		super(ref);
		this.left = lhs;
		this.right = rhs;
		this.operation = operation;
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		assert Logs.openNode("'Evaluate and Assign' Executions", "Executing 'Evaluate and Assign' Expression");
		assert Logs.addNode(this);
		ScriptValue_Numeric left = (ScriptValue_Numeric) this.left.getValue();
		ScriptValue_Numeric right = (ScriptValue_Numeric) this.right.getValue();
		if ((this.operation == ScriptOperatorType.DIVIDE || this.operation == ScriptOperatorType.MODULUS) && right.getNumericValue().doubleValue() == 0.0d) {
			throw new DivisionByZeroScriptException(this);
		}
		ScriptValue returning = null;
		switch (this.operation) {
		case PLUSEQUALS:
			returning = left.setNumericValue(left.increment(this, right));
			break;
		case MINUSEQUALS:
			returning = left.setNumericValue(left.decrement(this, right));
			break;
		case MULTIPLYEQUALS:
			returning = left.setNumericValue(left.multiply(this, right));
			break;
		case DIVIDEEQUALS:
			returning = left.setNumericValue(left.divide(this, right));
			break;
		case MODULUSEQUALS:
			returning = left.setNumericValue(left.modulus(this, right));
			break;
		default:
			throw new AssertionError("Invalid default");
		}
		assert Logs.closeNode();
		return returning;
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("'Evaluate and Assign' mathematical expression (" + this.operation + ")");
		assert Logs.addSnapNode("Left side", this.left);
		assert Logs.addSnapNode("Right side", this.right);
		assert Logs.closeNode();
	}
}
