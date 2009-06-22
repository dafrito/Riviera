/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.jdesktop.application;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

/**
 * An ActionMap class that defines cut/copy/paste/delete.
 * 
 * This class only exists to paper over limitations in the standard
 * JTextComponent cut/copy/paste/delete javax.swing.Actions. The standard
 * cut/copy Actions don't keep their enabled property in sync with having the
 * focus and (for copy) having a non-empty text selection. The standard paste
 * Action's enabled property doesn't stay in sync with the current contents of
 * the clipboard. The paste/copy/delete actions must also track the
 * JTextComponent editable property.
 * 
 * The new cut/copy/paste/delete are installed lazily, when a JTextComponent
 * gets the focus, and before any other focus-change related work is done. See
 * updateFocusOwner().
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 * @author Scott Violet (Scott.Violet@Sun.COM)
 */
class TextActions extends AbstractBean {
    private final ApplicationContext context;
    private final CaretListener textComponentCaretListener;
    private final PropertyChangeListener textComponentPCL;
    private final String markerActionKey = "TextActions.markerAction";
    private final javax.swing.Action markerAction;
    private boolean copyEnabled = false; // see setCopyEnabled
    private boolean cutEnabled = false; // see setCutEnabled
    private boolean pasteEnabled = false; // see setPasteEnabled
    private boolean deleteEnabled = false; // see setDeleteEnabled

    public TextActions(final ApplicationContext context) {
        this.context = context;
        this.markerAction = new javax.swing.AbstractAction() {
            /**
         * 
         */
            private static final long serialVersionUID = -7642793394555689456L;

            public void actionPerformed(final ActionEvent e) {
            }
        };
        this.textComponentCaretListener = new TextComponentCaretListener();
        this.textComponentPCL = new TextComponentPCL();
        this.getClipboard().addFlavorListener(new ClipboardListener());
    }

    private ApplicationContext getContext() {
        return this.context;
    }

    private JComponent getFocusOwner() {
        return this.getContext().getFocusOwner();
    }

    private Clipboard getClipboard() {
        return this.getContext().getClipboard();
    }

    /*
     * Called by the KeyboardFocus PropertyChangeListener in ApplicationContext,
     * before any other focus-change related work is done.
     */
    void updateFocusOwner(final JComponent oldOwner, final JComponent newOwner) {
        if (oldOwner instanceof JTextComponent) {
            final JTextComponent text = (JTextComponent) oldOwner;
            text.removeCaretListener(this.textComponentCaretListener);
            text.removePropertyChangeListener(this.textComponentPCL);
        }
        if (newOwner instanceof JTextComponent) {
            final JTextComponent text = (JTextComponent) newOwner;
            this.maybeInstallTextActions(text);
            this.updateTextActions(text);
            text.addCaretListener(this.textComponentCaretListener);
            text.addPropertyChangeListener(this.textComponentPCL);
        } else if (newOwner == null) {
            this.setCopyEnabled(false);
            this.setCutEnabled(false);
            this.setPasteEnabled(false);
            this.setDeleteEnabled(false);
        }
    }

    private final class ClipboardListener implements FlavorListener {
        public void flavorsChanged(final FlavorEvent e) {
            final JComponent c = TextActions.this.getFocusOwner();
            if (c instanceof JTextComponent) {
                TextActions.this.updateTextActions((JTextComponent) c);
            }
        }
    }

    private final class TextComponentCaretListener implements CaretListener {
        public void caretUpdate(final CaretEvent e) {
            TextActions.this.updateTextActions((JTextComponent) (e.getSource()));
        }
    }

    private final class TextComponentPCL implements PropertyChangeListener {
        public void propertyChange(final PropertyChangeEvent e) {
            final String propertyName = e.getPropertyName();
            if ((propertyName == null) || "editable".equals(propertyName)) {
                TextActions.this.updateTextActions((JTextComponent) (e.getSource()));
            }
        }
    }

