package com.bluespot.forms.validation;

public interface Validator<E> {
    public ValidationResult<E> validate(E value);
}
