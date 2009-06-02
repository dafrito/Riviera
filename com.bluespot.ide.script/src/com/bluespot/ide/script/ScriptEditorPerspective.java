package com.bluespot.ide.script;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import com.bluespot.ide.PerspectiveAction;
import com.bluespot.ide.editor.EditorPerspective;
import com.bluespot.swing.Dialogs;
import com.bluespot.swing.Dialogs.CancelledException;

public class ScriptEditorPerspective extends EditorPerspective {

	public static class RunAction extends ScriptEditorPerspectiveAction {

		public RunAction() {
			super("Run");
			this.putValue(Action.SHORT_DESCRIPTION, "Runs the current file.");
			this.putValue(Action.ACTION_COMMAND_KEY, "run");
			this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		}

		public void actionPerformed(final ActionEvent e) {
			try {
				this.getPerspective().evalSelectedEditor();
			} catch (final CancelledException ex) {
				// Do nothing.
			} catch (final IOException ex) {
				Dialogs.error("IOException while evaluating content", "Editor could not be read");
				ex.printStackTrace();
			}
		}
	}

	public static abstract class ScriptEditorPerspectiveAction extends PerspectiveAction {

		public ScriptEditorPerspectiveAction(final String name) {
			super(name);
		}

		@Override
		public ScriptEditorPerspective getPerspective() {
			return (ScriptEditorPerspective) super.getPerspective();
		}
	}

	protected ScriptEngineAdapter<?> scriptEngineAdapter;

	public ScriptEditorPerspective() {
		// Do nothing.
	}

	public ScriptEditorPerspective(final ScriptEngineAdapter<?> scriptEngineAdapter) {
		this.scriptEngineAdapter = scriptEngineAdapter;
	}

	public void evalSelectedEditor() throws CancelledException, IOException {
		this.getScriptEngineAdapter().getScriptEngine().reset();
		this.getScriptEngineAdapter().run(this.getSelectedEditor());
	}

	public ScriptEngineAdapter<?> getScriptEngineAdapter() {
		return this.scriptEngineAdapter;
	}

	@Override
	public void populateMenuBar(final JMenuBar menuBar) {
		super.populateMenuBar(menuBar);
		final JMenu scriptMenu = new JMenu("Script");
		scriptMenu.add(new RunAction());
		menuBar.add(scriptMenu);
	}

	public void setScriptEngineAdapter(final ScriptEngineAdapter<?> scriptEngineAdapter) {
		this.scriptEngineAdapter = scriptEngineAdapter;
	}

}
