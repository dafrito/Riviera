package com.bluespot.forms.model.commit;

import java.util.EventListener;

import com.bluespot.forms.model.validation.ValidationSummary;

public interface CommitListener<E> extends EventListener {
    public void commitSuccessful(Commit<E> commit);
    public void commitFailed(ValidationSummary<Commit<E>> result);
}
