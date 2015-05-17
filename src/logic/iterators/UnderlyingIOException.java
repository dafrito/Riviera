/**
 * 
 */
package logic.iterators;

import java.io.IOException;

public class UnderlyingIOException extends IllegalStateException {
	private static final long serialVersionUID = -1512303122764302644L;

	private final IOException underlying;

	public UnderlyingIOException(IOException ex) {
		super(ex);
		if (ex == null) {
			throw new NullPointerException("ex must not be null");
		}
		this.underlying = ex;
	}

	@Override
	public IOException getCause() {
		return this.underlying;
	}
}