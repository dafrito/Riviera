package com.bluespot.logging.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Publishes LogRecords to a List.
 * 
 * @author Aaron Faanes
 */
public final class ListHandler extends Handler {

    private final List<LogRecord> records;

    /**
     * Constructs a list handler that populates an empty list.
     * 
     * @see #getRecords()
     */
    public ListHandler() {
        this.records = new ArrayList<LogRecord>();
    }

    /**
     * Constructs a list handler that populates the specified list.
     * 
     * @param records
     *            the list to populate
     */
    public ListHandler(final List<LogRecord> records) {
        this.records = records;
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

    /**
     * Returns the current list of records collected by this handler.
     * 
     * @return the current list of records. This list is not modifiable.
     */
    public List<LogRecord> getRecords() {
        return Collections.unmodifiableList(this.records);
    }

    @Override
    public void publish(final LogRecord record) {
        if (this.isLoggable(record)) {
            this.records.add(record);
        }
    }

}
