package com.bluespot.forms.model.commit;

import com.bluespot.forms.model.input.AggregateInputMethod;
import com.bluespot.forms.model.validation.CommittableValidator;
import com.bluespot.forms.model.validation.ValidationResult;
import com.bluespot.forms.model.validation.Validator;
import com.bluespot.property.DefaultPropertySheet;

import java.util.ArrayList;
import java.util.List;


public class CommittableGroup extends Committable<DefaultPropertySheet> {

    protected AggregateInputMethod inputMethod = new AggregateInputMethod();
    protected List<Committable<?>> committables = new ArrayList<Committable<?>>();

    protected final Validator<Commit<DefaultPropertySheet>> validator = new Validator<Commit<DefaultPropertySheet>>() {
        private final CommittableValidator committableValidator = new CommittableValidator();

        public ValidationResult<Commit<DefaultPropertySheet>> validate(Commit<DefaultPropertySheet> commit) {
            ValidationResult<List<Committable<?>>> result = this.committableValidator.validate(CommittableGroup.this.committables);
            return ValidationResult.forwardResult(this, null, result);
        }
    };

    public CommittableGroup() {
        super();
        this.setInputMethod(this.inputMethod);
        this.getValidator().addValidator(this.validator);
    }

    public void addCommittable(String name, Committable<?> committable) {
        this.inputMethod.addInputMethod(name, committable.getCommittedInputMethod());
        this.committables.add(committable);
    }

    public void removeCommittable(String name, Committable<?> committable) {
        this.inputMethod.removeInputMethod(name);
        this.committables.remove(committable);
    }

    public void clearCommittables() {
        this.inputMethod.clearInputMethods();
        this.committables.clear();
    }
}
