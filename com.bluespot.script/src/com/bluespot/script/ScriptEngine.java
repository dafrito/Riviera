package com.bluespot.script;

import java.util.concurrent.FutureTask;

import javax.tools.DiagnosticListener;

public interface ScriptEngine<S> {

	public void addDiagnosticListener(DiagnosticListener<S> listener);

	public FutureTask<?> getCompileTask(S data);

	public ScriptContext getScriptContext();

	public void removeDiagnosticListener(DiagnosticListener<S> listener);

	public void reset();
}
