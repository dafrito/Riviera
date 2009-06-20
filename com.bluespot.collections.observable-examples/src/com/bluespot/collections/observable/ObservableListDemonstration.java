package com.bluespot.collections.observable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.demonstration.Demonstration;

/**
 * A very simple demo showing an {@link ObservableList} acting as a list model.
 * 
 * @author Aaron Faanes
 * 
 */
public final class ObservableListDemonstration extends Demonstration {

    private final List<String> strings = new ObservableList<String>();

    private final JList list = new JList((ListModel) this.strings);

    @Override
    public boolean initializeFrame(final JFrame frame) {
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 400);

        frame.getContentPane().add(new JScrollPane(this.list), BorderLayout.CENTER);

        final JPanel buttons = new JPanel();

        final List<String> thisStrings = this.strings;

        // Create our remove button
        final JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                thisStrings.remove(0);
                if (thisStrings.isEmpty()) {
                    removeButton.setEnabled(false);
                }
            }
        });
        removeButton.setEnabled(false);
        buttons.add(removeButton);

        // Create the add button
        final JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                thisStrings.add("Hello, world! This is element " + thisStrings.size());
                removeButton.setEnabled(true);
            }
        });
        buttons.add(addButton);

        frame.getContentPane().add(buttons, BorderLayout.SOUTH);
        return true;
    }

    /**
     * Creates a new {@link ObservableListDemonstration} using the
     * {@link Demonstration} framework.
     * 
     * @param args
     *            unused
     */
    public static void main(final String[] args) {
        Demonstration.launch(ObservableListDemonstration.class);
    }

}
