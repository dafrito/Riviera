/**
 * 
 */
package com.bluespot.playground.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.Timer;

import com.bluespot.geom.vectors.Vector3f;
import com.bluespot.swing.Components;

/**
 * @author Aaron Faanes
 * 
 */
public class GameView extends JComponent {

	private static final long serialVersionUID = 1L;

	private final Vector3f player = Vector3f.mutable(50, 50, 0);

	Deque<Vector3f> velocities = new ArrayDeque<Vector3f>();
	private static final Map<Integer, Vector3f> directions = new HashMap<Integer, Vector3f>();
	static {
		directions.put(KeyEvent.VK_W, Vector3f.down());
		directions.put(KeyEvent.VK_S, Vector3f.up());
		directions.put(KeyEvent.VK_A, Vector3f.left());
		directions.put(KeyEvent.VK_D, Vector3f.right());
	}

	public GameView() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				Vector3f direction = directions.get(e.getKeyCode());
				if (direction == null) {
					return;
				}
				velocities.remove(direction);
				velocities.addLast(direction);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Vector3f direction = directions.get(e.getKeyCode());
				if (direction == null) {
					return;
				}
				velocities.remove(direction);
			}
		});

		new Timer(1000 / 30, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!velocities.isEmpty()) {
					player.add(velocities.peekLast());
				}
				repaint();
			}
		}).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Components.setAntialias((Graphics2D) g, true);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setColor(Color.YELLOW);
		int size = 25;
		g.fillOval(
				Math.round(player.getX()) - size,
				Math.round(player.getY()) - size,
				2 * size,
				2 * size
				);
	}

}
