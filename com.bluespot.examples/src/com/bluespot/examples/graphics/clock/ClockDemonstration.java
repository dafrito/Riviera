package com.bluespot.examples.graphics.clock;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.bluespot.demonstration.Demonstration;

/**
 * Displays an animated clock. The user is able to set the time, and the
 * animation immediately changes to reflect the new starting time.
 * <p>
 * There are no numbers, nor are the fields themselves updated. The minute and
 * hour hands do gradually move according to the number of seconds, and
 * antialiasing is used to make the clock appear less rigid.
 */
public final class ClockDemonstration extends Demonstration {

    /**
     * The width, in characters, of the time-specifying fields.
     */
    private static final int FIELD_WIDTH = 10;

    private final JLabel hourLabel = new JLabel("Hour:");
    private final JTextField hourField = new JTextField(FIELD_WIDTH);

    private final JLabel minuteLabel = new JLabel("Minute:");
    private final JTextField minuteField = new JTextField(FIELD_WIDTH);

    private final JLabel secondLabel = new JLabel("Second:");
    private final JTextField secondField = new JTextField(FIELD_WIDTH);

    private final JButton button = new JButton("Draw");
    private final ClockViewerComponent component = new ClockViewerComponent();

    private long lastIteration;

    @Override
    protected JComponent newContentPane() {
        final JPanel panel = new JPanel(new BorderLayout());

        final Calendar calendar = Calendar.getInstance();
        ClockDemonstration.this.hourField.setText(Integer.toString(calendar.get(Calendar.HOUR)));
        ClockDemonstration.this.minuteField.setText(Integer.toString(calendar.get(Calendar.MINUTE)));
        ClockDemonstration.this.secondField.setText(Integer.toString(calendar.get(Calendar.SECOND)));

        this.lastIteration = System.currentTimeMillis();
        final Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final long currentTime = System.currentTimeMillis();
                final long delta = currentTime - ClockDemonstration.this.lastIteration;
                ClockDemonstration.this.lastIteration = System.currentTimeMillis();
                calendar.add(Calendar.MILLISECOND, (int) delta);
                ClockDemonstration.this.component.setTime(calendar);
            }
        });
        timer.start();

        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                try {
                    /*
                     * Parse these first so we don't have an inconsistent
                     * calendar if one of them fails
                     */
                    final int hour = Integer.parseInt(ClockDemonstration.this.hourField.getText());
                    final int minute = Integer.parseInt(ClockDemonstration.this.minuteField.getText());
                    final int second = Integer.parseInt(ClockDemonstration.this.secondField.getText());

                    calendar.set(Calendar.HOUR, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, second);
                    ClockDemonstration.this.component.setTime(hour, minute, second);
                } catch (final NumberFormatException e) {
                    JOptionPane.showMessageDialog(panel, "Couldn't parse time!");
                }
            }
        });

        final JPanel formPanel = new JPanel();
        formPanel.add(this.hourLabel);
        formPanel.add(this.hourField);
        formPanel.add(this.minuteLabel);
        formPanel.add(this.minuteField);
        formPanel.add(this.secondLabel);
        formPanel.add(this.secondField);
        formPanel.add(this.button);

        panel.add(formPanel, BorderLayout.SOUTH);
        panel.add(this.component, BorderLayout.CENTER);
        return panel;

    }

    /**
     * Launches a {@link ClockDemonstration}.
     * 
     * @param args
     *            unused
     */
    public static void main(final String[] args) {
        Demonstration.launch(ClockDemonstration.class);
    }
}