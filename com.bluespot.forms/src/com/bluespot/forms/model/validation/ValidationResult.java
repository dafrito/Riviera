package com.bluespot.forms.model.validation;

import java.util.Collections;
import java.util.List;

public class ValidationResult<E> {

    /**
     * Creates a summary that consists of the given results, which are validation reports using the given value.
     * @param <T> The type of ValidationResults given.
     * @param value The candidate value. This was the one that was tested .
     * @param results The results of validation.
     * @return A ValidationSummary using the given parameters.
     */
    public static <T> ValidationSummary<T> collectResults(T value, List<ValidationResult<T>> results) {
        boolean successful = true;
        for (ValidationResult<T> result : results) {
            successful = successful && result.isSuccessful();
        }
        return new ValidationSummary<T>(value, successful, results);
    }

    public static <E> ValidationResult<E> forwardResult(Validator<E> validator, E value, ValidationResult<?> result) {
        return new ValidationResult<E>(validator, value, result.isSuccessful(), result.getCause());
    }

    public static <E> ValidationResult<E> result(Validator<E> validator, E value, boolean success) {
        return new ValidationResult<E>(validator, value, success);
    }

    public static <E> ValidationResult<E> failedResult(Validator<E> validator, E value, Exception cause) {
        return new ValidationResult<E>(validator, value, false, cause);
    }

    public static <E> ValidationResult<E> failedResult(Validator<E> validator, E value) {
        return new ValidationResult<E>(validator, value, false);
    }

    public static <E> ValidationResult<E> successfulResult(Validator<E> validator, E value) {
        return new ValidationResult<E>(validator, value, true);
    }

    public static <E> ValidationResult<E> failedDependencies(Validator<E> validator, E value, List<Exception> causes) {
        return new ValidationResult<E>(validator, value, false, new DependencyValidationException(causes));
    }

    public static class DependencyValidationException extends Exception {
        public static final String MESSAGE = "One or more dependencies did not validate.";
        private final List<Exception> causes;

        public DependencyValidationException(List<Exception> causes) {
            super(MESSAGE);
            this.causes = Collections.unmodifiableList(causes);
        }

        public List<Exception> getCauses() {
            return this.causes;
        }
    }

    protected final boolean successful;
    protected final Validator<E> validator;
    protected final Exception cause;
    protected final E value;

    protected ValidationResult(E value, boolean successful) {
        this(null, value, successful, null);
    }

    protected ValidationResult(Validator<E> validator, E value, boolean successful) {
        this(validator, value, successful, null);
    }

    protected ValidationResult(Validator<E> validator, E value) {
        this(validator, value, true, null);
    }

    protected ValidationResult(Validator<E> validator, E value, Exception cause) {
        this(validator, value, false, cause);
    }

    protected ValidationResult(Validator<E> validator, E value, boolean successful, Exception cause) {
        this.validator = validator;
        this.successful = successful;
        this.value = value;
        this.cause = cause;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public Validator<E> getValidator() {
        return this.validator;
    }

    public Exception getCause() {
        return this.cause;
    }

    public E getValue() {
        return this.value;
    }

}
