package com.bluespot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.bluespot.table.Table;

public class DumbTileMap extends TileMap<Color> {

    public DumbTileMap(Table<Color> table, int tileWidth, int tileHeight) {
        super(table, tileWidth, tileHeight);
    }

    @Override
    protected void paintTile(Graphics2D g, Color value, Point location, int width, int height) {
        g.setColor(value);
        g.fillRect(0, 0, width, height);
    }

}
