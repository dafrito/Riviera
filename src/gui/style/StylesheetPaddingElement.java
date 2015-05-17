/**
 * 
 */
package gui.style;

import inspect.Inspectable;

@Inspectable
public class StylesheetPaddingElement {
	private int magnitude;

	public StylesheetPaddingElement(int magnitude) {
		this.magnitude = magnitude;
	}

	@Inspectable
	public int getMagnitude() {
		return this.magnitude;
	}

	@Override
	public String toString() {
		return "padding";
	}

}