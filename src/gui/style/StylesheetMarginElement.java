/**
 * 
 */
package gui.style;

import inspect.Inspectable;

@Inspectable
public class StylesheetMarginElement {
	private int magnitude;

	public StylesheetMarginElement(int magnitude) {
		this.magnitude = magnitude;
	}

	@Inspectable
	public int getMagnitude() {
		return this.magnitude;
	}

	@Override
	public String toString() {
		return "margin";
	}

}