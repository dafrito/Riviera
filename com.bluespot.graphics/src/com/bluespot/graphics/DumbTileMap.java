package com.bluespot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.bluespot.table.Table;

public class DumbTileMap extends TileMap<Color> {

	public DumbTileMap(final Table<Color> table, final int tileWidth, final int tileHeight) {
		super(table, tileWidth, tileHeight);
	}

	@Override
	protected void paintTile(final Graphics2D g, final Color value, final Point location, final int width,
			final int height) {
		g.setColor(value);
		g.fillRect(0, 0, width, height);
	}

}
