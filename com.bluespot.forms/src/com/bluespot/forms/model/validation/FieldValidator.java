package com.bluespot.forms.model.validation;

import com.bluespot.forms.model.commit.Commit;

public class FieldValidator<E> extends ForwardingValidator<Commit<E>, E> {
    
    public FieldValidator(Validator<E> internalValidator) {
        super(internalValidator);
    }
    
    @Override
    public E getInternalValue(Commit<E> commit) {
        return commit.getNewValue();
    }

}
