/**
 * 
 */
package gui.event;

import gui.MouseButton;
import inspect.Inspectable;

@Inspectable
public class RiffInterface_MouseDownEvent extends RiffInterface_MouseEvent {
	public RiffInterface_MouseDownEvent(int x, int y, MouseButton button) {
		super(x, y, button);
	}
}