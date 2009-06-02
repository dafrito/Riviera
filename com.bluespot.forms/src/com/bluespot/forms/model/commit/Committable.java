package com.bluespot.forms.model.commit;

import com.bluespot.dispatcher.SimpleDispatcher;
import com.bluespot.forms.model.input.InputMethod;
import com.bluespot.forms.model.validation.AggregateValidator;
import com.bluespot.forms.model.validation.ValidationSummary;

public class Committable<E> {

	private final SimpleDispatcher<ValidationSummary<Commit<E>>, CommitListener<E>> commitDispatcher = new SimpleDispatcher<ValidationSummary<Commit<E>>, CommitListener<E>>() {

		public void dispatch(final ValidationSummary<Commit<E>> value, final CommitListener<E> listener) {
			if (value.isSuccessful()) {
				listener.commitSuccessful(value.getValue());
			} else {
				listener.commitFailed(value);
			}
		}
	};

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

	public void addCommitListener(final CommitListener<E> listener) {
		this.commitDispatcher.addListener(listener);
	}

	public ValidationSummary<Commit<E>> commit() {
		final Commit<E> commit = this.createCommit(this.getInputMethod().getValue());
		final ValidationSummary<Commit<E>> result = this.getValidator().validate(commit);
		this.committedValue = commit.getNewValue();
		this.commitDispatcher.dispatch(result);
		return result;
	}

	public InputMethod<? extends E> getCommittedInputMethod() {
		return this.committedValueInputMethod;
	}

	public E getCommittedValue() {
		return this.getCommittedInputMethod().getValue();
	}

	public InputMethod<? extends E> getInputMethod() {
		if (this.inputMethod == null) {
			throw new NullPointerException("inputMethod cannot be null");
		}
		return this.inputMethod;
	}

	public AggregateValidator<Commit<E>> getValidator() {
		return this.validator;
	}

	public boolean hasCommitListeners() {
		return this.commitDispatcher.hasListeners();
	}

	public void removeCommitListener(final CommitListener<E> listener) {
		this.commitDispatcher.removeListener(listener);
	}

	public void setInputMethod(final InputMethod<? extends E> inputMethod) {
		this.inputMethod = inputMethod;
	}

	protected Commit<E> createCommit(final E newValue) {
		final E oldValue = this.getCommittedInputMethod().getValue();
		return new Commit<E>(this, oldValue, newValue);
	}
}
