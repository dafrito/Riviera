/**
 * 
 */
package com.bluespot.examples.graphics.tilemap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.vectors.Vector3i;
import com.bluespot.graphics.TileMap;
import com.bluespot.logic.actors.Actor;
import com.bluespot.swing.DragBridge;

/**
 * @author Aaron Faanes
 * 
 */
public class TileMapPanel extends JComponent {

	private static final long serialVersionUID = -1721472199100442129L;

	private final Vector3i origin = Vector3i.mutable();

	private final TileMap<Color> map;

	public TileMapPanel(final Table<Color> grid) {
		this.map = new TileMap<Color>(grid, Vector3i.frozen(25, 25)) {
			@Override
			protected void paintTile(Graphics2D g, Color value, Vector3i tableIndex, Vector3i tileSize) {
				g.setColor(value);
				g.fillRect(0, 0, tileSize.x(), tileSize.y());
				g.setColor(value.darker());
				g.drawRect(0, 0, tileSize.x(), tileSize.y());
			}
		};
		new DragBridge(this, new Actor<Vector3i>() {

			@Override
			public void receive(Vector3i value) {
				if (value == null) {
					return;
				}
				origin.add(value);
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		map.paint((Graphics2D) g, origin);
	}
}
