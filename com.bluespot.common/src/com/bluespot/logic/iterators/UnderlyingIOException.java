/**
 * 
 */
package com.bluespot.logic.iterators;

import java.io.IOException;

public class UnderlyingIOException extends IllegalStateException {
	private static final long serialVersionUID = -1512303122764302644L;

	public UnderlyingIOException(IOException ex) {
		super(ex);
	}
}