package com.bluespot.forms.validation;

import com.bluespot.forms.commit.Commit;

public class FieldValidator<E> extends ForwardingValidator<Commit<E>, E> {

    public FieldValidator(final Validator<E> internalValidator) {
        super(internalValidator);
    }

    @Override
    public E getInternalValue(final Commit<E> commit) {
        return commit.getNewValue();
    }

}
