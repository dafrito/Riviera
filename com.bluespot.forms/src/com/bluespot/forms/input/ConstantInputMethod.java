package com.bluespot.forms.input;

public class ConstantInputMethod<E> implements InputMethod<E> {

    private final E value;

    public ConstantInputMethod(final E value) {
        this.value = value;
    }

    @Override
    public E getValue() {
        return this.value;
    }

}
