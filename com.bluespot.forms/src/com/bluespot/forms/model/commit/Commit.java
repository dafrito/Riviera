package com.bluespot.forms.model.commit;

public class Commit<E> {
	private final E newValue;
	private final E oldValue;
	private final Committable<E> source;

	public Commit(final Committable<E> source, final E oldValue, final E newValue) {
		this.source = source;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public E getNewValue() {
		return this.newValue;
	}

	public E getOldValue() {
		return this.oldValue;
	}

	public Committable<E> getSource() {
		return this.source;
	}

}
