package com.bluespot.forms.model.commit;

public class CommitException extends RuntimeException {

	public static class NoCommittedValueException extends CommitException {
		public NoCommittedValueException(final Committable<?> source) {
			this(source, null);
		}

		public NoCommittedValueException(final Committable<?> source, final Throwable cause) {
			super(source, NoCommittedValueException.MESSAGE, cause);
		}

		public static final String MESSAGE = "The committed value is invalid or missing";
	}

	public static class PendingValueException extends CommitException {
		public PendingValueException(final Committable<?> source) {
			this(source, null);
		}

		public PendingValueException(final Committable<?> source, final Throwable cause) {
			super(source, PendingValueException.MESSAGE, cause);
		}

		public static final String MESSAGE = "The pending value is invalid or missing";
	}

	private final Committable<?> source;

	public CommitException(final Committable<?> source, final String message) {
		this(source, message, null);
	}

	public CommitException(final Committable<?> source, final String message, final Throwable cause) {
		super(message, cause);
		this.source = source;
	}

	public Committable<?> getSource() {
		return this.source;
	}
}