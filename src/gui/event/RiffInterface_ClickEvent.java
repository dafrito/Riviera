/**
 * 
 */
package gui.event;

import gui.MouseButton;
import inspect.Inspectable;

@Inspectable
public class RiffInterface_ClickEvent extends RiffInterface_MouseEvent {
	private final int clicks;

	public RiffInterface_ClickEvent(int x, int y, MouseButton button, int clicks) {
		super(x, y, button);
		this.clicks = clicks;
	}

	@Inspectable
	public int getClicks() {
		return this.clicks;
	}

}