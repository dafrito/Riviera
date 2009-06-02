package com.bluespot.forms.model.validation;

import java.util.ArrayList;
import java.util.List;

import com.bluespot.dispatcher.SimpleDispatcher;

public class AggregateValidator<E> implements Validator<E> {

	private ValidationSummary<E> validationSummary;

	protected void setValidationResults(E value, List<ValidationResult<E>> results) {
		this.validationSummary = ValidationResult.<E> collectResults(value, results);
	}

	public ValidationSummary<E> getValidationSummary() {
		return this.validationSummary;
	}

	private final SimpleDispatcher<E, Validator<E>> validationDispatcher = new SimpleDispatcher<E, Validator<E>>() {

		public final List<ValidationResult<E>> results = new ArrayList<ValidationResult<E>>();

		@Override
		public void dispatch(E value) {
			this.results.clear();
			super.dispatch(value);
			AggregateValidator.this.setValidationResults(value, this.results);
		}

		public void dispatch(E value, Validator<E> listener) {
			this.results.add(listener.validate(value));
		}

	};

	public boolean hasValidators() {
		return this.validationDispatcher.hasListeners();
	}

	public ValidationSummary<E> validate(E value) {
		this.validationDispatcher.dispatch(value);
		return this.getValidationSummary();
	}

	public void addValidator(Validator<E> validator) {
		this.validationDispatcher.addListener(validator);
	}

	public void removeValidator(Validator<E> validator) {
		this.validationDispatcher.removeListener(validator);
	}

}
