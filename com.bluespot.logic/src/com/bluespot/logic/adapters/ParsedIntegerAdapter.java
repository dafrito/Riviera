package com.bluespot.logic.adapters;

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

    @Override
    public Integer adapt(final String source) {
        if (source == null) {
            return null;
        }
        try {
            return Integer.parseInt(source);
        } catch (final NumberFormatException nfe) {
            this.getHandler().accept(nfe);
            return null;
        }
    }

}
