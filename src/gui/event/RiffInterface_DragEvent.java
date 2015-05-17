/**
 * 
 */
package gui.event;

import gui.MouseButton;
import inspect.Inspectable;

@Inspectable
public class RiffInterface_DragEvent extends RiffInterface_MouseEvent {
	private final int xOffset, yOffset;
	private final double distance;

	public RiffInterface_DragEvent(int x, int y, MouseButton button, int xOffset, int yOffset, double distance) {
		super(x, y, button);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.distance = distance;
	}

	public double getDistance() {
		return this.distance;
	}

	@Inspectable
	public int getXOffset() {
		return this.xOffset;
	}

	@Inspectable
	public int getYOffset() {
		return this.yOffset;
	}
}