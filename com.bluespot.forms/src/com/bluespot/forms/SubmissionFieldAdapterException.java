package com.bluespot.forms;

/**
 * A {@link SubmissionException} that occurred during an
 * {@link SubmissionFieldAdapter#adapt(Submission)} operation.
 * 
 * @author Aaron Faanes
 * 
 */
public abstract class SubmissionFieldAdapterException extends SubmissionException {

    private static final long serialVersionUID = 3043082119791950843L;

    private final SubmissionFieldAdapter<?, ?> adapter;

    SubmissionFieldAdapterException(final Submission<?> submission, final SubmissionFieldAdapter<?, ?> adapter,
            final String message) {
        super(submission, message);
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        this.adapter = adapter;
    }

    /**
     * Returns the {@link SubmissionFieldAdapter} that was the source of this
     * exception.
     * 
     * @return the {@code SubmissionFieldAdapter} that was the source of this
     *         exception
     */
    public SubmissionFieldAdapter<?, ?> getAdapter() {
        return this.adapter;
    }

}
