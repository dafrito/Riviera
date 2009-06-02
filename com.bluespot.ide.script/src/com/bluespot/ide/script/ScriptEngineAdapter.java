package com.bluespot.ide.script;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.FutureTask;

import com.bluespot.ide.editor.Editor;
import com.bluespot.script.ScriptEngine;
import com.bluespot.swing.Dialogs.CancelledException;

public abstract class ScriptEngineAdapter<S> {

	private final ScriptEngine<S> engine;

	public ScriptEngineAdapter(final ScriptEngine<S> engine) {
		this.engine = engine;
	}

	public ScriptEngine<S> getScriptEngine() {
		return this.engine;
	}

	public FutureTask<?> run(final Editor editor) throws CancelledException, IOException {
		editor.save();
		return this.getScriptEngine().getCompileTask(this.convert(editor.getFile(), editor));
	}

	protected abstract S convert(File file, Editor editor) throws IOException;
}
