package com.bluespot.forms.model.commit;

import com.bluespot.dispatcher.SimpleDispatcher;
import com.bluespot.forms.model.input.InputMethod;
import com.bluespot.forms.model.validation.AggregateValidator;
import com.bluespot.forms.model.validation.ValidationSummary;

public class Committable<E> {

	private AggregateValidator<Commit<E>> validator;

	protected final InputMethod<? extends E> committedValueInputMethod = new InputMethod<E>() {

		@Override
		public E getValue() {
			return Committable.this.committedValue;
		}
	};

	protected E committedValue;

	private InputMethod<? extends E> inputMethod;

	private final SimpleDispatcher<ValidationSummary<Commit<E>>, CommitListener<E>> commitDispatcher = new SimpleDispatcher<ValidationSummary<Commit<E>>, CommitListener<E>>() {

		public void dispatch(ValidationSummary<Commit<E>> value, CommitListener<E> listener) {
			if (value.isSuccessful()) {
				listener.commitSuccessful(value.getValue());
			} else {
				listener.commitFailed(value);
			}
		}
	};

	public Committable() {
		this(null);
	}

	public Committable(InputMethod<E> inputMethod) {
		this(inputMethod, new AggregateValidator<Commit<E>>());
	}

	public Committable(InputMethod<E> inputMethod, AggregateValidator<Commit<E>> validator) {
		this.inputMethod = inputMethod;
		this.validator = validator;
	}

	public AggregateValidator<Commit<E>> getValidator() {
		return this.validator;
	}

	public ValidationSummary<Commit<E>> commit() {
		Commit<E> commit = this.createCommit(this.getInputMethod().getValue());
		ValidationSummary<Commit<E>> result = this.getValidator().validate(commit);
		this.committedValue = commit.getNewValue();
		this.commitDispatcher.dispatch(result);
		return result;
	}

	public InputMethod<? extends E> getInputMethod() {
		if (this.inputMethod == null)
			throw new NullPointerException("inputMethod cannot be null");
		return this.inputMethod;
	}

	public void setInputMethod(InputMethod<? extends E> inputMethod) {
		this.inputMethod = inputMethod;
	}

	protected Commit<E> createCommit(E newValue) {
		E oldValue = this.getCommittedInputMethod().getValue();
		return new Commit<E>(this, oldValue, newValue);
	}

	public void addCommitListener(CommitListener<E> listener) {
		this.commitDispatcher.addListener(listener);
	}

	public boolean hasCommitListeners() {
		return this.commitDispatcher.hasListeners();
	}

	public void removeCommitListener(CommitListener<E> listener) {
		this.commitDispatcher.removeListener(listener);
	}

	public InputMethod<? extends E> getCommittedInputMethod() {
		return this.committedValueInputMethod;
	}

	public E getCommittedValue() {
		return this.getCommittedInputMethod().getValue();
	}
}
