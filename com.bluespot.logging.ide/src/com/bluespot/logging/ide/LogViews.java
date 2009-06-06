package com.bluespot.logging.ide;

import java.util.IllegalFormatException;
import java.util.logging.LogRecord;

public final class LogViews {

	private LogViews() {
		// Suppress default constructor to ensure non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	public static String asString(final LogRecord record) {
		if (record == null) {
			return "<null>";
		}

		if (record.getParameters() != null) {
			try {
				return String.format(record.getMessage(), record.getParameters());
			} catch (final IllegalFormatException e) {
				// Fall-through
			}
		}
		return record.getMessage();
	}

}
