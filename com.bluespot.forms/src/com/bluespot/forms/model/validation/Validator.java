package com.bluespot.forms.model.validation;

public interface Validator<E> {
	public ValidationResult<E> validate(E value);
}
