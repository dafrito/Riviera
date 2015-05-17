/**
 * 
 */
package gui.style;

import java.awt.Color;

import inspect.Inspectable;

@Inspectable
public class StylesheetBackgroundColorElement {
	private Color color;

	public StylesheetBackgroundColorElement(Color backgroundColor) {
		this.color = backgroundColor;
	}

	@Inspectable
	public Color getColor() {
		return this.color;
	}

	@Override
	public String toString() {
		return "background-color";
	}

}