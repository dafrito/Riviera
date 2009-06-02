package com.bluespot.forms;

public enum AttributeType {
    DESCRIPTION, ID, LABEL, SELECTED, UNKNOWN, VALUE;

    public static AttributeType getAttributeType(String name) {
        if(name == null)
            throw new NullPointerException();
        try {
            return AttributeType.valueOf(name.toUpperCase());
        } catch(IllegalArgumentException e) {
            return AttributeType.UNKNOWN;
        }
    }

}
