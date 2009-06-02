package com.bluespot.ide.script;

import com.bluespot.ide.PerspectiveAction;
import com.bluespot.ide.editor.EditorPerspective;
import com.bluespot.swing.Dialogs;
import com.bluespot.swing.Dialogs.CancelledException;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.io.IOException;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;


public class ScriptEditorPerspective extends EditorPerspective {

    protected ScriptEngineAdapter<?> scriptEngineAdapter;

    public ScriptEditorPerspective(ScriptEngineAdapter<?> scriptEngineAdapter) {
        this.scriptEngineAdapter = scriptEngineAdapter;
    }

    public ScriptEditorPerspective() {
        // Do nothing.
    }

    public ScriptEngineAdapter<?> getScriptEngineAdapter() {
        return this.scriptEngineAdapter;
    }


    public void setScriptEngineAdapter(ScriptEngineAdapter<?> scriptEngineAdapter) {
        this.scriptEngineAdapter = scriptEngineAdapter;
    }

    public void evalSelectedEditor() throws CancelledException, IOException {
        this.getScriptEngineAdapter().getScriptEngine().reset();
        this.getScriptEngineAdapter().run(this.getSelectedEditor());
    }

    @Override
    public void populateMenuBar(JMenuBar menuBar) {
        super.populateMenuBar(menuBar);
        JMenu scriptMenu = new JMenu("Script");
        scriptMenu.add(new RunAction());
        menuBar.add(scriptMenu);
    }

    public static abstract class ScriptEditorPerspectiveAction extends PerspectiveAction {

        public ScriptEditorPerspectiveAction(String name) {
            super(name);
        }

        @Override
        public ScriptEditorPerspective getPerspective() {
            return (ScriptEditorPerspective)super.getPerspective();
        }
    }

    public static class RunAction extends ScriptEditorPerspectiveAction {

        public RunAction() {
            super("Run");
            this.putValue(Action.SHORT_DESCRIPTION, "Runs the current file.");
            this.putValue(Action.ACTION_COMMAND_KEY, "run");
            this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                this.getPerspective().evalSelectedEditor();
            } catch (CancelledException ex) {
                // Do nothing.
            } catch (IOException ex) {
                Dialogs.error("IOException while evaluating content", "Editor could not be read");
                ex.printStackTrace();
            }
        }
    }

}
