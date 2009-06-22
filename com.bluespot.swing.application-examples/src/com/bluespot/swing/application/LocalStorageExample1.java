/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractListModel;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.ResourceMap;

/**
 * A simple demonstration of the {@code LocalStorage} class: loads and saves a
 * {@code LinkedHashMap} (a {@code HashMap} whose entries have a stable order).
 * To try it, add some entries to the Map by pressing the "Add Random Entry"
 * button or by entering key/value strings in the corresponding text fields.
 * Then save the Map (to a file), clear, and load the saved Map. with the
 * corresponding buttons.
 * <p>
 * The map is saved with the {@code LocalStorage} {@link LocalStorage#save save}
 * method like this:
 * 
 * <pre>
 * LinkedHashMap&lt;String, String&gt; map = listModel.getMap();
 * ApplicationContext.getInstance().getLocalStorage().save(map, &quot;map.xml&quot;);
 * </pre>
 * 
 * And loaded with the {@code LocalStorage} {@link LocalStorage#load load}
 * method like this:
 * 
 * <pre>
 * Object map = ApplicationContext.getInstance().getLocalStorage().load(&quot;map.xml&quot;);
 * listModel.setMap((LinkedHashMap&lt;String, String&gt;) map);
 * </pre>
 * 
 * The {@code LocalStorage.save/load} methods can be applied to anything
 * supported by Java Beans Persistence, i.e. any Java Bean as well as most of
 * the primitive and utility Java classes. The {@code LocalStorage.save} method
 * is implemented with the {@link java.beans.XMLEncoder XMLEncoder} class and
 * the {@code LocalStorage.load} method with the {@link java.beans.XMLDecoder
 * XMLDecoder} class. Take a look at the contents of {@code "map.xml"} by cut
 * and pasting the complete pathname from the bottom of the GUI into your
 * favorite text editor.
 * 
 * @see ApplicationContext#getLocalStorage
 * @see LocalStorage#load
 * @see LocalStorage#save
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class LocalStorageExample1 extends Application {
    private final static String file = "map.xml";
    private final Random random = new Random();
    private JTextField keyField = null;
    private JTextField valueField = null;
    private JTextField messageField = null;
    private MapListModel listModel = null;

    @Action
    public void addKeyValueEntry() {
        final String key = this.keyField.getText().trim();
        final String value = this.valueField.getText().trim();
        this.listModel.put(key, value);
    }

    @Action
    public void addRandomEntry() {
        final String key = this.keyField.getText().trim() + this.random.nextInt(10000);
        final String value = this.valueField.getText().trim() + this.random.nextInt(10000);
        this.listModel.put(key, value);
    }

    @Action
    public void clearMap() {
        this.listModel.clear();
    }

    @Action
    public void loadMap() throws IOException {
        final Object map = this.getContext().getLocalStorage().load(LocalStorageExample1.file);
        this.listModel.setMap((LinkedHashMap<String, String>) map);
        this.showFileMessage("loadedFile", LocalStorageExample1.file);
    }

    @Action
    public void saveMap() throws IOException {
        final LinkedHashMap<String, String> map = this.listModel.getMap();
        this.getContext().getLocalStorage().save(map, LocalStorageExample1.file);
        this.showFileMessage("savedFile", LocalStorageExample1.file);
    }

    private void showFileMessage(final String messageKey, final String file) {
        final File dir = this.getContext().getLocalStorage().getDirectory();
        final File path = (dir == null) ? new File(file) : new File(dir, file);
        final ResourceMap resourceMap = this.getContext().getResourceMap();
        final String message = resourceMap.getString(messageKey, path.toString());
        this.messageField.setText(message);
    }

    @Override
    protected void startup() {
        this.keyField = new JTextField(" Key ", 16);
        this.valueField = new JTextField(" Value ", 16);
        this.messageField = new JTextField();
        this.messageField.setEditable(false);
        final JButton addEntryButton = new JButton();
        final JButton saveButton = new JButton();
        final JButton loadButton = new JButton();
        final JButton clearButton = new JButton();

        /*
         * Create JScrollPane/JList that displays listModel
         */
        this.listModel = new MapListModel();
        final JList mapList = new JList(this.listModel);
        mapList.setPrototypeCellValue("Hello = World");
        mapList.setVisibleRowCount(12);
        final Border border = new EmptyBorder(2, 4, 2, 4);
        final JScrollPane scrollPane = new JScrollPane(mapList);
        scrollPane.setBorder(border);

        /*
         * Lookup up the Actions for this class/object in the
         * ApplicationContext, and bind them to the GUI controls.
         */
        final ActionMap actionMap = this.getContext().getActionMap();
        addEntryButton.setAction(actionMap.get("addRandomEntry"));
        this.keyField.setAction(actionMap.get("addKeyValueEntry"));
        this.valueField.setAction(actionMap.get("addKeyValueEntry"));
        saveButton.setAction(actionMap.get("saveMap"));
        loadButton.setAction(actionMap.get("loadMap"));
        clearButton.setAction(actionMap.get("clearMap"));

        final JPanel northPanel = new JPanel();
        northPanel.add(this.keyField);
        northPanel.add(this.valueField);
        northPanel.add(addEntryButton);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);

        final JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        final JFrame appFrame = new JFrame(this.getClass().getSimpleName());
        appFrame.add(centerPanel, BorderLayout.CENTER);
        appFrame.add(northPanel, BorderLayout.NORTH);
        appFrame.add(this.messageField, BorderLayout.SOUTH);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.pack();
        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);
    }

    /*
     * A ListModel that encapsulates a LinkedHashMap<String, String> The value
     * of each ListModel element is just a string: "key = value".
     */
    private static class MapListModel extends AbstractListModel {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private final LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        private List<String> keys = null;

        private List<String> getKeys() {
            if (this.keys == null) {
                this.keys = new ArrayList<String>(this.map.keySet());
            }
            return this.keys;
        }

        public void put(final String key, final String value) {
            int index = -1;
            if (this.map.containsKey(key)) {
                index = this.getKeys().indexOf(key);
            } else {
                index = this.map.size();
                this.keys = null;
            }
            this.map.put(key, value);
            this.fireContentsChanged(this, index, index);
        }

        public void clear() {
            if (this.map.size() > 0) {
                final int lastIndex = this.map.size() - 1;
                this.map.clear();
                this.keys = null;
                this.fireIntervalRemoved(this, 0, lastIndex);
            }
        }

        public LinkedHashMap<String, String> getMap() {
            return new LinkedHashMap<String, String>(this.map);
        }

        public void setMap(final LinkedHashMap<String, String> newMap) {
            final int oldLastIndex = Math.max(this.map.size() - 1, 0);
            this.map.clear();
            this.map.putAll(newMap);
            final int newLastIndex = Math.max(this.map.size() - 1, 0);
            this.fireContentsChanged(this, 0, Math.max(oldLastIndex, newLastIndex));
        }

        public int getSize() {
            return this.map.size();
        }

        public Object getElementAt(final int index) {
            final String key = this.getKeys().get(index);
            return key + " = " + this.map.get(key);
        }
    }

    public static void main(final String[] args) {
        Application.launch(LocalStorageExample1.class, args);
    }
}
