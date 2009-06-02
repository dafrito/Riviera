package com.bluespot.forms.model.field;

import com.bluespot.forms.model.commit.Committable;

public class Field<E> {
    
    private Description description = new Description();
    private Committable<E> committable;

    public Field(Committable<E> committable) {
        this(committable, null);
    }
    
    public Field(Committable<E> committable, String name) {
        this.committable = committable;
        this.setName(name);
    }
    
    public Description getDescription() {
        return this.description;
    }
    
    protected void setDescription(Description description) {
        this.description = description;
    }
    
    public String getName() {
        return this.getDescription().getName();
    }
    
    public String getLabel() {
        return this.getDescription().getLabel();
    }
    
    public String getInformationBlurb() {
        return this.getDescription().getInformationBlurb();
    }
    
    public void setName(String name) {
        this.setDescription(this.getDescription().changeName(name));
    }   

    public void setLabel(String label) {
        this.setDescription(this.getDescription().changeLabel(label));
    }
    
    public void setInformationBlurb(String newInformationBlurb) {
        this.setDescription(this.getDescription().changeInformationBlurb(newInformationBlurb));
    }
    
    public Committable<E> getCommittable() {
        return this.committable;
    }
}
