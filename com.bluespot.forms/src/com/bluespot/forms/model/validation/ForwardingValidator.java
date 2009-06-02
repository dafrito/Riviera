package com.bluespot.forms.model.validation;


public abstract class ForwardingValidator<E, T> implements Validator<E> {
    
    private final Validator<T> internalValidator;
    
    public Validator<T> getInternalValidator() {
        return this.internalValidator;
    }

    public ForwardingValidator(Validator<T> internalValidator) {
        this.internalValidator = internalValidator;
    }
    
    /**
     * Converts the value E to the internal value T so that the internal Validator can validate it.
     * This is not validation; the call here should always work.
     * 
     * @param value the value to convert
     * @return the converted value
     */
    public abstract T getInternalValue(E value);

    public ValidationResult<E> validate(E value) {
        ValidationResult<T> internalResult = this.getInternalValidator().validate(this.getInternalValue(value));
        return ValidationResult.forwardResult(this, value, internalResult);
    }

}
