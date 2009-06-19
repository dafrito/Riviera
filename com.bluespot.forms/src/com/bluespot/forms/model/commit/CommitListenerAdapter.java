package com.bluespot.forms.model.commit;

import com.bluespot.forms.model.validation.ValidationSummary;

public class CommitListenerAdapter<E> implements CommitListener<E> {

    public void commitFailed(final ValidationSummary<Commit<E>> result) {
        // Do nothing.
    }

    public void commitSuccessful(final Commit<E> commit) {
        // Do nothing.
    }

}
