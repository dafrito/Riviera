package com.bluespot.examples.collections.observable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.demonstration.BorderLayoutDemonstration;
import com.bluespot.demonstration.Demonstration;

/**
 * Demonstrates cell rendering using an {@link ObservableList}.
 * 
 * @author Aaron Faanes
 * 
 */
public final class CellRendererDemonstration extends BorderLayoutDemonstration {

    private static class MyCellRenderer extends JLabel implements ListCellRenderer {

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

    private ObservableList<String> strings;

    @Override
    protected void preInitialize(final JFrame frame) {
        frame.setPreferredSize(new Dimension(220, 330));
        this.strings = new ObservableList<String>();
        this.populateList(this.strings);
    }

    @Override
    protected JComponent newCenterPane() {
        final JList list = new JList(this.strings);
        list.setCellRenderer(new MyCellRenderer());
        return new JScrollPane(list);
    }

    @Override
    protected JComponent newSouthPane() {
        final JPanel buttons = new JPanel();

        final JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                CellRendererDemonstration.this.strings.remove(0);
                removeButton.setEnabled(!CellRendererDemonstration.this.strings.isEmpty());
            }
        });
        removeButton.setEnabled(false);

        final JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                CellRendererDemonstration.this.strings.add("Hello, world! This is element "
                        + CellRendererDemonstration.this.strings.size());
                removeButton.setEnabled(true);
            }
        });

        buttons.add(addButton);
        buttons.add(removeButton);

        return buttons;
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