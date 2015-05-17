/**
 * Copyright (c) 2015 Aaron Faanes
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package examples.graphics.tictactoe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import demonstration.Demonstration;
import examples.forms.SimpleFormDemonstration;
import swing.Components;

/**
 * @author Aaron Faanes
 * 
 */
public final class TicTacToe extends Demonstration {
	static enum State {
		EMPTY,
		X,
		O
	}

	static class Panel extends JComponent implements MouseListener {
		private static final long serialVersionUID = 179586090415438793L;

		private List<State> states = new ArrayList<>();

		private State whoseTurn = State.X;

		Panel() {
			this.addMouseListener(this);

			for (int i = 0; i < 9; ++i) {
				states.add(State.EMPTY);
			}
		}

		@Override
		protected void paintComponent(final Graphics g) {
			super.paintComponent(g);

			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			int thirdWidth = this.getWidth() / 3;
			int thirdHeight = this.getHeight() / 3;

			for (int r = 0; r < 3; ++r) {
				for (int c = 0; c < 3; ++c) {
					State state = get(r, c);
					if (state == State.X) {
						// Draw an X on the square.
						g.setColor(Color.blue);
						g.drawLine(
								c * thirdWidth,
								r * thirdHeight,
								(1 + c) * thirdWidth,
								(1 + r) * thirdHeight
								);

						g.drawLine(
								(1 + c) * thirdWidth,
								r * thirdHeight,
								c * thirdWidth,
								(1 + r) * thirdHeight
								);
					} else if (state == State.O) {
						g.setColor(Color.red);
						// Draw a O on the square.
						g.drawArc(
								c * thirdWidth,
								r * thirdHeight,
								thirdWidth,
								thirdHeight,
								0,
								360
								);
					}
				}
			}

			g.setColor(Color.black);
			g.drawLine(thirdWidth, 0, thirdWidth, this.getHeight());
			g.drawLine(thirdWidth * 2, 0, thirdWidth * 2, this.getHeight());

			g.drawLine(0, thirdHeight, this.getWidth(), thirdHeight);
			g.drawLine(0, thirdHeight * 2, this.getWidth(), thirdHeight * 2);
		}

		public State get(int row, int column) {
			return states.get(row * 3 + column);
		}

		public void take(int row, int column) {
			states.set(row * 3 + column, whoseTurn);
			if (whoseTurn == State.X) {
				whoseTurn = State.O;
			} else {
				whoseTurn = State.X;
			}
			repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int cx = e.getPoint().x / (this.getWidth() / 3);
			int cy = e.getPoint().y / (this.getHeight() / 3);

			State state = get(cy, cx);
			if (state == State.EMPTY) {
				// Make the move.
				take(cy, cx);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	@Override
	protected JComponent newContentPane() {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(300, 300));

		final Panel game = new TicTacToe.Panel();
		game.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(game, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Runs a {@link SimpleFormDemonstration} using {@link Demonstration}.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Components.LookAndFeel.NATIVE.activate();
		Demonstration.launch(TicTacToe.class);
	}

}
