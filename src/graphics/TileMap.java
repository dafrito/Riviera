package graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;

import collections.table.Table;
import collections.table.iteration.NaturalTableIterator;
import collections.table.iteration.TableIterator;
import geom.vectors.Vector3i;

/**
 * A {@link Paintable} that will output a {@link Table} as an isometric tile
 * map.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of element in the table
 */
public abstract class TileMap<T> {

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

	public void paint(final Graphics2D originalG, Vector3i origin) {
		Graphics2D g = (Graphics2D) originalG.create();
		try {
			Vector3i firstTile = Vector3i.mutable(g.getClipBounds().getLocation()).subtract(origin);
			firstTile.floorDivide(this.getTileSize());

			if (firstTile.x() > table.width() - 1 || firstTile.y() > table.height() - 1) {
				return;
			}

			Vector3i lastTile = Vector3i.mutable(g.getClipBounds().getSize());
			lastTile.ceilDivide(tileSize);
			lastTile.add(firstTile);

			if (lastTile.x() < 0 || lastTile.y() < 0) {
				return;
			}

			if (origin.x() < 0) {
				g.translate(origin.x() % tileSize.x(), 0);
			} else {
				g.translate(origin.x(), 0);
			}
			if (origin.y() < 0) {
				g.translate(0, origin.y() % tileSize.y());
			} else {
				g.translate(0, origin.y());
			}

			lastTile.setX(Math.min(this.table.width() - 1, Math.max(0, lastTile.x())));
			lastTile.setY(Math.min(this.table.height() - 1, Math.max(0, lastTile.y())));
			firstTile.setX(Math.min(this.table.width() - 1, Math.max(0, firstTile.x())));
			firstTile.setY(Math.min(this.table.height() - 1, Math.max(0, firstTile.y())));

			Table<T> subtable = this.table.subTable(firstTile, lastTile.toMutable().subtract(firstTile).add(1));
			this.renderTable(g, subtable);
		} finally {
			g.dispose();
		}
	}

	private void renderTable(final Graphics2D g, final Table<T> renderedTable) {
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
