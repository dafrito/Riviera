package com.bluespot.script;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;

public abstract class AbstractScriptEngine<S> implements ScriptEngine<S> {

	private final List<DiagnosticListener<S>> listeners = new CopyOnWriteArrayList<DiagnosticListener<S>>();

	public void addDiagnosticListener(final DiagnosticListener<S> listener) {
		this.listeners.add(listener);
	}

	public void removeDiagnosticListener(final DiagnosticListener<S> listener) {
		this.listeners.remove(listener);
	}

	protected void report(final Diagnostic<S> diagnostic) {
		for (final DiagnosticListener<S> listener : this.listeners) {
			listener.report(diagnostic);
		}
	}

}
