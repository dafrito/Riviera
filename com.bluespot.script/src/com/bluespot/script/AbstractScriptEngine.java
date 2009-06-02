package com.bluespot.script;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;

import com.bluespot.dispatcher.SimpleDispatcher;

public abstract class AbstractScriptEngine<S> implements ScriptEngine<S> {

	private final SimpleDispatcher<Diagnostic<S>, DiagnosticListener<S>> dispatcher = new SimpleDispatcher<Diagnostic<S>, DiagnosticListener<S>>() {

		public void dispatch(Diagnostic<S> value, DiagnosticListener<S> listener) {
			listener.report(value);
		}

	};

	public void addDiagnosticListener(DiagnosticListener<S> listener) {
		this.dispatcher.addListener(listener);
	}

	public void removeDiagnosticListener(DiagnosticListener<S> listener) {
		this.dispatcher.removeListener(listener);
	}

	protected void report(Diagnostic<S> diagnostic) {
		this.dispatcher.dispatch(diagnostic);
	}

}
