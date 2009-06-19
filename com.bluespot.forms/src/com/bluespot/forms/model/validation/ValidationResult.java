package com.bluespot.forms.model.validation;

import java.util.Collections;
import java.util.List;

public class ValidationResult<E> {

    public static class DependencyValidationException extends Exception {

        private static final long serialVersionUID = 6217081622731218877L;

        private final List<Exception> causes;

        public DependencyValidationException(final List<Exception> causes) {
            super(DependencyValidationException.MESSAGE);
            this.causes = Collections.unmodifiableList(causes);
        }

        public List<Exception> getCauses() {
            return this.causes;
        }

        public static final String MESSAGE = "One or more dependencies did not validate.";
    }

    protected final Exception cause;

    protected final boolean successful;

    protected final Validator<E> validator;

    protected final E value;

    protected ValidationResult(final E value, final boolean successful) {
        this(null, value, successful, null);
    }

    protected ValidationResult(final Validator<E> validator, final E value) {
        this(validator, value, true, null);
    }

    protected ValidationResult(final Validator<E> validator, final E value, final boolean successful) {
        this(validator, value, successful, null);
    }

    protected ValidationResult(final Validator<E> validator, final E value, final boolean successful,
            final Exception cause) {
        this.validator = validator;
        this.successful = successful;
        this.value = value;
        this.cause = cause;
    }

    protected ValidationResult(final Validator<E> validator, final E value, final Exception cause) {
        this(validator, value, false, cause);
    }

    public Exception getCause() {
        return this.cause;
    }

    public Validator<E> getValidator() {
        return this.validator;
    }

    public E getValue() {
        return this.value;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    /**
     * Creates a summary that consists of the given results, which are
     * validation reports using the given value.
     * 
     * @param <T>
     *            The type of ValidationResults given.
     * @param value
     *            The candidate value. This was the one that was tested .
     * @param results
     *            The results of validation.
     * @return A ValidationSummary using the given parameters.
     */
    public static <T> ValidationSummary<T> collectResults(final T value, final List<ValidationResult<T>> results) {
        boolean successful = true;
        for (final ValidationResult<T> result : results) {
            successful = successful && result.isSuccessful();
        }
        return new ValidationSummary<T>(value, successful, results);
    }

    public static <E> ValidationResult<E> failedDependencies(final Validator<E> validator, final E value,
            final List<Exception> causes) {
        return new ValidationResult<E>(validator, value, false, new DependencyValidationException(causes));
    }

    public static <E> ValidationResult<E> failedResult(final Validator<E> validator, final E value) {
        return new ValidationResult<E>(validator, value, false);
    }

    public static <E> ValidationResult<E> failedResult(final Validator<E> validator, final E value,
            final Exception cause) {
        return new ValidationResult<E>(validator, value, false, cause);
    }

    public static <E> ValidationResult<E> forwardResult(final Validator<E> validator, final E value,
            final ValidationResult<?> result) {
        return new ValidationResult<E>(validator, value, result.isSuccessful(), result.getCause());
    }

    public static <E> ValidationResult<E> result(final Validator<E> validator, final E value, final boolean success) {
        return new ValidationResult<E>(validator, value, success);
    }

    public static <E> ValidationResult<E> successfulResult(final Validator<E> validator, final E value) {
        return new ValidationResult<E>(validator, value, true);
    }

}
