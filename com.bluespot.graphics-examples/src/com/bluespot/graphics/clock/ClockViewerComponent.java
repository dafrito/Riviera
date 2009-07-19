package com.bluespot.graphics.clock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;

import javax.swing.JComponent;

import com.bluespot.geom.Geometry;
import com.bluespot.swing.Components;

/**
 * A {@link JComponent} that draws a clock, offering an interface to adjust the
 * time. The drawn clock will gradually adjust the minute and hour hands to show
 * the progression of time, as opposed to rigidly updating them on new minute
 * and hour arguments. The second hand is not fluidly moved, as this seems to
 * detract from the illusion of a real clock.
 * 
 * @author Aaron Faanes
 * 
 */
public class ClockViewerComponent extends JComponent {

    private static final long serialVersionUID = -4595803338525110331L;

    private int second;
    private int minute;
    private int hour;

    /**
     * Constructs a {@link ClockViewerComponent}.
     */
    public ClockViewerComponent() {
        super();
        this.setPreferredSize(new Dimension(500, 500));
    }

    /**
     * Sets the time manually using the specified arguments to determine the new
     * time. They will be clamped to valid arguments.
     * 
     * @param hours
     *            the new hour
     * @param minutes
     *            the new minute
     * @param seconds
     *            the new second
     */
    public void setTime(final int hours, final int minutes, final int seconds) {
        this.hour = Math.max(0, hours % 12);
        this.minute = Math.max(0, minutes % 60);
        this.second = Math.max(0, seconds % 60);
        this.repaint();
    }

    /**
     * Sets the time, according to the time represented by the specified
     * calendar.
     * 
     * @param calendar
     *            the calendar that represents the new time
     * @throws NullPointerException
     *             if {@code calendar} is null
     */
    public void setTime(final Calendar calendar) {
        if (calendar == null) {
            throw new NullPointerException("calendar is null");
        }
        this.setTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        Components.setAntialias(g2, true);

        final Rectangle bounds = Components.getDrawableArea(this);
        Geometry.trimToSquare(bounds);
        Geometry.alignCenter(bounds, Components.getCenter(this));

        final Point center = Geometry.getCenter(bounds);

        g2.drawOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);

        g2.setColor(Color.black);
        final int secondHandLength = (int) (.9d * bounds.width / 2);
        final int secondX = (int) (secondHandLength * Math.cos(Math.toRadians(this.second * (360 / 60)) - Math.PI / 2));
        final int secondY = (int) (secondHandLength * Math.sin(Math.toRadians(this.second * (360 / 60)) - Math.PI / 2));
        g2.drawLine(center.x, center.y, center.x + secondX, center.y + secondY);

        g2.setColor(Color.blue);
        final int minuteHandLength = (int) (.7d * bounds.width / 2);
        final int minuteX = (int) (minuteHandLength * Math.cos(Math.toRadians((this.minute + ((double) this.second) / 60)
                * (360 / 60))
                - Math.PI / 2));
        final int minuteY = (int) (minuteHandLength * Math.sin(Math.toRadians((this.minute + ((double) this.second) / 60)
                * (360 / 60))
                - Math.PI / 2));
        g2.drawLine(center.x, center.y, center.x + minuteX, center.y + minuteY);

        g2.setColor(Color.red);
        final int hourHandLength = (int) (.5d * bounds.width / 2);
        final int hourX = (int) (hourHandLength * Math.cos(Math.toRadians((this.hour + ((double) this.minute) / 60)
                * (360 / 12))
                - Math.PI / 2));
        final int hourY = (int) (hourHandLength * Math.sin(Math.toRadians((this.hour + ((double) this.minute) / 60)
                * (360 / 12))
                - Math.PI / 2));
        g2.drawLine(center.x, center.y, center.x + hourX, center.y + hourY);

    }
}