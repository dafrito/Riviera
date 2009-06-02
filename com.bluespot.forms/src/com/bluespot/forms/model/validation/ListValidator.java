package com.bluespot.forms.model.validation;

import java.util.ArrayList;
import java.util.List;

public class ListValidator<E> implements Validator<List<E>> {

    private final Validator<E> internalValidator;

    public Validator<E> getInternalValidator() {
        return this.internalValidator;
    }

    public ListValidator(Validator<E> internalValidator) {
        this.internalValidator = internalValidator;
    }

    public ValidationResult<List<E>> validate(List<E> list) {
        List<ValidationResult<E>> results = new ArrayList<ValidationResult<E>>();
        for (E value : list) {
            results.add(this.getInternalValidator().validate(value));
        }
        return ValidationResult.forwardResult(this, list, ValidationResult.<E>collectResults(null, results));
    }

}