    private void updateTextActions(final JTextComponent text) {
        final Caret caret = text.getCaret();
        final boolean selection = (caret.getDot() != caret.getMark());
        final boolean editable = text.isEditable();
        final boolean data = this.getClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor);
        this.setCopyEnabled(selection);
        this.setCutEnabled(editable && selection);
        this.setDeleteEnabled(editable && selection);
        this.setPasteEnabled(editable && data);
    }

    // TBD: what if text.getActionMap is null, or if it's parent isn't the
    // UI-installed actionMap
    private void maybeInstallTextActions(final JTextComponent text) {
        final ActionMap actionMap = text.getActionMap();
        if (actionMap.get(this.markerActionKey) == null) {
            actionMap.put(this.markerActionKey, this.markerAction);
            final ActionMap textActions = this.getContext().getActionMap(this.getClass(), this);
            for (final Object key : textActions.keys()) {
                actionMap.put(key, textActions.get(key));
            }
        }
    }

    /*
     * This method lifted from JTextComponent.java
     */
    private int getCurrentEventModifiers() {
        int modifiers = 0;
        final AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        return modifiers;
    }

    private void invokeTextAction(final JTextComponent text, final String actionName) {
        final ActionMap actionMap = text.getActionMap().getParent();
        final long eventTime = EventQueue.getMostRecentEventTime();
        final int eventMods = this.getCurrentEventModifiers();
        final ActionEvent actionEvent = new ActionEvent(text, ActionEvent.ACTION_PERFORMED, actionName, eventTime,
                eventMods);
        actionMap.get(actionName).actionPerformed(actionEvent);
    }

    @Action(enabledProperty = "cutEnabled")
    public void cut(final ActionEvent e) {
        final Object src = e.getSource();
        if (src instanceof JTextComponent) {
            this.invokeTextAction((JTextComponent) src, "cut");
        }
    }

    public boolean isCutEnabled() {
        return this.cutEnabled;
    }

    public void setCutEnabled(final boolean cutEnabled) {
        final boolean oldValue = this.cutEnabled;
        this.cutEnabled = cutEnabled;
        this.firePropertyChange("cutEnabled", oldValue, this.cutEnabled);
    }

    @Action(enabledProperty = "copyEnabled")
    public void copy(final ActionEvent e) {
        final Object src = e.getSource();
        if (src instanceof JTextComponent) {
            this.invokeTextAction((JTextComponent) src, "copy");
        }
    }

    public boolean isCopyEnabled() {
        return this.copyEnabled;
    }

    public void setCopyEnabled(final boolean copyEnabled) {
        final boolean oldValue = this.copyEnabled;
        this.copyEnabled = copyEnabled;
        this.firePropertyChange("copyEnabled", oldValue, this.copyEnabled);
    }

    @Action(enabledProperty = "pasteEnabled")
    public void paste(final ActionEvent e) {
        final Object src = e.getSource();
        if (src instanceof JTextComponent) {
            this.invokeTextAction((JTextComponent) src, "paste");
        }
    }

    public boolean isPasteEnabled() {
        return this.pasteEnabled;
    }

    public void setPasteEnabled(final boolean pasteEnabled) {
        final boolean oldValue = this.pasteEnabled;
        this.pasteEnabled = pasteEnabled;
        this.firePropertyChange("pasteEnabled", oldValue, this.pasteEnabled);
    }

    @Action(enabledProperty = "deleteEnabled")
    public void delete(final ActionEvent e) {
        final Object src = e.getSource();
        if (src instanceof JTextComponent) {
            /*
             * The deleteNextCharAction is bound to the delete key in text
             * components. The name appears to be a misnomer, however it's
             * really a compromise. Calling the method by a more accurate name,
             * "IfASelectionExistsThenDeleteItOtherwiseDeleteTheNextCharacter"
             * would be rather unwieldy.
             */
            this.invokeTextAction((JTextComponent) src, DefaultEditorKit.deleteNextCharAction);
        }
    }

    public boolean isDeleteEnabled() {
        return this.deleteEnabled;
    }

    public void setDeleteEnabled(final boolean deleteEnabled) {
        final boolean oldValue = this.deleteEnabled;
        this.deleteEnabled = deleteEnabled;
        this.firePropertyChange("deleteEnabled", oldValue, this.deleteEnabled);
    }
}
