/**
 * 
 */
package com.bluespot.playground.game;

import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.bluespot.swing.Components;

/**
 * @author Aaron Faanes
 * 
 */
public class GameRunner implements Runnable {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new GameRunner());
	}

	@Override
	public void run() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("Must be invoked on the EDT");
		}
		Components.LookAndFeel.NATIVE.activate();
		JFrame frame = new JFrame("Game");

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new CardLayout());
		GameView view = new GameView();
		view.setFocusable(true);
		panel.add(view);
		frame.setContentPane(panel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		Components.center(frame);
		frame.setVisible(true);
	}
}
