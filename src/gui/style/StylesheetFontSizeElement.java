/**
 * 
 */
package gui.style;

import inspect.Inspectable;

@Inspectable
public class StylesheetFontSizeElement {
	private int fontSize;

	public StylesheetFontSizeElement(int fontSize) {
		this.fontSize = fontSize;
	}

	@Inspectable
	public int getFontSize() {
		return this.fontSize;
	}

	@Override
	public String toString() {
		return "font-size";
	}

}