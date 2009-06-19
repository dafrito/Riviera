package com.bluespot.forms.model.input;

public class MutableInputMethod<E> implements InputMethod<E> {

    private E value;

    public E getValue() {
        return this.value;
    }

    public void setValue(final E value) {
        this.value = value;
    }
}
