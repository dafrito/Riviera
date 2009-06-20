package com.bluespot.collections.observable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.demonstration.Demonstration;

/**
 * Demonstrates cell rendering using an {@link ObservableList}.
 * 
 * @author Aaron Faanes
 * 
 */
public final class CellRendererDemonstration extends Demonstration {

    private static class MyCellRenderer extends JLabel implements ListCellRenderer {

        public MyCellRenderer() {
            // Default constructor, defined explicitly to remove
            // synthetic-access problems
        }

        private static final long serialVersionUID = 5217981785927203654L;

        public Component getListCellRendererComponent(final JList sourceList, final Object value, final int index,
                final boolean isSelected, final boolean cellHasFocus) {
            this.setOpaque(true);
            this.setText(String.valueOf(value.toString().length()));
            if (isSelected) {
                this.setForeground(Color.WHITE);
                this.setBackground(Color.BLUE);
                this.setText(value.toString());
            } else {
                this.setForeground(Color.BLACK);
                this.setBackground(Color.WHITE);
            }
            return this;
        }

    }

    private final JList list = new JList();

    @Override
    public boolean initializeFrame(final JFrame frame) {

        this.list.setCellRenderer(new MyCellRenderer());

        final ObservableList<String> strings = new ObservableList<String>();
        this.list.setModel(strings);

        this.populateList(strings);

        frame.setLayout(new BorderLayout());
        frame.setSize(400, 400);
        frame.getContentPane().add(new JScrollPane(this.list), BorderLayout.CENTER);

        final JPanel panel = new JPanel();

        final JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                strings.remove(0);
                removeButton.setEnabled(!strings.isEmpty());
            }
        });
        removeButton.setEnabled(false);
        panel.add(removeButton);

        final JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                strings.add("Hello, world! This is element " + strings.size());
                removeButton.setEnabled(true);
            }
        });
        panel.add(addButton);

        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        return true;
    }

    private void populateList(final List<String> targetList) {
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < alphabet.length(); i++) {
            targetList.add(String.valueOf(alphabet.charAt(i)));
        }
    }

    /**
     * Creates a new {@link CellRendererDemonstration} using
     * {@link Demonstration}.
     * 
     * @param args
     *            unused
     */
    public static void main(final String[] args) {
        Demonstration.launch(CellRendererDemonstration.class);
    }

}