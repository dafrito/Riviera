package com.bluespot.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.bluespot.collections.table.Table;
import com.bluespot.geom.Geometry;

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

    private final int tileHeight;

    private final int tileWidth;

    /**
     * The point (in map coordinates) which should be at 0,0 in screen
     * coordinates
     */
    protected Point2D.Double origin = new Point2D.Double();

    /**
     * The {@link Table} used as the data source for this tile map
     */
    protected final Table<T> table;

    /**
     * Constructs a tile map that will output the specified table.
     * 
     * @param table
     *            the drawn table
     * @param tileWidth
     *            the width of each tile
     * @param tileHeight
     *            the height of each tile
     */
    public TileMap(final Table<T> table, final int tileWidth, final int tileHeight) {
        this.table = table;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }

    /**
     * Returns the size of this tile map, in pixels.
     * 
     * @return the size of this tile map in pixels
     */
    public Dimension getSize() {
        final Dimension size = this.getTileSize();
        Geometry.Ceil.multiply(size, this.table.getWidth() + .5, this.table.getHeight() + 2);
        return size;
    }

    /**
     * Returns the height of a single tile in this tile map.
     * 
     * @return the height of a single tile
     */
    public int getTileHeight() {
        return this.tileHeight;
    }

    /**
     * Returns the width of a single tile in this tile map.
     * 
     * @return the width of a single tile
     */
    public int getTileWidth() {
        return this.tileWidth;
    }

    /**
     * Returns a new {@link Dimension} object that represents the dimensions of
     * a single tile. The returned object may be freely modified.
     * 
     * @return the size of a single tile
     */
    public Dimension getTileSize() {
        return new Dimension(this.getTileWidth(), this.getTileHeight());
    }

    @Override
	public void paint(final Graphics2D originalG, final int width, final int height) {
        final Graphics2D g = (Graphics2D) originalG.create();
        final Point firstTile = this.adjustForOrigin(g, g.getClipBounds().getLocation());

        final Dimension initialOffset = new Dimension(this.getTileWidth() / 2, this.getTileHeight() / 2);

        final Dimension tileSize = g.getClipBounds().getSize();
        tileSize.width /= this.getTileWidth();
        tileSize.height /= this.getTileHeight();
        tileSize.height *= 2;

        // God knows how many tiles we'll need to render, so just render a lot
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

    private Point adjustForOrigin(final Graphics2D g, final Point targetOrigin) {

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

        targetOrigin.x = Math.min(this.table.getWidth() - 1, Math.max(0, targetOrigin.x));
        targetOrigin.y = Math.min(this.table.getHeight() - 1, Math.max(0, targetOrigin.y));

        return targetOrigin;
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
     * @param width
     *            the width of the tile. Any painting should completely fill
     *            this space.
     * @param height
     *            the height of the tile. Any painting should completely fill
     *            this space.
     */
    protected abstract void paintTile(Graphics2D g, T value, Point tableIndex, int width, int height);

}
