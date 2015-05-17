/**
 * 
 */
package gui.style.dimensions;

import inspect.Inspectable;

/**
 * A stylesheet element that represents absolute width.
 * 
 * @author Aaron Faanes
 * @see StylesheetPercentageWidthElement
 * @see StylesheetAbsoluteHeightElement
 */
@Inspectable
public class StylesheetAbsoluteWidthElement extends StylesheetMagnitude<Integer> {

	public StylesheetAbsoluteWidthElement(int magnitude) {
		super(magnitude);
	}

	@Override
	public String toString() {
		return "absolute width";
	}

}