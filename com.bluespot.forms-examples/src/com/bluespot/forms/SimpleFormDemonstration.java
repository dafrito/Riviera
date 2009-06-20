package com.bluespot.forms;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.bluespot.demonstration.Demonstration;
import com.bluespot.swing.Components;
import com.bluespot.swing.GroupLayoutBuilder;

/**
 * Demonstrates the forms framework.
 * 
 * @author Aaron Faanes
 * 
 */
public final class SimpleFormDemonstration extends Demonstration {

    @Override
    protected void initializeFrame(final JFrame frame) {
        Components.LookAndFeel.SYSTEM.activate();
        final GroupLayoutBuilder form = new GroupLayoutBuilder();
        form.label("<html>Type in a name of a program, folder, document, or <br/>Internet resource, and Windows will open it for you.</html>");
        form.field("Name");
        form.field("Lower Bound");
        form.field("Upper Bound");
        form.button("OK");
        final JComponent comp = form.build();
        frame.setContentPane(comp);
        frame.pack();

    }

    /**
     * Runs a {@link SimpleFormDemonstration} using {@link Demonstration}.
     * 
     * @param args
     *            unused
     */
    public static void main(final String[] args) {
        Demonstration.launch(SimpleFormDemonstration.class);
    }

}
