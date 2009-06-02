package com.bluespot.forms.model.input;

import java.util.HashMap;
import java.util.Map;

import com.bluespot.property.DefaultPropertySheet;

public class AggregateInputMethod extends InputMethod<DefaultPropertySheet> {
    
    private Map<String, InputMethod<?>> inputMethods = new HashMap<String, InputMethod<?>>();
    
    public void addInputMethod(String name, InputMethod<?> inputMethod) {
        this.inputMethods.put(name, inputMethod);
    }
    
    public InputMethod<?> removeInputMethod(String name) {
        return this.inputMethods.remove(name);
    }
    
    public void clearInputMethods() {
        this.inputMethods.clear();
    }

    @Override
    public DefaultPropertySheet getValue() {
        DefaultPropertySheet properties = new DefaultPropertySheet();
        for(Map.Entry<String, InputMethod<?>> entry : this.inputMethods.entrySet()) {
            if(!entry.getValue().hasAnyValue())
                return null;
            properties.setProperty(entry.getKey(), entry.getValue());
        }
        return properties.freeze();
    }

}
