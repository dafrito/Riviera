package com.bluespot.graphics.boxfan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.bluespot.demonstration.Demonstration;

public class FanDemonstration extends Demonstration {
    enum Speed {
        OFF("Off", 0), LOW("Low", 1), MEDIUM("Medium", 15), HIGH("High", 35);

        private final String prettyName;
        private final int speed;

        private Speed(final String prettyName, final int speed) {
            this.prettyName = prettyName;
            this.speed = speed;
        }

        public int getSpeed() {
            return this.speed;
        }

        public String getPrettyName() {
            return this.prettyName;
        }

        public void activate(final Rotation model) {
            model.setSpeed(this.getSpeed());
        }

        public Action newAction(final Rotation model) {
            return new AbstractAction(this.getPrettyName()) {
                public void actionPerformed(final ActionEvent e) {
                    Speed.this.activate(model);
                }
            };
        }
    }

    private final Rotation model = new Rotation();

    @Override
    protected JComponent newContentPane() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 300));

        final ButtonGroup group = new ButtonGroup();

        final JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        panel.add(buttonPanel, BorderLayout.LINE_START);

        for (final Speed speed : Speed.values()) {
            final Action speedAction = speed.newAction(this.model);
            final JButton button = new JButton(speedAction);
            group.add(button);
            buttonPanel.add(button);
        }

        final FanPanel fan = new FanPanel(.8d, 5);
        fan.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.RED));

        panel.add(fan, BorderLayout.CENTER);

        return panel;
    }

    public static void main(final String[] args) {
        Demonstration.launch(FanDemonstration.class);
    }
}
