package com.bluespot.forms.model.input;

public abstract class InputMethod<E> {

	public abstract E getValue();

	public boolean hasAnyValue() {
		return this.getValue() != null;
	}

	public boolean hasEqualValue(final E comparedValue) {
		final E currentValue = this.getValue();
		if (currentValue == null) {
			return false;
		}
		return currentValue.equals(comparedValue);
	}
}
