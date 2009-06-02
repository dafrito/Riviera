package com.bluespot.ide;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

public interface Perspective {

    JComponent getComponent();
    String getName();
    
    boolean isReadyForClose();
    void close();
    
    void populateMenuBar(JMenuBar menuBar);
    
    public static class ExitAction extends AbstractAction {

        public ExitAction() {
            this.putValue(Action.NAME, "Exit");
            this.putValue(Action.SHORT_DESCRIPTION, "Exits Riviera");
            this.putValue(Action.ACTION_COMMAND_KEY, "exit");
            this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            if(PerspectiveManager.getCurrentManager().closeAll()) {
                System.exit(0);
            }
        }
        
        public void attachTo(JFrame frame) {
            // Make sure exiting is intercepted by the perspective manager.
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            
            frame.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    ExitAction.this.actionPerformed(null);
                }
                
            });
        }

    }
    
    public static final ExitAction exitAction = new ExitAction();
    
}
