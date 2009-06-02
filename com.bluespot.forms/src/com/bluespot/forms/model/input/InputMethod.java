package com.bluespot.forms.model.input;

public abstract class InputMethod<E> {
    
    public boolean hasEqualValue(E comparedValue) {
        E currentValue = this.getValue();
        if(currentValue == null)
            return false;
        return currentValue.equals(comparedValue);
    }
    
    public boolean hasAnyValue() {
        return this.getValue() != null;
    }
    
    public abstract E getValue();
}
