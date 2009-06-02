package com.bluespot.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.bluespot.geom.Geometry;
import com.bluespot.table.Table;
import com.bluespot.table.TableIterator;

public abstract class TileMap<T> implements Paintable {

	protected final Table<T> table;

	private final int tileWidth;
	private final int tileHeight;

	/**
	 * The point (in map coordinates) which should be at 0,0 in screen
	 * coordinates
	 */
	protected Point2D.Double origin = new Point2D.Double();
	
	public TileMap(Table<T> table, int tileWidth, int tileHeight) {
		this.table = table;
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
	}

	public void paint(Graphics2D g, int width, int height) {
		this.doRender(g);
	}
	
	private Point adjustForOrigin(Graphics2D g, Point origin) {
		
		// Offset the origin so that the tile is drawn in-line with the other tiles.
		Point offset = g.getClipBounds().getLocation();
		System.out.println(offset);
		offset.x %= this.getTileWidth();
		offset.y %= this.getTileHeight();
		g.translate(-offset.x, -offset.y);
		
		g.translate(origin.x, origin.y);
		
		// Get the position of the "major" tile
		origin.x /= this.getTileWidth();
		origin.y /= this.getTileHeight();
		
		// Multiply by two since our rows are overlapping
		origin.y *= 2;
		
		// Move up one row and column; it's the easiest way to ensure we're rendering everything
		origin.x--;
		origin.y -= 2;
		
		g.translate(-this.getTileWidth(), -this.getTileHeight());
		if( origin.x < 0 )
			g.translate(this.getTileWidth(), 0);
		if( origin.y < 0 )
			g.translate(0, this.getTileHeight());
		
		origin.x = Math.min(this.table.getWidth() -1, Math.max(0, origin.x));
		origin.y = Math.min(this.table.getHeight() -1, Math.max(0, origin.y));
		
		return origin;
	}

	protected void doRender(Graphics2D originalG) {
		Graphics2D g = (Graphics2D) originalG.create();
		Point firstTile = this.adjustForOrigin(g, g.getClipBounds().getLocation());
		
		Dimension initialOffset = new Dimension(this.getTileWidth()/2,this.getTileHeight()/2);
		
		Dimension tileSize = g.getClipBounds().getSize();
		tileSize.width /= this.getTileWidth();
		tileSize.height /= this.getTileHeight();
		tileSize.height *= 2;
		
		// God knows how many tiles we'll need to render, so just render "alot" more than we need.
		tileSize.width += 3;
		tileSize.height += 2;
		
		Point lastTile = new Point(firstTile.x + tileSize.width, firstTile.y + tileSize.height*2);
		lastTile.x = Math.min(this.table.getWidth() - 1, Math.max(0, lastTile.x));
		lastTile.y = Math.min(this.table.getHeight() - 1, Math.max(0, lastTile.y));
		
		Table<T> subtable = this.table.subTable(firstTile, new Dimension(lastTile.x - firstTile.x, lastTile.y - firstTile.y));
		this.renderTable(g, subtable,initialOffset);
		
		originalG.setColor(Color.red);
		originalG.draw(originalG.getClip());
	}
	
	private void renderTable(Graphics2D g, Table<T> renderedTable, Dimension initialOffset) {
		g.translate(initialOffset.width, initialOffset.height);
		Point location = new Point();
		for(int y = 0; y < renderedTable.getHeight(); y++) {
		    for(int x = 0; x < renderedTable.getWidth(); x++) {
		        location.setLocation(x, y);
		        T value = renderedTable.get(location);
		        this.paintTile(g, value, location, this.tileWidth, this.tileHeight);
	            g.translate(this.tileWidth, 0);
		    }
		    g.translate(-this.tileWidth * renderedTable.getWidth(), this.tileHeight);
            Dimension offset = this.newRow(location);
            g.translate(offset.width, offset.height);
		}
	}

	protected Dimension newRow(Point location) {
		return Geometry.EMPTY_SIZE;
	}

	public Dimension getTileSize() {
		return new Dimension(this.getTileWidth(), this.getTileHeight());
	}

	protected abstract void paintTile(Graphics2D g, T value, Point location, int width, int height);

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public Dimension getSize() {
		Dimension size = this.getTileSize();
		Table<T> table = this.table;
		Geometry.scale(size, table.getWidth() + .5, table.getHeight() + 2);
		return size;
	}

	public void scrollTo(Point point) {
		System.out.println(point);
		
	}

}
