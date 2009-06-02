package com.bluespot.forms.model.field;

import com.bluespot.forms.model.commit.Committable;
import com.bluespot.forms.model.commit.CommittableGroup;
import com.bluespot.property.DefaultPropertySheet;

public class Form extends Field<DefaultPropertySheet> {
    
    public Form(String name) {
        super(new CommittableGroup(), name);
    }
    
    public Form() {
        this(null);
    }

    public CommittableGroup getCommittableGroup() {
        return (CommittableGroup)this.getCommittable();
    }
    
    public void addField(Field<?> field) {
        this.getCommittableGroup().addCommittable(field.getName(), field.getCommittable());
    }
    
    public void addField(String name, Committable<?> committable) {
        this.getCommittableGroup().addCommittable(name, committable);
    }
    
    public void addField(String name, Field<?> field) {
        this.addField(name, field.getCommittable());
    }
    
    public void removeField(String name, Committable<?> committable) {
        this.getCommittableGroup().removeCommittable(name, committable);
    }
    
    public void removeField(Field<?> field) {
        this.getCommittableGroup().removeCommittable(field.getName(), field.getCommittable());
    }
    
    public void clearFields() {
        this.getCommittableGroup().clearCommittables();
    }
}
