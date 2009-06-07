package com.bluespot.forms.model.field;

public final class Description {
	private final String informationBlurb;
	private final String label;
	private final String name;

	public Description() {
		this("", "", "");
	}

	public Description(final String name) {
		this(name, "", "");
	}

	public Description(final String name, final String label, final String informationBlurb) {
		this.name = name;
		this.label = label;
		this.informationBlurb = informationBlurb;
	}

	public Description changeInformationBlurb(final String newInformationBlurb) {
		return new Description(this.name, this.label, newInformationBlurb);
	}

	public Description changeLabel(final String newLabel) {
		return new Description(this.name, newLabel, this.informationBlurb);
	}

	public Description changeName(final String newName) {
		return new Description(newName, this.label, this.informationBlurb);
	}

	public String getInformationBlurb() {
		return this.informationBlurb;
	}

	public String getLabel() {
		return this.label;
	}

	public String getName() {
		return this.name;
	}
}
