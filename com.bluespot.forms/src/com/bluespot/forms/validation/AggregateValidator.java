package com.bluespot.forms.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AggregateValidator<E> implements Validator<E> {

    private final List<Validator<E>> validators = new CopyOnWriteArrayList<Validator<E>>();

    protected void fireValidation(final E value) {
        final List<ValidationResult<E>> results = new ArrayList<ValidationResult<E>>();
        for (final Validator<E> validator : this.validators) {
            results.add(validator.validate(value));
        }
        this.setValidationResults(value, results);
    }

    private ValidationSummary<E> validationSummary;

    public void addValidator(final Validator<E> validator) {
        this.validators.add(validator);
    }

    public ValidationSummary<E> getValidationSummary() {
        return this.validationSummary;
    }

    public void removeValidator(final Validator<E> validator) {
        this.validators.remove(validator);
    }

    public ValidationSummary<E> validate(final E value) {
        this.fireValidation(value);
        return this.getValidationSummary();
    }

    protected void setValidationResults(final E value, final List<ValidationResult<E>> results) {
        this.validationSummary = ValidationResult.<E> collectResults(value, results);
    }

}
