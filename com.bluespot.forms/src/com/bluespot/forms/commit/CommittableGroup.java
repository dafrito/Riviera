package com.bluespot.forms.commit;

import java.util.ArrayList;
import java.util.List;

import com.bluespot.forms.input.AggregateInputMethod;
import com.bluespot.forms.property.DefaultPropertySheet;
import com.bluespot.forms.validation.CommittableValidator;
import com.bluespot.forms.validation.ValidationResult;
import com.bluespot.forms.validation.Validator;

public class CommittableGroup extends Committable<DefaultPropertySheet> {

    protected List<Committable<?>> committables = new ArrayList<Committable<?>>();
    protected AggregateInputMethod inputMethod = new AggregateInputMethod();

    protected final Validator<Commit<DefaultPropertySheet>> validator = new Validator<Commit<DefaultPropertySheet>>() {
        private final CommittableValidator committableValidator = new CommittableValidator();

        public ValidationResult<Commit<DefaultPropertySheet>> validate(final Commit<DefaultPropertySheet> commit) {
            final ValidationResult<List<Committable<?>>> result = this.committableValidator.validate(CommittableGroup.this.committables);
            return ValidationResult.forwardResult(this, null, result);
        }
    };

    public CommittableGroup() {
        super();
        this.setInputMethod(this.inputMethod);
        this.getValidator().addValidator(this.validator);
    }

    public void addCommittable(final String name, final Committable<?> committable) {
        this.inputMethod.addInputMethod(name, committable.getCommittedInputMethod());
        this.committables.add(committable);
    }

    public void clearCommittables() {
        this.inputMethod.clearInputMethods();
        this.committables.clear();
    }

    public void removeCommittable(final String name, final Committable<?> committable) {
        this.inputMethod.removeInputMethod(name);
        this.committables.remove(committable);
    }
}
