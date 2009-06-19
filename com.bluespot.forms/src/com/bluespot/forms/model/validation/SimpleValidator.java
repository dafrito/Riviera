package com.bluespot.forms.model.validation;

public abstract class SimpleValidator<E> implements Validator<E> {
    public abstract boolean checkValue(E value);

    public ValidationResult<E> validate(final E value) {
        return ValidationResult.result(this, value, this.checkValue(value));
    }
}
