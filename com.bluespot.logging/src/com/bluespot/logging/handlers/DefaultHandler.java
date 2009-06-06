package com.bluespot.logging.handlers;

import java.util.logging.Handler;

/**
 * A {@link Handler} implementation that ignores the {@link #close()} and
 * {@link #flush()} methods.
 * 
 * @author Aaron Faanes
 * 
 */
public abstract class DefaultHandler extends Handler {

	@Override
	public void close() throws SecurityException {
		// Do nothing; this has no resources to close
	}

	@Override
	public void flush() {
		// Do nothing; this has no resources to flush
	}

}
