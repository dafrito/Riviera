package com.bluespot.forms.model.commit;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bluespot.forms.model.input.InputMethod;
import com.bluespot.forms.model.validation.AggregateValidator;
import com.bluespot.forms.model.validation.ValidationSummary;

public class Committable<E> {

    private final List<CommitListener<E>> listeners = new CopyOnWriteArrayList<CommitListener<E>>();

    private InputMethod<? extends E> inputMethod;

    private AggregateValidator<Commit<E>> validator;

    protected E committedValue;

    protected final InputMethod<? extends E> committedValueInputMethod = new InputMethod<E>() {

        @Override
        public E getValue() {
            return Committable.this.committedValue;
        }
    };

    public Committable() {
        this(null);
    }

    public Committable(final InputMethod<E> inputMethod) {
        this(inputMethod, new AggregateValidator<Commit<E>>());
    }

    public Committable(final InputMethod<E> inputMethod, final AggregateValidator<Commit<E>> validator) {
        this.inputMethod = inputMethod;
        this.validator = validator;
    }

    public InputMethod<? extends E> getCommittedInputMethod() {
        return this.committedValueInputMethod;
    }

    public E getCommittedValue() {
        return this.getCommittedInputMethod().getValue();
    }

    public ValidationSummary<Commit<E>> commit() {
        final Commit<E> commit = this.createCommit(this.getInputMethod().getValue());
        final ValidationSummary<Commit<E>> result = this.getValidator().validate(commit);
        this.committedValue = commit.getNewValue();
        this.fireCommitResult(result);
        return result;
    }

    protected Commit<E> createCommit(final E newValue) {
        final E oldValue = this.getCommittedInputMethod().getValue();
        return new Commit<E>(this, oldValue, newValue);
    }

    public InputMethod<? extends E> getInputMethod() {
        return this.inputMethod;
    }

    public void setInputMethod(final InputMethod<? extends E> inputMethod) {
        this.inputMethod = inputMethod;
    }

    public AggregateValidator<Commit<E>> getValidator() {
        return this.validator;
    }

    public void addCommitListener(final CommitListener<E> listener) {
        this.listeners.add(listener);
    }

    public void removeCommitListener(final CommitListener<E> listener) {
        this.listeners.remove(listener);
    }

    protected void fireCommitResult(final ValidationSummary<Commit<E>> value) {
        for (final CommitListener<E> listener : this.listeners) {
            if (value.isSuccessful()) {
                listener.commitSuccessful(value.getValue());
            } else {
                listener.commitFailed(value);
            }
        }
    }

}
