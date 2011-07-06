package com.bluespot.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.Geometry;
import com.bluespot.geom.vectors.Vector3i;

/**
 * A {@link Paintable} that will output a {@link Table} as an isometric tile
 * map.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of element in the table
 */
public abstract class TileMap<T> implements Paintable {

	private final Vector3i tileSize;

	/**
	 * The point (in map coordinates) which should be at 0,0 in screen
	 * coordinates
	 */
	protected Vector3i origin = Vector3i.mutable();

	/**
	 * The {@link Table} used as the data source for this tile map
	 */
	protected final Table<T> table;

	/**
	 * Constructs a tile map that will output the specified table.
	 * 
	 * @param table
	 *            the drawn table
	 * @param tileSize
	 *            the dimensions of a single tile
	 */
	public TileMap(final Table<T> table, final Vector3i tileSize) {
		this.table = table;
		this.tileSize = tileSize.toFrozen();
	}

	/**
	 * Returns the size of this tile map, in pixels.
	 * 
	 * @return the size of this tile map in pixels
	 */
	public Dimension getSize() {
		final Dimension size = this.getTileSize().toDimension();
		Geometry.Ceil.multiply(size, this.table.width() + .5, this.table.height() + 2);
		return size;
	}

	/**
	 * Returns the dimensions of a single tile.
	 * 
	 * @return the dimensions of a single tile
	 */
	public Vector3i getTileSize() {
		return this.tileSize;
	}

	protected int getTileWidth() {
		return this.tileSize.x();
	}

	protected int getTileHeight() {
		return this.tileSize.y();
	}

	@Override
	public void paint(final Graphics2D originalG, final int width, final int height) {
		final Graphics2D g = (Graphics2D) originalG.create();
		final Vector3i firstTile = this.adjustForOrigin(g, g.getClipBounds().getLocation());

		final Dimension initialOffset = new Dimension(this.getTileWidth() / 2, this.getTileHeight() / 2);

		final Dimension tileSize = g.getClipBounds().getSize();
		tileSize.width /= this.getTileWidth();
		tileSize.height /= this.getTileHeight();
		tileSize.height *= 2;

		// God knows how many tiles we'll need to render, so just render a lot
		// more than we need.
		tileSize.width += 3;
		tileSize.height += 2;

		final Vector3i lastTile = Vector3i.mutable(firstTile.x() + tileSize.width, firstTile.y() + tileSize.height * 2);
		lastTile.setX(Math.min(this.table.width() - 1, Math.max(0, lastTile.x())));
		lastTile.setY(Math.min(this.table.height() - 1, Math.max(0, lastTile.y())));

		final Table<T> subtable = this.table.subTable(firstTile, Vector3i.mutable(lastTile.x() - firstTile.x(), lastTile.y()
				- firstTile.y()));
		this.renderTable(g, subtable, initialOffset);
	}

	private Vector3i adjustForOrigin(final Graphics2D g, final Point targetOrigin) {

		// Offset the origin so that the tile is drawn in-line with the other
		// tiles.
		final Point offset = g.getClipBounds().getLocation();
		System.out.println(offset);
		offset.x %= this.getTileWidth();
		offset.y %= this.getTileHeight();
		g.translate(-offset.x, -offset.y);

		g.translate(targetOrigin.x, targetOrigin.y);

		// Get the position of the "major" tile
		targetOrigin.x /= this.getTileWidth();
		targetOrigin.y /= this.getTileHeight();

		// Multiply by two since our rows are overlapping
		targetOrigin.y *= 2;

		// Move up one row and column; it's the easiest way to ensure we're
		// rendering everything
		targetOrigin.x--;
		targetOrigin.y -= 2;

		g.translate(-this.getTileWidth(), -this.getTileHeight());
		if (targetOrigin.x < 0) {
			g.translate(this.getTileWidth(), 0);
		}
		if (targetOrigin.y < 0) {
			g.translate(0, this.getTileHeight());
		}

		targetOrigin.x = Math.min(this.table.width() - 1, Math.max(0, targetOrigin.x));
		targetOrigin.y = Math.min(this.table.height() - 1, Math.max(0, targetOrigin.y));

		return Vector3i.mutable(targetOrigin);
	}

	private void renderTable(final Graphics2D g, final Table<T> renderedTable, final Dimension initialOffset) {
		g.translate(initialOffset.width, initialOffset.height);
		final Vector3i location = Vector3i.mutable();
		for (int y = 0; y < renderedTable.height(); y++) {
			for (int x = 0; x < renderedTable.width(); x++) {
				location.set(x, y, 0);
				final T value = renderedTable.get(location);
				this.paintTile(g, value, location, this.tileSize);
				g.translate(this.tileSize.x(), 0);
			}
			g.translate(-this.tileSize.x() * renderedTable.width(), this.tileSize.y());
			final Dimension offset = this.newRow();
			g.translate(offset.width, offset.height);
		}
	}

	/**
	 * A (poorly-named) method that is called whenever a new row is entered. The
	 * dimension returned will be used to adjust the graphics context.
	 * 
	 * @return the offset of the graphics context for the specified row
	 */
	protected Dimension newRow() {
		return new Dimension(0, 0);
	}

	/**
	 * Paints the specified tile. The tile should be drawn in a rectangle with
	 * the top-left corner at the origin, and the size being the {@code width}
	 * and {@code height}.
	 * 
	 * @param g
	 *            the context to use to draw the tile
	 * @param value
	 *            the value of the current tile
	 * @param tableIndex
	 *            the index of the tile in the table. Do not use this as the
	 *            origin - it will not work.
	 * @param tileSize
	 *            the dimensions of the tile.
	 */
	protected abstract void paintTile(Graphics2D g, T value, Vector3i tableIndex, Vector3i tileSize);

}
