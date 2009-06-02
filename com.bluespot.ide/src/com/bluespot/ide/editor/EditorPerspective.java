package com.bluespot.ide.editor;

import com.bluespot.ide.AbstractPerspective;
import com.bluespot.ide.PerspectiveAction;
import com.bluespot.swing.Dialogs;
import com.bluespot.swing.TabView;
import com.bluespot.swing.Dialogs.CancelledException;
import com.bluespot.swing.list.ProxiedListModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;


public class EditorPerspective extends AbstractPerspective {

    // TODO getEditorForFile(File file)

    protected final JTabbedPane editorsPane = new JTabbedPane();
    protected final ProxiedListModel<Editor> editors = new ProxiedListModel<Editor>();

    public EditorPerspective() {
        super("Edit");
        new TabView<JScrollPane, Editor>(this.editorsPane, this.editors) {

            @Override
            public JScrollPane createComponent(Editor data) {
                return new JScrollPane(data);
            }

            @Override
            protected Component getNameSource(JScrollPane childComponent) {
                return childComponent.getViewport().getView();
            }
        };
    }

    public JComponent getComponent() {
        return this.editorsPane;
    }

    @Override
    public void populateMenuBar(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        fileMenu.add(new NewEditorAction());
        fileMenu.add(new OpenEditorAction());
        fileMenu.addSeparator();
        fileMenu.add(new CloseEditorAction());
        fileMenu.addSeparator();
        fileMenu.add(new SaveEditorAction());
        fileMenu.add(new SaveAsEditorAction());
        fileMenu.addSeparator();
        fileMenu.add(new ExitAction());

        menuBar.add(fileMenu);
    }

    public Editor getSelectedEditor() {
        return this.editors.get(this.editorsPane.getSelectedIndex());
    }

    public void setSelectedEditor(Editor editor) {
        int index = this.editors.indexOf(editor);
        if (index < 0) {
            throw new IllegalArgumentException("Editor is not in this perspective");
        }
        this.editorsPane.setSelectedIndex(index);
    }

    protected Editor searchForExistingEditor(Editor editor) {
        File file = editor.getFile();
        if (file == null)
            return null;
        for (Editor candidate : this.editors) {
            File candidateFile = candidate.getFile();
            if (candidateFile == null)
                continue;
            if (candidateFile.equals(file))
                return candidate;
        }
        return null;
    }

    public void add(Editor editor) {
        Editor existingEditor = this.searchForExistingEditor(editor);
        if (existingEditor != null) {
            this.setSelectedEditor(existingEditor);
            return;
        }
        this.editors.add(editor);
        this.setSelectedEditor(editor);
    }

    public void remove(Editor editor) {
        this.editors.remove(editor);
    }

    @Override
    public boolean isReadyForClose() {
        for (Editor editor : this.editors) {
            try {
                if (!editor.isDirty())
                    continue;
                int choice = JOptionPane.showConfirmDialog(null, editor.getName() + " has unsaved changes. Save?");
                switch (choice) {
                    case JOptionPane.YES_OPTION:
                        editor.save();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    default:
                        return false;
                }
            } catch (IOException e) {
                // TODO Spew on this.
                return false;
            } catch (CancelledException e) {
                return false;
            }
        }
        return true;
    }

    public static abstract class EditorPerspectiveAction extends PerspectiveAction {

        public EditorPerspectiveAction(String name) {
            super(name);
        }

        @Override
        public EditorPerspective getPerspective() {
            return (EditorPerspective)super.getPerspective();
        }

    }

    public static class CloseEditorAction extends EditorPerspectiveAction {

        public CloseEditorAction() {
            super("Close");
            this.putValue(Action.SHORT_DESCRIPTION, "Closes this file.");
            this.putValue(Action.ACTION_COMMAND_KEY, "closeEditor");
            this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                Editor editor = this.getPerspective().getSelectedEditor();
                if (editor.isDirty()) {
                    int choice = JOptionPane.showConfirmDialog(null, "The file has unsaved changes. Save?",
                                                               "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (choice) {
                        case JOptionPane.YES_OPTION:
                            editor.save();
                            break;
                        case JOptionPane.NO_OPTION:
                            break;
                        default:
                            return;
                    }
                }
                this.getPerspective().remove(editor);
            } catch (CancelledException ex) {
                // Do nothing.
            } catch (IOException ex) {
                Dialogs.error("The file could not be saved. (Error: " + ex.getLocalizedMessage() + ")",
                              "File I/O Exception");
            } catch (Throwable exx) {
                exx.printStackTrace();
            }
        }

    }

    public static class NewEditorAction extends EditorPerspectiveAction {

        public NewEditorAction() {
            super("New");
            this.putValue(Action.SHORT_DESCRIPTION, "Creates a new, blank editor.");
            this.putValue(Action.ACTION_COMMAND_KEY, "newEditor");
            this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            this.getPerspective().add(new Editor());
        }

    }

    public static class OpenEditorAction extends EditorPerspectiveAction {

        public OpenEditorAction() {
            super("Open...");
            this.putValue(Action.SHORT_DESCRIPTION, "Opens a file for editing.");
            this.putValue(Action.ACTION_COMMAND_KEY, "openEditor");
            this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                this.getPerspective().add(Editor.openFile(Dialogs.openFile()));
            } catch (CancelledException ex) {
                // Do nothing.
            } catch (IOException ex) {
                Dialogs.error("The file could not be read. (Error: " + ex.getLocalizedMessage() + ")",
                              "File I/O Exception");
            }
        }

    }

    public static class SaveAsEditorAction extends EditorPerspectiveAction {

        public SaveAsEditorAction() {
            super("Save As...");
            this.putValue(Action.SHORT_DESCRIPTION, "Saves this file at the given location.");
            this.putValue(Action.ACTION_COMMAND_KEY, "saveAsEditor");
            this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                this.getPerspective().getSelectedEditor().save(Dialogs.saveFile());
            } catch (CancelledException ex) {
                // Do nothing.
            } catch (IOException ex) {
                Dialogs.error("The file could not be saved. (Error: " + ex.getLocalizedMessage() + ")",
                              "File I/O Exception");
            }
        }

    }

    public static class SaveEditorAction extends EditorPerspectiveAction {

        public SaveEditorAction() {
            super("Save");
            this.putValue(Action.SHORT_DESCRIPTION, "Saves this file.");
            this.putValue(Action.ACTION_COMMAND_KEY, "saveEditor");
            this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                this.getPerspective().getSelectedEditor().save();
            } catch (CancelledException ex) {
                // Do nothing.
            } catch (IOException ex) {
                Dialogs.error("The file could not be saved. (Error: " + ex.getLocalizedMessage() + ")",
                              "File I/O Exception");
            }
        }

    }

}
