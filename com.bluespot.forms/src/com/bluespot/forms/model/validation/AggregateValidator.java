package com.bluespot.forms.model.validation;

import java.util.ArrayList;
import java.util.List;

import com.bluespot.dispatcher.SimpleDispatcher;

public class AggregateValidator<E> implements Validator<E> {

	private final SimpleDispatcher<E, Validator<E>> validationDispatcher = new SimpleDispatcher<E, Validator<E>>() {

		public final List<ValidationResult<E>> results = new ArrayList<ValidationResult<E>>();

		@Override
		public void dispatch(final E value) {
			this.results.clear();
			super.dispatch(value);
			AggregateValidator.this.setValidationResults(value, this.results);
		}

		public void dispatch(final E value, final Validator<E> listener) {
			this.results.add(listener.validate(value));
		}

	};

	private ValidationSummary<E> validationSummary;

	public void addValidator(final Validator<E> validator) {
		this.validationDispatcher.addListener(validator);
	}

	public ValidationSummary<E> getValidationSummary() {
		return this.validationSummary;
	}

	public boolean hasValidators() {
		return this.validationDispatcher.hasListeners();
	}

	public void removeValidator(final Validator<E> validator) {
		this.validationDispatcher.removeListener(validator);
	}

	public ValidationSummary<E> validate(final E value) {
		this.validationDispatcher.dispatch(value);
		return this.getValidationSummary();
	}

	protected void setValidationResults(final E value, final List<ValidationResult<E>> results) {
		this.validationSummary = ValidationResult.<E> collectResults(value, results);
	}

}
