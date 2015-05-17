package graphics;

import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * Represents an object that can be painted.
 * <p>
 * This was intended to be a super-lightweight component, but I actually prefer
 * having a discrete {@link JComponent}, at least in the cases where we use this
 * interface.
 * 
 * @author Aaron Faanes
 * @see TileMap
 */
public interface Paintable {

	/**
	 * Invoked when the object should be drawn using the specified graphics
	 * context. It should draw in an area from {@code (0,0} to {@code (width,
	 * height)}.
	 * 
	 * @param g
	 *            the graphics context used in drawing. It has been translated
	 *            so the origin of this paintable is at {@code (0,0)}.
	 * @param width
	 *            the width of the drawable area
	 * @param height
	 *            the height of the drawable area
	 */
	public void paint(Graphics2D g, int width, int height);
}
