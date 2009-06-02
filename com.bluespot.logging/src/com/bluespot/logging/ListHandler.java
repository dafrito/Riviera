package com.bluespot.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Publishes LogRecords to a List.
 * 
 * @author Aaron Faanes
 */
public class ListHandler extends Handler {

	private List<LogRecord> records;

	public ListHandler() {
		this(new ArrayList<LogRecord>());
	}

	public ListHandler(final List<LogRecord> recordList) {
		this.records = recordList;
	}

	@Override
	public void close() throws SecurityException {
		// Do nothing; this handler never has any assets that must be
		// explicitly closed.
	}

	@Override
	public void flush() {
		// Do nothing; our collection is always synchronized.
	}

	public List<LogRecord> getRecords() {
		return this.records;
	}

	@Override
	public void publish(final LogRecord record) {
		if (this.isLoggable(record)) {
			this.records.add(record);
		}
	}

	public void setRecords(final List<LogRecord> records) {
		this.records = records;
	}

}
