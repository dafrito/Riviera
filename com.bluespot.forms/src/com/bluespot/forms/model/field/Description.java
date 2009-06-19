package com.bluespot.forms.model.field;

/**
 * Represents an immutable description of something.
 * 
 * @author Aaron Faanes
 * @deprecated This class should not be used. It does not decompose well; we're
 *             interested in string IDs part of the time, and full descriptions
 *             other times.
 */
@Deprecated
public final class Description {
    private final String informationBlurb;
    private final String label;
    private final String name;

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

    public String getId() {
        return this.name;
    }
}
