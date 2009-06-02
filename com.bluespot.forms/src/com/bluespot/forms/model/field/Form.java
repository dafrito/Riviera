package com.bluespot.forms.model.field;

import com.bluespot.forms.model.commit.Committable;
import com.bluespot.forms.model.commit.CommittableGroup;
import com.bluespot.property.DefaultPropertySheet;

public class Form extends Field<DefaultPropertySheet> {

	public Form() {
		this(null);
	}

	public Form(final String name) {
		super(new CommittableGroup(), name);
	}

	public void addField(final Field<?> field) {
		this.getCommittableGroup().addCommittable(field.getName(), field.getCommittable());
	}

	public void addField(final String name, final Committable<?> committable) {
		this.getCommittableGroup().addCommittable(name, committable);
	}

	public void addField(final String name, final Field<?> field) {
		this.addField(name, field.getCommittable());
	}

	public void clearFields() {
		this.getCommittableGroup().clearCommittables();
	}

	public CommittableGroup getCommittableGroup() {
		return (CommittableGroup) this.getCommittable();
	}

	public void removeField(final Field<?> field) {
		this.getCommittableGroup().removeCommittable(field.getName(), field.getCommittable());
	}

	public void removeField(final String name, final Committable<?> committable) {
		this.getCommittableGroup().removeCommittable(name, committable);
	}
}
