package com.bluespot.forms.model.input;

public class ConstantInputMethod<E> extends InputMethod<E> {

    private final E value;

    public ConstantInputMethod(final E value) {
        this.value = value;
    }

    @Override
    public E getValue() {
        return this.value;
    }

}
