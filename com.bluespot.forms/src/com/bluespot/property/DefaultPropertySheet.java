package com.bluespot.property;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultPropertySheet {
    
    private boolean isImmutable;
    protected Map<String, Object> properties;
    
    public DefaultPropertySheet() {
        this(false);
    }
    
    public DefaultPropertySheet(boolean isImmutable) {
        this.properties = new HashMap<String, Object>();
        this.isImmutable = isImmutable;
    }
    
    public DefaultPropertySheet(DefaultPropertySheet other) {
        this(false, other);
    }
    
    public DefaultPropertySheet(boolean isImmutable, DefaultPropertySheet other) {
        this(false);
        for(String propertyName : other.getPropertyNames())
            this.setProperty(propertyName, other.getProperty(propertyName));
        this.isImmutable = isImmutable;
    }
    
    public Object getProperty(String propertyName) {
        return this.properties.get(propertyName);
    }

    public void setProperty(String propertyName, Object value) {
        if(this.isImmutable())
            throw new UnsupportedOperationException("PropertySheet is immutable.");
        this.properties.put(propertyName, value);
    }
    
    public Set<String> getPropertyNames() {
        return this.properties.keySet();
    }
    
    public DefaultPropertySheet freeze() {
        this.isImmutable = true;
        return this;
    }
    
    public DefaultPropertySheet getProperties(boolean isNewPropertySheetImmutable) {
        return new DefaultPropertySheet(isNewPropertySheetImmutable, this);
    }
    
    public DefaultPropertySheet getImmutableProperties() {
        return this.getProperties(true);
    }
    
    public boolean isImmutable() {
        return this.isImmutable;
    }

}