package com.bluespot.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import com.bluespot.collections.table.Table;
import com.bluespot.collections.table.iteration.NaturalTableIterator;
import com.bluespot.collections.table.iteration.TableIterator;
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
		return this.getTileSize().toMutable().multiply(table.dimensions()).toDimension();
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

		Point targetOrigin = g.getClipBounds().getLocation();

		// Get the position of the first origin tile
		final Vector3i firstTile = Vector3i.mutable(targetOrigin);
		firstTile.divide(this.getTileSize());

		if (firstTile.x() > table.width() - 1 || firstTile.y() > table.height() - 1) {
			g.dispose();
			return;
		}

		final Dimension tileSize = g.getClipBounds().getSize();
		tileSize.width /= this.getTileWidth();
		tileSize.height /= this.getTileHeight();

		final Vector3i lastTile = Vector3i.mutable(firstTile.x() + tileSize.width, firstTile.y() + tileSize.height);
		if (lastTile.x() < 0 || lastTile.y() < 0) {
			g.dispose();
			return;
		}
		lastTile.setX(Math.min(this.table.width() - 1, Math.max(0, lastTile.x())));
		lastTile.setY(Math.min(this.table.height() - 1, Math.max(0, lastTile.y())));
		firstTile.setX(Math.min(this.table.width() - 1, Math.max(0, firstTile.x())));
		firstTile.setY(Math.min(this.table.height() - 1, Math.max(0, firstTile.y())));

		Vector3i size = lastTile.toMutable().subtract(firstTile);
		size.add(1);
		final Table<T> subtable = this.table.subTable(firstTile, size);

		this.renderTable(g, subtable);
	}

	private void renderTable(final Graphics2D g, final Table<T> renderedTable) {
		System.out.println(renderedTable.dimensions());
		TableIterator<T> iter = new NaturalTableIterator<T>(renderedTable);
		while (iter.hasNext()) {
			T value = iter.next();
			Vector3i offset = iter.offset();
			g.translate(this.tileSize.x() * offset.x(), this.tileSize.y() * offset.y());
			this.paintTile(g, value, iter.location(), this.tileSize);
		}
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
