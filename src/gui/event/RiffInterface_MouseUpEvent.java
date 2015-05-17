/**
 * 
 */
package gui.event;

import gui.MouseButton;
import inspect.Inspectable;

@Inspectable
public class RiffInterface_MouseUpEvent extends RiffInterface_MouseEvent {
	public RiffInterface_MouseUpEvent(int x, int y, MouseButton button) {
		super(x, y, button);
	}
}