package com.bluespot.forms.model.input;

public class MutableInputMethod<E> extends InputMethod<E> {
    
    private E value;
    
    public void setValue(E value) {
        this.value = value;
    }

    @Override
    public E getValue() {
        return this.value;
    }
}
