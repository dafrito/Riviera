package script.operations;

import java.util.List;

import inspect.Nodeable;
import logging.Logs;
import script.exceptions.ScriptException;
import script.parsing.Referenced;
import script.parsing.ScriptElement;
import script.values.Returnable;
import script.values.ScriptValue;
import script.values.ScriptValue_Boolean;

public class ScriptExecutable_ForStatement extends ScriptElement implements ScriptExecutable, Returnable, Nodeable {
	private ScriptExecutable initializer, tester, repeater;
	private boolean shouldReturn = false;
	private ScriptValue returnValue;
	private List<ScriptExecutable> expressions;

	public ScriptExecutable_ForStatement(ScriptExecutable initializer, ScriptExecutable tester, ScriptExecutable repeater, List<ScriptExecutable> expressions) {
		super((Referenced) initializer);
		this.initializer = initializer;
		this.tester = tester;
		this.repeater = repeater;
		this.expressions = expressions;
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		assert Logs.openNode("For-Statement Executions", "Executing For-Statement");
		this.getEnvironment().advanceNestedStack();
		assert Logs.openNode("Initializing");
		this.initializer.execute();
		assert Logs.closeNode();
		while (((ScriptValue_Boolean) this.tester.execute().getValue()).getBooleanValue()) {
			assert Logs.openNode("Looping", "Looping iteration");
			this.getEnvironment().advanceNestedStack();
			for (ScriptExecutable exec : this.expressions) {
				exec.execute();
				if (exec instanceof Returnable && ((Returnable) exec).shouldReturn()) {
					this.returnValue = ((Returnable) exec).getReturnValue();
					this.shouldReturn = true;
					assert Logs.closeNode();
					return null;
				}
			}
			this.getEnvironment().retreatNestedStack();
			assert Logs.closeNode();
			assert Logs.openNode("Executing repeater");
			this.repeater.execute();
			assert Logs.closeNode();
		}
		this.getEnvironment().retreatNestedStack();
		assert Logs.closeNode();
		return null;
	}

	@Override
	public ScriptValue getReturnValue() throws ScriptException {
		if (this.returnValue != null) {
			this.returnValue = this.returnValue.getValue();
		}
		return this.returnValue;
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Script For-Statement");
		assert Logs.addSnapNode("Initializer", this.initializer);
		assert Logs.addSnapNode("Boolean expression", this.tester);
		assert Logs.addSnapNode("Repeating expression", this.repeater);
		assert Logs.addSnapNode("Expressions", this.expressions);
		assert Logs.closeNode();
	}

	// Returnable implementation
	@Override
	public boolean shouldReturn() {
		return this.shouldReturn;
	}
}
