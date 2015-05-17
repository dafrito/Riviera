/**
 * 
 */
package gui.style.dimensions;

import inspect.Inspectable;

public abstract class StylesheetMagnitude<T> {

	private T magnitude;

	protected StylesheetMagnitude(T magnitude) {
		this.magnitude = magnitude;
	}

	@Inspectable
	public T getMagnitude() {
		return this.magnitude;
	}

	public void setMagnitude(T magnitude) {
		this.magnitude = magnitude;
	}
}