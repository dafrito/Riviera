/**
 * 
 */
package gui.style.dimensions;

import inspect.Inspectable;

/**
 * A stylesheet element that represents a percentage width.
 * 
 * @author Aaron Faanes
 * @see StylesheetAbsoluteWidthElement
 * @see StylesheetPercentageHeightElement
 */
@Inspectable
public class StylesheetPercentageWidthElement extends StylesheetMagnitude<Double> {

	public StylesheetPercentageWidthElement(double magnitude) {
		super(magnitude);
	}

	@Override
	public String toString() {
		return "percentage-width";
	}

}