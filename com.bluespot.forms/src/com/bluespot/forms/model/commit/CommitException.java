package com.bluespot.forms.model.commit;

public class CommitException extends Exception {

	private static final long serialVersionUID = 1852132192918621425L;

	public static class NoCommittedValueException extends CommitException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8701257132020912847L;

		public NoCommittedValueException(final Committable<?> source) {
			this(source, null);
		}

		public NoCommittedValueException(final Committable<?> source, final Throwable cause) {
			super(source, NoCommittedValueException.MESSAGE, cause);
		}

		public static final String MESSAGE = "The committed value is invalid or missing";
	}

	public static class PendingValueException extends CommitException {
		private static final long serialVersionUID = -5316728163675136864L;

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