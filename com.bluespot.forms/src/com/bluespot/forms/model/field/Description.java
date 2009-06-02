package com.bluespot.forms.model.field;

public class Description {
    private final String label;
    private final String informationBlurb;
    private final String name;
    
    public Description() {
        this(null, null, null);
    }
    
    public Description(String name) {
        this(name, null, null);
    }
    
    public Description(String name, String label, String informationBlurb) {
        this.name = name;
        this.label = label;
        this.informationBlurb = informationBlurb;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Description changeName(String newName) {
        return new Description(newName, this.label, this.informationBlurb);
    }

    public String getInformationBlurb() {
        return this.informationBlurb;
    }

    public Description changeInformationBlurb(String newInformationBlurb) {
        return new Description(this.name, this.label, newInformationBlurb);
    }

    public String getLabel() {
        return this.label;
    }
    
    public Description changeLabel(String newLabel) {
        return new Description(this.name, newLabel, this.informationBlurb);
    }
}
