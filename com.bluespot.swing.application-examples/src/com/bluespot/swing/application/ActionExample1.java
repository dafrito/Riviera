/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.bluespot.swing.application;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

/**
 * {@code @Action} basics.
 * <p>
 * A trivial {@code @Action} example: the buttons set/clear the Frame's title:
 * 
 * <pre>
 * public class ActionExample1 extends Application {
 *     &#064;Action
 *     public void setTitle() {
 *         appFrame.setTitle(textField.getText());
 *     }
 * 
 *     &#064;Action
 *     public void clearTitle() {
 *         appFrame.setTitle(&quot;&quot;);
 *     }
 *     // ...
 * }
 * </pre>
 * 
 * The only wrinkle worth noting is that the Action objects we've created are
 * going to call the methods on <i>this</i> object. So when we lookup the
 * ActionMap for this class, we have to pass along the {@code ActionExample1}
 * instance as well:
 * 
 * <pre>
 * ApplicationContext ac = ApplicationContext.getInstance();
 * ActionMap actionMap = ac.getActionMap(getClass(), &lt;i&gt;this&lt;/i&gt;);
 * setTitleButton.setAction(actionMap.get(&quot;setTitle&quot;));
 * clearTitleButton.setAction(actionMap.get(&quot;clearTitle&quot;));
 * </pre>
 * 
 * Since our {@code @Actions} have been defined in the {@code Application}
 * subclass itself, we can use the no-argument version of {@code getActionMap()}
 * , which returns the {@code ActionMap} for the application:
 * 
 * <pre>
 * ApplicationContext ac = ApplicationContext.getInstance();
 * ActionMap actionMap = ac.getActionMap();
 * setTitleButton.setAction(actionMap.get(&quot;setTitle&quot;));
 * clearTitleButton.setAction(actionMap.get(&quot;clearTitle&quot;));
 * </pre>
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class ActionExample1 extends Application {
    private JFrame appFrame = null;
    private JTextField textField = null;

    @Action
    public void setTitle() {
        this.appFrame.setTitle(this.textField.getText());
    }

    @Action
    public void clearTitle() {
        this.appFrame.setTitle("");
    }

    @Override
    protected void startup() {
        this.appFrame = new JFrame("");
        this.textField = new JTextField("<Enter the window title here>");
        this.textField.setFont(new Font("LucidSans", Font.PLAIN, 32));
        final JButton clearTitleButton = new JButton();
        final JButton setTitleButton = new JButton();
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(setTitleButton);
        buttonPanel.add(clearTitleButton);
        this.appFrame.add(this.textField, BorderLayout.CENTER);
        this.appFrame.add(buttonPanel, BorderLayout.SOUTH);

        /*
         * Lookup up the Actions for this Application and bind them to the GUI
         * controls.
         */
        final ActionMap actionMap = this.getContext().getActionMap();
        setTitleButton.setAction(actionMap.get("setTitle"));
        this.textField.setAction(actionMap.get("setTitle"));
        clearTitleButton.setAction(actionMap.get("clearTitle"));

        this.appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.appFrame.pack();
        this.appFrame.setLocationRelativeTo(null);
        this.appFrame.setVisible(true);
    }

    public static void main(final String[] args) {
        Application.launch(ActionExample1.class, args);
    }
}
