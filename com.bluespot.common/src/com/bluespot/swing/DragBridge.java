/**
 * 
 */
package com.bluespot.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import com.bluespot.geom.vectors.Vector3i;
import com.bluespot.logic.actors.Actor;
import com.bluespot.logic.actors.Producer;

/**
 * Produces offsets in {@link Vector3i} form, passing them to an underlying
 * {@link Actor}.
 * 
 * @author Aaron Faanes
 * @see Producer
 * @see Vector3i
 * @see Actor
 */
public class DragBridge extends Producer<Vector3i> {

	private final JComponent component;

	private final Vector3i last = Vector3i.mutable();

	public DragBridge(final JComponent component, final Actor<? super Vector3i> receiver) {
		super(receiver);
		if (component == null) {
			throw new NullPointerException("component must not be null");
		}
		this.component = component;
		component.addMouseListener(mouseListener);

	}

	private final MouseListener mouseListener = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			component.addMouseMotionListener(motionListener);
			last.set(e.getX(), e.getY());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			component.removeMouseMotionListener(motionListener);
		}
	};

	private final MouseMotionListener motionListener = new MouseMotionAdapter() {

		@Override
		public void mouseDragged(MouseEvent e) {
			produce(Vector3i.frozen(e.getX() - last.x(), e.getY() - last.y()));
			last.set(e.getX(), e.getY());
		}

	};

	public void detach() {
		component.removeMouseListener(this.mouseListener);
		component.removeMouseMotionListener(this.motionListener);
	}
}
