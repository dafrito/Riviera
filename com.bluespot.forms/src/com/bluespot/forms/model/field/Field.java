package com.bluespot.forms.model.field;

import com.bluespot.forms.model.commit.Committable;

public class Field<E> {

	private Committable<E> committable;
	private Description description = new Description();

	public Field(final Committable<E> committable) {
		this(committable, "");
	}

	public Field(final Committable<E> committable, final String name) {
		this.committable = committable;
		this.setName(name);
	}

	public Committable<E> getCommittable() {
		return this.committable;
	}

	public Description getDescription() {
		return this.description;
	}

	public String getInformationBlurb() {
		return this.getDescription().getInformationBlurb();
	}

	public String getLabel() {
		return this.getDescription().getLabel();
	}

	public String getName() {
		return this.getDescription().getName();
	}

	public void setInformationBlurb(final String newInformationBlurb) {
		this.setDescription(this.getDescription().changeInformationBlurb(newInformationBlurb));
	}

	public void setLabel(final String label) {
		this.setDescription(this.getDescription().changeLabel(label));
	}

	public void setName(final String name) {
		this.setDescription(this.getDescription().changeName(name));
	}

	protected void setDescription(final Description description) {
		this.description = description;
	}
}
