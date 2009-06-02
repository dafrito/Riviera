package com.bluespot.forms.model.input;

public class MutableInputMethod<E> extends InputMethod<E> {

	private E value;

	@Override
	public E getValue() {
		return this.value;
	}

	public void setValue(final E value) {
		this.value = value;
	}
}
