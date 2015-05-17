/**
 * 
 */
package gui.event;

import inspect.Inspectable;

@Inspectable
public class RiffInterface_KeyEvent implements RiffInterface_Event {
	private int key;

	public RiffInterface_KeyEvent(int key) {
		this.key = key;
	}

	@Inspectable
	public int getKeyCode() {
		return this.key;
	}
}