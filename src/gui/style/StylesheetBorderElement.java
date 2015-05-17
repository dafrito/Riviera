/**
 * 
 */
package gui.style;

import java.awt.Color;

import inspect.Inspectable;
import script.parsing.ScriptKeywordType;

public class StylesheetBorderElement {
	private int magnitude;
	private ScriptKeywordType style;
	private Color color;

	public StylesheetBorderElement(int mag, ScriptKeywordType style, Color color) {
		this.magnitude = mag;
		this.style = style;
		this.color = color;
	}

	@Inspectable
	public Color getColor() {
		return this.color;
	}

	@Inspectable
	public int getMagnitude() {
		return this.magnitude;
	}

	@Inspectable
	public ScriptKeywordType getStyle() {
		return this.style;
	}

	@Override
	public String toString() {
		return "border";
	}
}