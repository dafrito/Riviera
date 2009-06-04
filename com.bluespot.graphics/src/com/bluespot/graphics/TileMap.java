package com.bluespot.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.Geometry;

public abstract class TileMap<T> implements Paintable {

	private final int tileHeight;

	private final int tileWidth;
	/**
	 * The point (in map coordinates) which should be at 0,0 in screen
	 * coordinates
	 */
	protected Point2D.Double origin = new Point2D.Double();

	protected final Table<T> table;

	public TileMap(final Table<T> table, final int tileWidth, final int tileHeight) {
		this.table = table;
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
	}

	public Dimension getSize() {
		final Dimension size = this.getTileSize();
		final Table<T> table = this.table;
		Geometry.scale(size, table.getWidth() + .5, table.getHeight() + 2);
		return size;
	}

	public int getTileHeight() {
		return this.tileHeight;
	}

	public Dimension getTileSize() {
		return new Dimension(this.getTileWidth(), this.getTileHeight());
	}

	public int getTileWidth() {
		return this.tileWidth;
	}

	public void paint(final Graphics2D g, final int width, final int height) {
		this.doRender(g);
	}

	public void scrollTo(final Point point) {
		System.out.println(point);

	}

	private Point adjustForOrigin(final Graphics2D g, final Point origin) {

		// Offset the origin so that the tile is drawn in-line with the other
		// tiles.
		final Point offset = g.getClipBounds().getLocation();
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

		// Move up one row and column; it's the easiest way to ensure we're
		// rendering everything
		origin.x--;
		origin.y -= 2;

		g.translate(-this.getTileWidth(), -this.getTileHeight());
		if (origin.x < 0) {
			g.translate(this.getTileWidth(), 0);
		}
		if (origin.y < 0) {
			g.translate(0, this.getTileHeight());
		}

		origin.x = Math.min(this.table.getWidth() - 1, Math.max(0, origin.x));
		origin.y = Math.min(this.table.getHeight() - 1, Math.max(0, origin.y));

		return origin;
	}

	private void renderTable(final Graphics2D g, final Table<T> renderedTable, final Dimension initialOffset) {
		g.translate(initialOffset.width, initialOffset.height);
		final Point location = new Point();
		for (int y = 0; y < renderedTable.getHeight(); y++) {
			for (int x = 0; x < renderedTable.getWidth(); x++) {
				location.setLocation(x, y);
				final T value = renderedTable.get(location);
				this.paintTile(g, value, location, this.tileWidth, this.tileHeight);
				g.translate(this.tileWidth, 0);
			}
			g.translate(-this.tileWidth * renderedTable.getWidth(), this.tileHeight);
			final Dimension offset = this.newRow(location);
			g.translate(offset.width, offset.height);
		}
	}

	protected void doRender(final Graphics2D originalG) {
		final Graphics2D g = (Graphics2D) originalG.create();
		final Point firstTile = this.adjustForOrigin(g, g.getClipBounds().getLocation());

		final Dimension initialOffset = new Dimension(this.getTileWidth() / 2, this.getTileHeight() / 2);

		final Dimension tileSize = g.getClipBounds().getSize();
		tileSize.width /= this.getTileWidth();
		tileSize.height /= this.getTileHeight();
		tileSize.height *= 2;

		// God knows how many tiles we'll need to render, so just render "alot"
		// more than we need.
		tileSize.width += 3;
		tileSize.height += 2;

		final Point lastTile = new Point(firstTile.x + tileSize.width, firstTile.y + tileSize.height * 2);
		lastTile.x = Math.min(this.table.getWidth() - 1, Math.max(0, lastTile.x));
		lastTile.y = Math.min(this.table.getHeight() - 1, Math.max(0, lastTile.y));

		final Table<T> subtable = this.table.subTable(firstTile, new Dimension(lastTile.x - firstTile.x, lastTile.y
				- firstTile.y));
		this.renderTable(g, subtable, initialOffset);

		originalG.setColor(Color.red);
		originalG.draw(originalG.getClip());
	}

	protected Dimension newRow(final Point location) {
		return new Dimension(0, 0);
	}

	protected abstract void paintTile(Graphics2D g, T value, Point location, int width, int height);

}
