package com.bluespot.ide;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * Provides a graphical interface for a {@link PerspectiveManager}.
 * 
 * @author Aaron Faanes
 *
 */
public class IDE {

    private final PerspectiveManager manager;
    
    protected final PerspectivesPanel panel = new PerspectivesPanel(this.getManager());
    protected final PerspectiveMenuBar menuBar = new PerspectiveMenuBar(this.getManager());
    
    public IDE() {
        this(new PerspectiveManager());
    }
    
    public IDE(PerspectiveManager manager) {
        this.manager = manager;
    }
    
    public JFrame populate(JFrame frame) {
        frame.setContentPane(this.getPanel());
        frame.setJMenuBar(this.getMenuBar());
        Perspective.exitAction.attachTo(frame);
        return frame;
    }
    
    public PerspectiveManager getManager() {
        return this.manager;
    }

    public void add(Perspective perspective) {
        this.getManager().addPerspective(perspective);
    }

    public JPanel getPanel() {
        return this.panel.getPanel();
    }

    
    public JMenuBar getMenuBar() {
        return this.menuBar.getMenuBar();
    }

}
