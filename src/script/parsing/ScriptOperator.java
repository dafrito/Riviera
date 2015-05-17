package script.parsing;

import inspect.Inspectable;
import script.parsing.tokens.RiffToken;
import script.parsing.tokens.RiffTokenVisitor;

@Inspectable
public class ScriptOperator extends ScriptElement implements RiffToken {

	private final ScriptOperatorType type;

	public ScriptOperator(final ScriptLine line, final ScriptOperatorType type) {
		super(line);
		this.type = type;
	}

	@Inspectable
	public ScriptOperatorType getType() {
		return this.type;
	}

	public String getName() {
		return this.getType().toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ScriptOperator)) {
			return false;
		}
		return this.getType().equals(((ScriptOperator) obj).getType());
	}

	@Override
	public int hashCode() {
		return this.getType().hashCode();
	}

	@Override
	public String toString() {
		return String.format("ScriptOperator[%s]", this.type.toString());
	}

	@Override
	public void accept(RiffTokenVisitor visitor) {
		visitor.visitOperator(this.getType());
	}
}
