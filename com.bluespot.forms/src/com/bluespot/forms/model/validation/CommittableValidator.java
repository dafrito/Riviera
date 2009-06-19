package com.bluespot.forms.model.validation;

import com.bluespot.forms.model.commit.Committable;

public final class CommittableValidator extends ListValidator<Committable<?>> {

    public CommittableValidator() {
        super(CommittableValidator.VALIDATOR);
    }

    private static final Validator<Committable<?>> VALIDATOR = new Validator<Committable<?>>() {
        public ValidationResult<Committable<?>> validate(final Committable<?> committable) {
            return ValidationResult.forwardResult(this, null, committable.commit());
        }
    };

}
