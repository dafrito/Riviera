package com.bluespot.forms.model.field;

import com.bluespot.forms.model.commit.Committable;

public class Field<E> {

    private final Committable<E> committable;
    private final Description description;

    public Field(final Committable<E> committable, final Description description) {
        if (committable == null) {
            throw new NullPointerException("committable is null");
        }
        if (description == null) {
            throw new NullPointerException("description is null");
        }
        this.committable = committable;
        this.description = description;
    }

    public Committable<E> getCommittable() {
        return this.committable;
    }

    public Description getDescription() {
        return this.description;
    }

}
