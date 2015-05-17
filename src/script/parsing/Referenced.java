package script.parsing;

import script.ScriptEnvironment;

public interface Referenced {
	public ScriptElement getDebugReference();

	public ScriptEnvironment getEnvironment();
}
