package com.bluespot.forms;

/**
 * A {@link Exception} that relates to {@link Submission} objects.
 * 
 * @author Aaron Faanes
 * 
 */
public class SubmissionException extends Exception {

    private static final long serialVersionUID = -7828917085638647096L;
    private final Submission<?> submission;

    /**
     * Constructs a {@link SubmissionException} that indicates a special
     * condition occurred with the specified {@code Submission} object. The
     * condition is described using the specified message.
     * 
     * @param submission
     *            the submission that was the source of this exception. It must
     *            not be null.
     * @param message
     *            the message that describes the exception. It must not be null.
     * @throws NullPointerException
     *             if {@code submission} is null
     */
    public SubmissionException(final Submission<?> submission, final String message) {
        super(message);
        if (submission == null) {
            throw new NullPointerException("submission is null");
        }
        this.submission = submission;
    }

    /**
     * Returns the {@link Submission} object that was the source of this
     * exception.
     * 
     * @return the {@code Submission} object that was the source of this
     *         exception
     */
    public Submission<?> getSubmission() {
        return this.submission;
    }

    @Override
    public String toString() {
        return String.format("SubmissionException[%s]: %s", this.getSubmission(), this.getMessage());
    }

}
