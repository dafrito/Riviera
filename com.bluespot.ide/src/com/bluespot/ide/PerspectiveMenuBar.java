package com.bluespot.ide;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PerspectiveMenuBar extends PerspectiveComponent {

    private final JMenuBar menuBar = new JMenuBar();

    public PerspectiveMenuBar(PerspectiveManager manager) {
        super(manager);
        this.getManager().getPerspectives().addListDataListener(new ListDataListener() {
                public void contentsChanged(ListDataEvent e) {
                    PerspectiveMenuBar.this.refresh();
                }

                public void intervalAdded(ListDataEvent e) {
                    PerspectiveMenuBar.this.refresh();
                }

                public void intervalRemoved(ListDataEvent e) {
                    PerspectiveMenuBar.this.refresh();
                }
            });
        this.refresh();
    }

    public JMenuBar getMenuBar() {
        return this.menuBar;
    }

    public void refresh() {
        this.getMenuBar().removeAll();
        if (this.getManager().getCurrentPerspective() != null) {
            this.getManager().getCurrentPerspective().populateMenuBar(this.getMenuBar());
        }
        this.populateMenuBar();
        this.getMenuBar().repaint();
    }

    @Override
    protected void enterPerspective(Perspective perspective) {
        this.refresh();
    }

    public void populateMenuBar() {
        this.menuBar.add(Box.createHorizontalGlue());
        ButtonGroup group = new ButtonGroup();
        for (Perspective perspective : this.getManager().getPerspectives()) {
            AbstractButton button = this.createMenuItem(perspective);
            group.add(button);
            this.menuBar.add(button);
            this.menuBar.add(Box.createRigidArea(new Dimension(5, 0)));
        }
    }

    protected AbstractButton createMenuItem(final Perspective perspective) {
        JRadioButton menuItem = new JRadioButton(perspective.getName());
        if (perspective.equals(this.getManager().getCurrentPerspective())) {
            menuItem.setSelected(true);
        }
        menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    PerspectiveMenuBar.this.getManager().showPerspective(perspective);
                }
            });
        return menuItem;
    }

}
