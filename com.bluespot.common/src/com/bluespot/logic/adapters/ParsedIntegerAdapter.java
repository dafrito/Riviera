package com.bluespot.logic.adapters;

import com.bluespot.logic.actors.Actor;

/**
 * An {@link HandledAdapter} implementation that parses and converts string
 * values to integers.
 * <p>
 * It will report {@link NumberFormatException} events if they are thrown during
 * conversion. They will not, however, be thrown.
 * 
 * @author Aaron Faanes
 * @see HandledAdapter
 */
public class ParsedIntegerAdapter extends AbstractHandledAdapter<String, Integer, NumberFormatException> {

	/**
	 * Constructs a {@link ParsedIntegerAdapter} that uses the default no-op
	 * {@link Actor} for its handler.
	 */
	public ParsedIntegerAdapter() {
		super();
	}

	/**
	 * Constructs a {@link ParsedIntegerAdapter} that uses the specified
	 * {@link Actor} for its handler.
	 * 
	 * @param handler
	 *            the handler that is notified of {@link NumberFormatException}
	 *            that occur during adapting. It may not be null
	 * @throws NullPointerException
	 *             if {@code handler} is null
	 */
	public ParsedIntegerAdapter(final Actor<? super NumberFormatException> handler) {
		super(handler);
	}

	@Override
	public Integer adapt(final String source) {
		if (source == null) {
			return null;
		}
		try {
			return Integer.parseInt(source);
		} catch (final NumberFormatException nfe) {
			this.dispatch(nfe);
			return null;
		}
	}

}
