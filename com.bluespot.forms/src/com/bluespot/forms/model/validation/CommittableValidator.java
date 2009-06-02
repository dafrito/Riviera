package com.bluespot.forms.model.validation;

import com.bluespot.forms.model.commit.Committable;

public class CommittableValidator extends ListValidator<Committable<?>> {

    protected static final Validator<Committable<?>> VALIDATOR = new Validator<Committable<?>>() {
        public ValidationResult<Committable<?>> validate(Committable<?> committable) {
            return ValidationResult.forwardResult(this, null,
                                                  committable.commit());
        }
    };

    public CommittableValidator() {
        super(VALIDATOR);
    }

}
